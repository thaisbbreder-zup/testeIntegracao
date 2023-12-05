package br.com.zup.edu.nossozenity.zupper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class ListarKudosRecebidosDoZupperController {
    private final KudoRepository repository;
    private final ZupperRepository zupperRepository;

    public ListarKudosRecebidosDoZupperController(KudoRepository repository, ZupperRepository zupperRepository) {
        this.repository = repository;
        this.zupperRepository = zupperRepository;
    }

    @GetMapping("/zupper/{id}/kudos/recebidos")
    @Transactional
    public ResponseEntity<?> listar(@PathVariable Long id) {

        if (!zupperRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zupper nao cadastrado");
        }

        List<KudoResponse> response = repository.findAllByRecebidoId(id)
                .stream()
                .map(KudoResponse::new)
                .collect(Collectors.toList());

        return ok(response);
    }
}
