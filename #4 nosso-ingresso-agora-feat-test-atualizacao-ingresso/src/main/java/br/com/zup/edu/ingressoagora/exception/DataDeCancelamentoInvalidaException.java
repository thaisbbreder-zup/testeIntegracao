package br.com.zup.edu.ingressoagora.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class DataDeCancelamentoInvalidaException extends RuntimeException {
    public DataDeCancelamentoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
