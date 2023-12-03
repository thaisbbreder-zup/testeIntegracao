package br.com.zup.edu.nossospotify.musica;

import javax.validation.constraints.NotBlank;

public class AlbumRequest {
    @NotBlank
    private String nome;

    public AlbumRequest(String nome) {
        this.nome = nome;
    }

    public Album paraAlbum(Artista dono){
        return new Album(nome,dono);
    }

    public AlbumRequest() {
    }

    public String getNome() {
        return nome;
    }
}
