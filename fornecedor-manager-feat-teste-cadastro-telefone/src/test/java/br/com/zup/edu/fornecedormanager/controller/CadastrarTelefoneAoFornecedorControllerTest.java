package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.Telefone;
import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.repository.FornecedorRepository;
import br.com.zup.edu.fornecedormanager.repository.TelefoneRepository;
import br.com.zup.edu.fornecedormanager.util.MensagemDeErro;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


    @SpringBootTest
    @AutoConfigureMockMvc(printOnlyOnFailure = false)
    @ActiveProfiles("teste")
    class CadastrarNovoFornecedorControllerTest {
        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper mapper;
        @Autowired
        private FornecedorRepository fornecedorRepository;
        @Autowired
        private TelefoneRepository telefoneRepository;
        private Fornecedor fornecedor;

        @BeforeEach
        void setUp() {
            telefoneRepository.deleteAll();
            fornecedorRepository.deleteAll();
            //1 É cadastrado um fornecedor
            this.fornecedor = new Fornecedor("João Maia", "Bebidas", "Ambev" );
            fornecedorRepository.save(this.fornecedor);
        }

        //cadastra telefone válido
        @Test
        @DisplayName("cadastra um fornecedor com telefone em branco")
        void cadastraTelefoneAoFornecedorComDadosValidos() throws Exception {
            TelefoneRequest telefoneRequest = new TelefoneRequest("031","989874400");
            String payload = mapper.writeValueAsString(telefoneRequest);

            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .post("/fornecedores/{id}/telefones", fornecedor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "pt-br")
                    .content(payload);

            mockMvc.perform(request)
                    .andExpect(
                            status().isCreated()
                    ) .andExpect(
                            redirectedUrlPattern("http://localhost/fornecedores/*/telefones/*")
                    );

            List<Telefone> telefones = telefoneRepository.findAll();

            assertEquals(1, telefones.size());
        }

        //cadastra telefone inválido
        @Test
        @DisplayName("não cadastra telefone com dados inválidos")
        void cadastraTelefoneAoFornecedorComDadosInvalidos() throws Exception {
            //2 É criado um Json referente ao TelefoneRequest com dados invalidos
            TelefoneRequest telefoneRequest = new TelefoneRequest(null,null);
            String payload = mapper.writeValueAsString(telefoneRequest);

            //3 É construido uma requisição POST
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .post("/fornecedores/{id}/telefones", fornecedor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "pt-br")
                    .content(payload);

            //4 É executada a requisição, e validado se o status é BadRequest
            String payloadResponse = mockMvc.perform(request)
                    .andExpect(
                            status().isBadRequest()
                    )
                    .andReturn()
                    .getResponse()
                    .getContentAsString(UTF_8);

            MensagemDeErro mensagemDeErro = mapper.readValue(payloadResponse, MensagemDeErro.class);

            //5 É verificado se existem duas mensagens de erro, e se as mensagens informam que os campos endereco e cep não devem estar em branco.
            assertEquals(2, mensagemDeErro.getMensagens().size());
            assertThat(mensagemDeErro.getMensagens(), containsInAnyOrder(
                            "O campo ddd não deve estar em branco", "O campo numero não deve estar em branco"
                    )
            );
        }

        //não cadastra telefone se o fornecedor não existir
        @Test
        @DisplayName("nao deve cadastrar um telefone se o fornecedor nao existir")
        void cadastraTelefoneAoFornecedorInexistente() throws Exception {
            TelefoneRequest telefoneRequest = new TelefoneRequest("031", "989874400");
            String payload = mapper.writeValueAsString(telefoneRequest);

            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .post("/fornecedores/{id}/telefones", 1000)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload);

            mockMvc.perform(request)
                    .andExpect(
                            status().isNotFound()
                    );

        }

    }