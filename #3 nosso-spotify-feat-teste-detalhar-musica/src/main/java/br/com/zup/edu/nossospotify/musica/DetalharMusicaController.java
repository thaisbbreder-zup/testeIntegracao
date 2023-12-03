package br.com.zup.edu.nossospotify.musica;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class DetalharMusicaController {
    private final MusicaRepository repository;

    public DetalharMusicaController(MusicaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/musicas/{id}")
    public ResponseEntity<?> detalhar(@PathVariable Long id){

        Musica musica = repository.findByIdAndParticpantes(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Musica nao cadastrada."));

        return ok(new DetalharMusicaResponse(musica));
    }
}
