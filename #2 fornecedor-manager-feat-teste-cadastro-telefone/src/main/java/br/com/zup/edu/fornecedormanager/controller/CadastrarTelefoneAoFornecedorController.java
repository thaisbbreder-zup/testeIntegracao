package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.model.Telefone;
import br.com.zup.edu.fornecedormanager.repository.FornecedorRepository;
import br.com.zup.edu.fornecedormanager.repository.TelefoneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/fornecedores/{id}")
public class CadastrarTelefoneAoFornecedorController {
    private final FornecedorRepository fornecedorRepository;
    private final TelefoneRepository telefoneRepository;

    public CadastrarTelefoneAoFornecedorController(FornecedorRepository fornecedorRepository, TelefoneRepository telefoneRepository) {
        this.fornecedorRepository = fornecedorRepository;
        this.telefoneRepository = telefoneRepository;
    }

    @PostMapping("/telefones")
    @Transactional
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid TelefoneRequest request, @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder) {

        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor nao cadastrado no sistema."));

        Telefone telefone = request.paraTelefone(fornecedor);

        telefoneRepository.save(telefone);

        URI location = uriComponentsBuilder.path("/fornecedores/{id}/telefones/{idTelefone}")
                .buildAndExpand(fornecedor.getId(), telefone.getId())
                .toUri();


        return ResponseEntity.created(location).build();
    }
}
