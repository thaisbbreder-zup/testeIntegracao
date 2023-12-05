package br.com.zup.edu.fornecedormanager.repository;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<Fornecedor,Long> {
}
