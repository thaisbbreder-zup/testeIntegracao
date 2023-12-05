package br.com.zup.edu.nossozenity.zupper;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Zupper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cargo;


    @Column(nullable = false)
    private LocalDate dataAdmissao;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "dono")
    private List<Certificado> certificados= new ArrayList<>();

    @OneToMany(mappedBy = "recebido")
    private List<Kudo> kudosRecebidos= new ArrayList<>();

    @OneToMany(mappedBy = "donoHabilidade")
    private List<Habilidade> habilidades= new ArrayList<>();

    public Zupper(String nome, String cargo, LocalDate dataAdmissao, String email) {
        this.nome = nome;
        this.cargo = cargo;
        this.dataAdmissao = dataAdmissao;
        this.email = email;
    }


    /**
     * @deprecated construtor de uso exclusivo para o hibernate
     */
    @Deprecated
    public Zupper() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
