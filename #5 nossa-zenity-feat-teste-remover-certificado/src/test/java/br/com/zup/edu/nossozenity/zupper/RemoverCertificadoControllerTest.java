package br.com.zup.edu.nossozenity.zupper;

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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class RemoverCertificadoControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CertificadoRepository certificadoRepository;
    @Autowired
    private ZupperRepository zupperRepository;
    @Autowired
    private MockMvc mockMvc;
    private Zupper zupper;

    @BeforeEach
    void setUp() {
        certificadoRepository.deleteAll();
        zupperRepository.deleteAll();;
    }

    //deleta certificado cadastrado
    @Test
    @DisplayName("Deve deletar certificado  cadastrado")
    void deletaCertificadoCadastrado() throws Exception {
        Zupper zupper = new Zupper("Thaís Breder", "Desenvolvedora Assistente", LocalDate.now(), "thais.breder@zup.com.bt");
        zupperRepository.save(zupper);

        Certificado certificado = new Certificado("Teste de Integração com Spring",
                "Zup Edu - Handora",
                "https://handora.zup.com.br/cardapio/59/atividades",
                zupper,
                TipoCertificado.CURSO);

        certificadoRepository.save(certificado);

        MockHttpServletRequestBuilder request = delete("/certificados/{id}", certificado.getId());
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(
                        status().isNoContent()
                );
        assertFalse(certificadoRepository.existsById(certificado.getId()), "Não deve existir um certificado para este id");
    }


    @Test
    @DisplayName("Não deve deletar certificado não cadastrado")
    void naoDeletaCertificadoNaoCadastrado() throws Exception {
        MockHttpServletRequestBuilder request = delete("/certificados/{id}", Integer.MAX_VALUE).
                contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Certificado não cadastrado.", ((ResponseStatusException) resolvedException).getReason());
    }

}
