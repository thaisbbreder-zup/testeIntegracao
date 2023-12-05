package br.com.zup.edu.nossozenity.zupper;

import java.util.Locale;

public class KudoResponse {
    private String nome;
    private String enviadoPor;


    public KudoResponse(Kudo kudo) {
        this.nome = kudo.getNome().name().toLowerCase(Locale.ROOT);
        this.enviadoPor=kudo.getEnviado().getNome();
    }

    public KudoResponse() {
    }

    public String getNome() {
        return nome;
    }

    public String getEnviadoPor() {
        return enviadoPor;
    }
}
