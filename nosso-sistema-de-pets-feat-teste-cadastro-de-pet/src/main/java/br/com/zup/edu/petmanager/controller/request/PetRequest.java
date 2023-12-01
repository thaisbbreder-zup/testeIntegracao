package br.com.zup.edu.petmanager.controller.request;

import br.com.zup.edu.petmanager.model.Pet;
import br.com.zup.edu.petmanager.model.TipoPet;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

public class PetRequest {
    @NotBlank
    private String nome;

    @NotBlank
    private String raca;

    @NotNull
    private TipoPetRequest tipo;

    @Past //validar se uma data está no passado em relação ao momento atual
    @NotNull
    private LocalDate dataNascimento;

    public PetRequest(String nome, String raca, TipoPetRequest tipo, LocalDate dataNascimento) {
        this.nome = nome;
        this.raca = raca;
        this.tipo = tipo;
        this.dataNascimento = dataNascimento;
    }

    /**
     * @deprecated construtor para uso exclusivo para o Hibernate
     */
    @Deprecated
    public PetRequest() {
    }


    public Pet paraPet(){
        return new Pet(nome,raca, TipoPet.valueOf(tipo.name()),dataNascimento);
    }


    public String getNome() {
        return nome;
    }

    public String getRaca() {
        return raca;
    }

    public TipoPetRequest getTipo() {
        return tipo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
}
