package br.com.zup.edu.nossospotify.musica;

import static org.assertj.core.api.Assertions.assertThat;

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

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
//@ActiveProfiles("test")
class DetalharMusicaControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MusicaRepository musicaRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ArtistaRepository artistaRepository;
    private Artista artista;
    private Album album;

    @BeforeEach
    void setUp() {
        musicaRepository.deleteAll();
        artistaRepository.deleteAll();
    }

    //detalhar música
    @Test
    @DisplayName("deve detalhar uma musica")
    void detalharMusicaTest() throws Exception {

        Artista brunoMars = new Artista("Bruno Mars", "Honululu", "Havaí");
        Artista paulMc = new Artista("Paul McCartney", "Liverpool", "Merseyside");

        List<Artista> artistas = List.of(brunoMars, paulMc);

        artistaRepository.saveAll(artistas);

        Musica musica = new Musica("Treasure", brunoMars);

        HashSet<Artista> participantes = new HashSet<>(artistas);

        participantes.remove(brunoMars);

        musica.adicionar(participantes);

        musicaRepository.save(musica);

        MockHttpServletRequestBuilder request = get("/musicas/{id}", musica.getId()).
                contentType(MediaType.APPLICATION_JSON);

        String payload = mockMvc.perform(request)
                .andExpect(
                        status().isOk()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);


        DetalharMusicaResponse response = mapper.readValue(
                payload, DetalharMusicaResponse.class
        );

        assertThat(response)
                .extracting(DetalharMusicaResponse::getNome, DetalharMusicaResponse::getDono)
                .contains(
                        musica.getNome(),
                        brunoMars.getNome()
                );

        assertThat(response.getParticipacoes())
                .hasSize(1)
                .contains(participantes.stream().map(Artista::getNome).toArray(String[]::new));
    }


    //não deve detalhar uma música que não foi cadastrada
    @Test
    @DisplayName("não deve detalhar uma musica")
    void detalharMusicaNaoCadastradaTest() throws Exception {

        Artista brunoMars = new Artista("Bruno Mars", "Honululu", "Havaí");
        Artista paulMc = new Artista("Paul McCartney", "Liverpool", "Merseyside");

        List<Artista> artistas = List.of(brunoMars, paulMc);

        artistaRepository.saveAll(artistas);

        Musica musica = new Musica("Treasure", brunoMars);

        HashSet<Artista> participantes = new HashSet<>(artistas);

        participantes.remove(brunoMars);

        musica.adicionar(participantes);

        musicaRepository.save(musica);

        MockHttpServletRequestBuilder request = get("/musicas/{id}", 10000)
                .contentType(MediaType.APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Musica nao cadastrada.", ((ResponseStatusException) resolvedException).getReason());


    }
}