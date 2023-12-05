package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.model.Telefone;

import javax.validation.constraints.NotBlank;

public class TelefoneRequest {
    @NotBlank
    private String ddd;

    @NotBlank
    private String numero;

    public TelefoneRequest(String ddd, String numero) {
        this.ddd = ddd;
        this.numero = numero;
    }

    public TelefoneRequest() {
    }

    public Telefone paraTelefone(Fornecedor fornecedor){
        return new Telefone(fornecedor,ddd,numero);
    }

    public String getDdd() {
        return ddd;
    }

    public String getNumero() {
        return numero;
    }
}
