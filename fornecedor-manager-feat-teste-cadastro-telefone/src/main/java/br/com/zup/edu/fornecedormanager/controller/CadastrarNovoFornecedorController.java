package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.repository.FornecedorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/fornecedores")
public class CadastrarNovoFornecedorController {
    private final FornecedorRepository repository;

    public CadastrarNovoFornecedorController(FornecedorRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid FornecedorRequest request, UriComponentsBuilder uriComponentsBuilder){

        Fornecedor novoFornecedor = request.paraFornecedor();

        repository.save(novoFornecedor);

        URI location = uriComponentsBuilder.path("/fornecedores/{id}")
                .buildAndExpand(novoFornecedor.getId())
                .toUri();


        return ResponseEntity.created(location).build();
    }
}
