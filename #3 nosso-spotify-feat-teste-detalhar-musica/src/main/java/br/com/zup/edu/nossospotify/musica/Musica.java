package br.com.zup.edu.nossospotify.musica;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Musica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne(optional = false)
    private Artista dono;

    @JoinTable(
            name = "participante_musica",
            joinColumns = @JoinColumn(name = "musica_id"),
            inverseJoinColumns = @JoinColumn(name = "artista_id")
    )
    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<Artista> participantes = new LinkedHashSet<>();

    @ManyToOne
    private Album album;

    public Musica(String nome, Artista dono) {
        this.nome = nome;
        this.dono = dono;
    }

    /**
     * @deprecated construtor para uso exclusivo do Hibernate
     */
    @Deprecated
    public Musica() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Artista getDono() {
        return dono;
    }

    public Set<Artista> getParticipantes() {
        return participantes;
    }

    public void adicionar(Set<Artista> artistasParticipantes) {

        this.participantes.addAll(artistasParticipantes);

        artistasParticipantes.forEach(artista -> artista.participou(this));
    }

    public void adicionar(Album album) {
        this.album = album;
    }
}
