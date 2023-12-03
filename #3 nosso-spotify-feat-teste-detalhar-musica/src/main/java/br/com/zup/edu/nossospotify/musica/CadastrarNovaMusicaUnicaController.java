package br.com.zup.edu.nossospotify.musica;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class CadastrarNovaMusicaUnicaController {
    private final ArtistaRepository artistaRepository;
    private final MusicaRepository repository;

    public CadastrarNovaMusicaUnicaController(ArtistaRepository artistaRepository, MusicaRepository repository) {
        this.artistaRepository = artistaRepository;
        this.repository = repository;
    }

    @PostMapping("/artistas/{id}/musicas")
    public ResponseEntity<?> cadastrar(@RequestBody @Valid MusicaSoloRequest request, @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder){
        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista nao cadastrado no sistema."));

        Musica musica = request.paraMusica(artista);

        repository.save(musica);

        URI location = uriComponentsBuilder.path("/artistas/{id}/musicas/{idMusica}")
                .buildAndExpand(artista.getId(), musica.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }
}
