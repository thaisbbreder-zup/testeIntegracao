package br.com.zup.edu.petmanager.controller;

import br.com.zup.edu.petmanager.controller.request.PetRequest;
import br.com.zup.edu.petmanager.model.Pet;
import br.com.zup.edu.petmanager.repository.PetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/v1/pets")
public class CadastrarPetController {
    private final PetRepository repository;

    public CadastrarPetController(PetRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid PetRequest request, UriComponentsBuilder uriComponentsBuilder){
        Pet pet = request.paraPet();

        repository.save(pet);

        URI location = uriComponentsBuilder.path("/pets/{id}")
                .buildAndExpand(pet.getId())
                .toUri();

        return created(location).build();
    }
}
