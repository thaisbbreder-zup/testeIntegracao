package br.com.zup.edu.nossospotify.musica;

import java.util.List;
import java.util.stream.Collectors;

public class DetalharMusicaResponse {
    private String nome;

    private String dono;

    private List<String> participacoes;

    public DetalharMusicaResponse(Musica musica) {
        this.nome = musica.getNome();
        this.dono = musica.getDono().getNome();
        this.participacoes = musica.getParticipantes()
                .stream()
                .map(Artista::getNome)
                .collect(Collectors.toList());
    }

    public DetalharMusicaResponse() {
    }

    public String getNome() {
        return nome;
    }

    public String getDono() {
        return dono;
    }

    public List<String> getParticipacoes() {
        return participacoes;
    }
}
