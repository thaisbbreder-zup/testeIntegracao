package br.com.zup.edu.ingressoagora.controller;

import br.com.zup.edu.ingressoagora.model.EstadoIngresso;
import br.com.zup.edu.ingressoagora.model.Evento;
import br.com.zup.edu.ingressoagora.model.Ingresso;
import br.com.zup.edu.ingressoagora.repository.EventoRepository;
import br.com.zup.edu.ingressoagora.repository.IngressoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
//@ActiveProfiles("test")
@AutoConfigureMockMvc
class CancelarIngressoControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private IngressoRepository ingressoRepository;
    @Autowired
    private EventoRepository eventoRepository;
    private Ingresso ingresso;
    private Evento evento;

    @BeforeEach
    void setUp() {
        ingressoRepository.deleteAll();
        eventoRepository.deleteAll();
    }

    @Test
    @DisplayName("deve cancelar um ingresso válido")
    void cancelaIngressoValidoTest() throws Exception {
        // Cria um evento com título, data e preço definido de acordo com o model
        this.evento = new Evento(
                "Curso Teste de Integração com Spring", LocalDate.now().plusDays(5), new BigDecimal("200")
        );
        eventoRepository.save(evento);

        // Cria e salva um ingresso associado ao evento acima
        Ingresso ingresso = new Ingresso(evento);
        ingressoRepository.save(ingresso);

        // Prepara uma requisição PATCH para cancelar o ingresso recém-criado
        MockHttpServletRequestBuilder request = patch("/ingressos/{id}/cancelamento", ingresso.getId())
                .contentType(MediaType.APPLICATION_JSON);

        // Realiza a requisição e espera que o status retornado seja 204 (No Content)
        mockMvc.perform(request)
                .andExpect(
                        status().isNoContent() //requisição bem sucedida mas não há conteúdo para ser retornado
                );

        // Busca o ingresso no repositório pelo ID
        Optional<Ingresso> ingressoParaSerCancelado = ingressoRepository.findById(ingresso.getId());
        // Garante que o ingresso foi encontrado
        assertTrue(ingressoParaSerCancelado.isPresent());

        // Obtém o ingresso após o cancelamento
        Ingresso ingressoAposCancelamento = ingressoParaSerCancelado.get();

        // Verifica se o estado do ingresso após o cancelamento foi atualizado para CANCELADO
        assertEquals(EstadoIngresso.CANCELADO, ingressoAposCancelamento.getEstado());
    }


    @Test
    @DisplayName("não deve cancelar um ingresso inexistente")
    void naoCancelaIngressoInexistenteTest() throws Exception {
        MockHttpServletRequestBuilder request = patch("/ingressos/{id}/cancelamento", Integer.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                // retorna um objeto MvcResult que contém informações sobre a resposta HTTP, como status, cabeçalhos e corpo
                .andReturn()
                // obtem a exceção que foi resolvida durante o processamento da solicitação
                .getResolvedException();

        //verifica se o objeto resolvedException não é nulo
        assertNotNull(resolvedException);
        //verificando se a exceção que ocorreu durante o processamento da solicitação foi uma instância da classe ResponseStatusException
        assertEquals(ResponseStatusException.class, resolvedException.getClass());

        assertEquals("Este ingresso não existe.", ((ResponseStatusException) resolvedException).getReason());
    }

    @Test
    @DisplayName("não deve cancelar um ingresso no dia do evento")
    void naoCancelaIngressoNoDiaTest() throws Exception {
        // Cria um evento no momento atual
        this.evento = new Evento(
                "Curso Teste de Integração com Spring", LocalDate.now(), new BigDecimal("200")
        );
        eventoRepository.save(evento);

        Ingresso ingresso = new Ingresso(evento);
        ingressoRepository.save(ingresso);

        MockHttpServletRequestBuilder request = patch("/ingressos/{id}/cancelamento", ingresso.getId())
                .contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isUnprocessableEntity() //não foi processada por semântica inválida ou dados inválidos.
                )
                .andReturn()
                .getResolvedException();


        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());

        assertEquals(
                "Não é possível cancelar faltando menos de 1 dia para a data do evento",
                ((ResponseStatusException) resolvedException).getReason()
        );
    }

    @Test
    @DisplayName("não deve cancelar um ingresso usado")
    void naoCancelaIngressoConsumidoTest() throws Exception {
        this.evento = new Evento(
                "Curso Teste de Integração com Spring", LocalDate.now().plusDays(5), new BigDecimal("200")
        );
        eventoRepository.save(evento);

        Ingresso ingresso = new Ingresso(evento);
        ingresso.consumir();
        ingressoRepository.save(ingresso);

        MockHttpServletRequestBuilder request = patch("/ingressos/{id}/cancelamento", ingresso.getId())
                .contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isUnprocessableEntity()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals(
                "Impossível cancelar um Ingresso já consumido.",
                ((ResponseStatusException) resolvedException).getReason()
        );
    }
}



