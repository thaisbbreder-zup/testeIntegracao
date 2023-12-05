package br.com.zup.edu.petmanager.controller;

import br.com.zup.edu.petmanager.controller.request.PetRequest;
import br.com.zup.edu.petmanager.controller.request.TipoPetRequest;
import br.com.zup.edu.petmanager.model.Pet;
import br.com.zup.edu.petmanager.repository.PetRepository;
import br.com.zup.edu.petmanager.util.MensagemDeErro;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("teste") //executa testes no banco especifico para isso

class CadastrarPetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PetRepository petRepository;

    //limpa o banco antes de cada teste
    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
    }

    @Test
    @DisplayName("deve cadastrar um pet")
    void cadastraPetComDadosValidos() throws Exception {
        // Cenário
        PetRequest petRequest = new PetRequest("Chico", "Schitzu", TipoPetRequest.CAO, LocalDate.parse("2021-03-04"));

        String payload = mapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .header("Accept-Language", "pt-br");

        //Ação
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("http://localhost/pets/*"));

        // Corretude
        List<Pet> pets = petRepository.findAll();
        assertEquals(1, pets.size());
    }

    @Test
    @DisplayName("nao deve cadastrar um pet com dados invalidos")
    void cadastraPetComDadosInvalidos() throws Exception {
        // Cenário
        PetRequest petRequest = new PetRequest(null, "", null,  null);

        String payload = mapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .header("Accept-Language", "pt-br");

        // Ação
        String payloadResponse = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        //corretude
        MensagemDeErro mensagemDeErro = mapper.readValue(payloadResponse, MensagemDeErro.class);

        assertEquals(4, mensagemDeErro.getMensagens().size());
        assertThat(mensagemDeErro.getMensagens(), containsInAnyOrder(
                "O campo dataNascimento não deve ser nulo",
                "O campo tipo não deve ser nulo",
                "O campo raca não deve estar em branco",
                "O campo nome não deve estar em branco"
        ));
    }

    @Test
    @DisplayName("nao deve cadastrar um pet com data de nascimento no presente ou futuro")
    void cadastraPetComDataDeNascimentoInvalida() throws Exception {
        // Cenário
        PetRequest petRequest = new PetRequest("Chico", "Schitzu", TipoPetRequest.CAO,LocalDate.now());

        String payload = mapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .header("Accept-Language", "pt-br");

        // Ação
        String payloadResponse = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        //corretude
        MensagemDeErro mensagemDeErro = mapper.readValue(payloadResponse, MensagemDeErro.class);

        assertEquals(1, mensagemDeErro.getMensagens().size());
        assertThat(mensagemDeErro.getMensagens(), containsInAnyOrder(
                "O campo dataNascimento deve ser uma data passada"
        ));
    }
    }
