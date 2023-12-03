package br.com.zup.edu.nossospotify.musica;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;

    @ManyToMany(mappedBy = "participantes")
    private Set<Musica> participacoes = new HashSet<>();

    @OneToMany(mappedBy ="dono")
    private Set<Album> albuns = new HashSet<>();

    @OneToMany(mappedBy ="dono")
    private Set<Musica> musicas = new HashSet<>();


    public Artista(String nome, String cidade, String estado) {
        this.nome = nome;
        this.cidade = cidade;
        this.estado = estado;
    }

    /**
     * @deprecated construtor para uso exclusivo do Hibernate
     */
    @Deprecated
    public Artista() {
    }

    public Long getId() {
        return this.id;
    }

    public String getNome() {
        return nome;
    }

    public void participou(Musica musica) {
        this.participacoes.add(musica);
    }

    public void adicionar(Musica musica) {
        this.musicas.add(musica);
    }

    public void adicionar(Album album){
        this.albuns.add(album);
    }

}
