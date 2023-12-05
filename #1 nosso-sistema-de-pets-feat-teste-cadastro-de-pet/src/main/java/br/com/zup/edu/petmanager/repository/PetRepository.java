package br.com.zup.edu.petmanager.repository;

import br.com.zup.edu.petmanager.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet,Long> {
}
