package br.com.zup.edu.universidade.controller;

import br.com.zup.edu.universidade.controller.request.AvaliacaoAlunoRequest;
import br.com.zup.edu.universidade.controller.request.RespostaQuestaoRequest;
import br.com.zup.edu.universidade.model.*;
import br.com.zup.edu.universidade.repository.AlunoRepository;
import br.com.zup.edu.universidade.repository.AvaliacaoRepository;
import br.com.zup.edu.universidade.repository.RespostaAvaliacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.zup.edu.universidade.model.Aluno;
import br.com.zup.edu.universidade.model.Avaliacao;
import br.com.zup.edu.universidade.model.Questao;
import br.com.zup.edu.universidade.model.RespostaAvaliacao;

@SpringBootTest
//@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class FazerAvaliacaoControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    @Autowired
    private RespostaAvaliacaoRepository respostaAvaliacaoRepository;

    private Avaliacao avaliacao;

    private Set<Questao> questoes = new LinkedHashSet<>();
    private Aluno aluno;

    @BeforeEach
    void setUp() {
        respostaAvaliacaoRepository.deleteAll();
        avaliacaoRepository.deleteAll();
        alunoRepository.deleteAll();

        questoes.addAll(Set.of(
                new Questao("quem descobriu a america?", "Cristóvão Colombo", new BigDecimal("1")),
                new Questao("quem descobriu o Brasil?", "Pedro Alvares Cabral", new BigDecimal("2"))
        ));

        this.avaliacao = new Avaliacao(this.questoes);
        avaliacaoRepository.save(avaliacao);

        this.aluno = new Aluno("Rafael Ponte", "A234", LocalDate.of(1984, 3, 7));
        alunoRepository.save(aluno);
    }

    @Test
    @DisplayName("um aluno deve responder a avaliacao")
    void alunoValidoTest() throws Exception {
        String resposta = "qualquer coisa quero zerar";
        List<RespostaQuestaoRequest> questoesRequest = questoes.stream()
                .map(q -> new RespostaQuestaoRequest(q.getId(), resposta))
                .collect(Collectors.toList());

        AvaliacaoAlunoRequest avaliacaoAlunoRequest = new AvaliacaoAlunoRequest(questoesRequest);

        String payload = mapper.writeValueAsString(avaliacaoAlunoRequest);

        MockHttpServletRequestBuilder request = post(
                "/alunos/{id}/avaliacoes/{idAvaliacao}/respostas", aluno.getId(), avaliacao.getId()
        )
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        String location = mockMvc.perform(request)
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        redirectedUrlPattern("http://localhost/alunos/*/avaliacoes/*/respostas/*")
                )
                .andReturn()
                .getResponse()
                //Obtém a URL do cabeçalho de localização da resposta
                .getHeader("location");

        //Extrai o ID da resposta da URL obtida.
        String idResposta = location.substring(location.lastIndexOf("/") + 1);

        //Verifica se a resposta foi salva no banco de dados
        Optional<RespostaAvaliacao> possivelResposta = respostaAvaliacaoRepository.findById(Long.valueOf(idResposta));
        assertTrue(possivelResposta.isPresent());
        RespostaAvaliacao respostaAvaliacao = possivelResposta.get();


        assertThat(respostaAvaliacao.getRespostas())
                .hasSize(2)
                //Indica que estamos extraindo a propriedade questao de cada objeto RespostaQuestao na lista
                .extracting(RespostaQuestao::getQuestao)
                //Continua a extração, agora da propriedade id de cada objeto Questao associado às respostas
                .extracting(Questao::getId)
                .contains(
                        //Converte a lista original de questoes para uma matriz de IDs
                        questoes.stream().map(Questao::getId).toArray(Long[]::new)
                );
    }

    @Test
    @DisplayName("aluno nao cadastrado nao deve responder avaliacao")
    void alunoNaoCadastradoTest() throws Exception {
        String resposta = "Bill Gates";

        // Cria uma lista de solicitações de resposta para cada questão, mapeando todas as questões existentes
        List<RespostaQuestaoRequest> questoesRequest = questoes.stream()
                .map(x -> new RespostaQuestaoRequest(x.getId(), resposta))
                .collect(Collectors.toList());

        // Cria uma solicitação de avaliação do aluno, utilizando as respostas criadas anteriormente
        AvaliacaoAlunoRequest avaliacaoAlunoRequest = new AvaliacaoAlunoRequest(questoesRequest);

        String payload = mapper.writeValueAsString(avaliacaoAlunoRequest);

        MockHttpServletRequestBuilder request = post("/alunos/{id}/avaliacoes/{idAvaliacao}/respostas", Integer.MAX_VALUE, avaliacao.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());

        ResponseStatusException exception = (ResponseStatusException) resolvedException;

        assertEquals("aluno nao cadastrado", exception.getReason());
    }

    @Test
    @DisplayName("nao deve responder uma avaliacao nao cadastrada")
    void avaliacaoNaoCadastradoTest() throws Exception {
        String resposta = "Bill Gates";

         List<RespostaQuestaoRequest> questoesRequest = questoes.stream()
                .map(q -> new RespostaQuestaoRequest(q.getId(), resposta))
                .collect(Collectors.toList());

         AvaliacaoAlunoRequest avaliacaoAlunoRequest = new AvaliacaoAlunoRequest(questoesRequest);

        String payload = mapper.writeValueAsString(avaliacaoAlunoRequest);

        MockHttpServletRequestBuilder request = post("/alunos/{id}/avaliacoes/{idAvaliacao}/respostas",  aluno.getId(), Integer.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());

        ResponseStatusException exception = (ResponseStatusException) resolvedException;

        assertEquals("Avaliacao não cadastrada", exception.getReason());
    }
}



//avaliacao nao cadastrada
//avaliacao com dados invelidos
//aluno cadastrado
//questao na existe
