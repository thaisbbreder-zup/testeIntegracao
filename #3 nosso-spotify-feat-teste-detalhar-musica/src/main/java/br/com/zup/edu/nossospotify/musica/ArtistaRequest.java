package br.com.zup.edu.nossospotify.musica;

import javax.validation.constraints.NotBlank;

public class ArtistaRequest {
    @NotBlank
    private String nome;

    @NotBlank
    private String cidade;

    @NotBlank
    private String estado;

    public ArtistaRequest(String nome, String cidade, String estado) {
        this.nome = nome;
        this.cidade = cidade;
        this.estado = estado;
    }

    public ArtistaRequest() {
    }


    public Artista paraArtista(){
        return new Artista(nome,cidade,estado);
    }

    public String getNome() {
        return nome;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }
}
