package br.com.zup.edu.fornecedormanager.model;

import javax.persistence.*;

@Entity
public class Telefone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Fornecedor fornecedor;

    @Column(nullable = false)
    private String ddd;

    @Column(nullable = false)
    private String numero;

    public Telefone(Fornecedor fornecedor, String ddd, String numero) {
        this.fornecedor = fornecedor;
        this.ddd = ddd;
        this.numero = numero;
    }

    /**
     * @deprecated  construtor para uso exclusivo do Hibernate
     */
    @Deprecated
    public Telefone() {
    }

    public Long getId() {
        return id;
    }
}
