package br.com.zup.edu.ingressoagora.controller;

import br.com.zup.edu.ingressoagora.model.Ingresso;
import br.com.zup.edu.ingressoagora.repository.IngressoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@RestController
public class CancelarIngressoController {
    private final IngressoRepository repository;

    public CancelarIngressoController(IngressoRepository repository) {
        this.repository = repository;
    }

    @PatchMapping("/ingressos/{id}/cancelamento")
    @Transactional
    public ResponseEntity<?> cancelamento(@PathVariable Long id) {

        Ingresso ingresso = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Este ingresso não existe."));


        if (!ingresso.aptoACancelarPorData()) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Não é possivel cancelar faltando menos de 1 dia para data do evento");
        }

        if (ingresso.isConsumido()) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Impossivel cancelar um Ingresso já consumido.");
        }

        ingresso.cancelar();

        return noContent().build();
    }
}
