package br.com.zup.edu.nossozenity.zupper;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KudoRepository extends JpaRepository<Kudo,Long> {
    List<Kudo> findAllByRecebidoId(Long id);

}
