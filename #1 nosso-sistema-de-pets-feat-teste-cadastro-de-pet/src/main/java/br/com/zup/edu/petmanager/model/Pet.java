package br.com.zup.edu.petmanager.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPet tipo;


    @Column(nullable = false)
    private LocalDate dataNascimento;

    public Pet(String nome, String raca, TipoPet tipo, LocalDate dataNascimento) {
        this.nome = nome;
        this.raca = raca;
        this.tipo = tipo;
        this.dataNascimento = dataNascimento;
    }

    /**
     * @deprecated contrutor para uso exclusivo do hibernate
     */
    @Deprecated
    public Pet() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRaca() {
        return raca;
    }

    public TipoPet getTipo() {
        return tipo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
}
