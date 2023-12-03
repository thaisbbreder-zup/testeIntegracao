package br.com.zup.edu.fornecedormanager.repository;

import br.com.zup.edu.fornecedormanager.model.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepository extends JpaRepository<Telefone,Long> {
}
