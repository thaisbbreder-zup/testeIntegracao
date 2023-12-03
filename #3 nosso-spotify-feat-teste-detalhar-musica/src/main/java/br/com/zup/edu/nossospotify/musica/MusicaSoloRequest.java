package br.com.zup.edu.nossospotify.musica;

import javax.validation.constraints.NotBlank;

public class MusicaSoloRequest {
    @NotBlank
    private String nome;

    public MusicaSoloRequest(String nome) {
        this.nome = nome;
    }

    public MusicaSoloRequest() {
    }

    public Musica paraMusica(Artista dono){

        Musica musica = new Musica(nome, dono);

        dono.adicionar(musica);

        return musica;

    }

    public String getNome() {
        return nome;
    }
}
