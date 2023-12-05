package br.com.zup.edu.nossozenity.zupper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.assertj.core.groups.Tuple;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
class ListarKudosRecebidosDoZupperControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private KudoRepository kudoRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ZupperRepository zupperRepository;

    private Zupper rafael;
    private Zupper jordi;

    @BeforeEach
    void setUp() {
        kudoRepository.deleteAll();
        zupperRepository.deleteAll();
        this.jordi = new Zupper("Jordi Henrique", "Dev Back-end", LocalDate.now(), "jordi.silva@zup.com.br");
        this.rafael = new Zupper("Rafael Ponte", "Dev Back-end", LocalDate.now(), "rafael.ponte@zup.com.br");
        List<Zupper> zuppers = List.of(this.jordi, rafael);
        zupperRepository.saveAll(zuppers);

    }

    @Test
    @DisplayName("deve retornar um conjunto de kudos vazios caso o zupper nao tenha recebido")
    void test() throws Exception {

        MockHttpServletRequestBuilder request = get("/zupper/{id}/kudos/recebidos", rafael.getId()).
                contentType(MediaType.APPLICATION_JSON);

        String payload = mockMvc.perform(request)
                .andExpect(
                        status().isOk()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = mapper.getTypeFactory();

        List<KudoResponse> response = mapper.readValue(
                payload,
                typeFactory.constructCollectionType(List.class, KudoResponse.class)
        );

        assertTrue(response.isEmpty());

    }

    @Test
    @DisplayName("deve listar os kudos recebidos por um zupper")
    void test1() throws Exception {

        Kudo alemDasExpectativas = new Kudo(TipoKudo.ALEM_DAS_EXPECTATIVAS, jordi, rafael);
        Kudo execelenteMentor = new Kudo(TipoKudo.EXCELENTE_MENTOR, jordi, rafael);

        List<Kudo> kudos = List.of(alemDasExpectativas, execelenteMentor);

        kudoRepository.saveAll(kudos);

        MockHttpServletRequestBuilder request = get("/zupper/{id}/kudos/recebidos", rafael.getId()).
                contentType(MediaType.APPLICATION_JSON);

        String payload = mockMvc.perform(request)
                .andExpect(
                        status().isOk()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = mapper.getTypeFactory();

        List<KudoResponse> response = mapper.readValue(
                payload,
                typeFactory.constructCollectionType(List.class, KudoResponse.class)
        );

//        assertEquals(2, response.size());
        assertThat(response)
                .hasSize(2)
                .extracting("nome", "enviadoPor")
                .contains(
                        new Tuple(TipoKudo.ALEM_DAS_EXPECTATIVAS.name().toLowerCase(Locale.ROOT), jordi.getNome()),
                        new Tuple(TipoKudo.EXCELENTE_MENTOR.name().toLowerCase(Locale.ROOT), jordi.getNome())
                );

    }

    @Test
    @DisplayName("nao deve listar os kudos recebidos por um zupper nao cadastrado")
    void test2() throws Exception {


        MockHttpServletRequestBuilder request = get("/zupper/{id}/kudos/recebidos", Integer.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();


        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class,resolvedException.getClass());
        assertEquals("Zupper nao cadastrado",((ResponseStatusException) resolvedException).getReason());


    }

}

