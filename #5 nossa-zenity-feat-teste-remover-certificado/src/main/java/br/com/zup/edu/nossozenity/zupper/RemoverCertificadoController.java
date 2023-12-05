package br.com.zup.edu.nossozenity.zupper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@RestController
public class RemoverCertificadoController {
    private final CertificadoRepository repository;

    public RemoverCertificadoController(CertificadoRepository repository) {
        this.repository = repository;
    }

    @DeleteMapping("/certificados/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id) {

        Certificado certificado = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificado não cadastrado."));

        repository.delete(certificado);

        return ResponseEntity.noContent().build();

    }
}
