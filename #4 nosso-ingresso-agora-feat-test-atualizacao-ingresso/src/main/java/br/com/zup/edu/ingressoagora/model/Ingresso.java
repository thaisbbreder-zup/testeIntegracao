package br.com.zup.edu.ingressoagora.model;

import br.com.zup.edu.ingressoagora.exception.DataDeCancelamentoInvalidaException;
import br.com.zup.edu.ingressoagora.exception.IngressoConsumidoException;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import static br.com.zup.edu.ingressoagora.model.EstadoIngresso.*;
import static java.time.LocalDateTime.now;

@Entity
public class Ingresso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoIngresso estado = NAOCONSUMIDO;


    @Column(nullable = false)
    private LocalDateTime compradoEm = now();


    @ManyToOne(optional = false)
    private Evento evento;

    private LocalDateTime canceladoEm;
    private LocalDateTime atualizadoEm;


    public Ingresso(Evento evento) {
        this.evento = evento;
    }

    /**
     * @deprecated construtor para uso exclusivo do Hibernate
     */
    @Deprecated
    public Ingresso() {
    }

    public Long getId() {
        return id;
    }

    public EstadoIngresso getEstado() {
        return estado;
    }

    public boolean isConsumido() {
        return estado.equals(CONSUMIDO);
    }

    public void consumir() {
        this.estado = CONSUMIDO;
        this.atualizadoEm = now();
    }

    public boolean aptoACancelarPorData() {
        int days = Period.between(LocalDate.now(), evento.getData()).getDays();
        return days >= 1;
    }


    public void cancelar() {

        if (isConsumido()) {
            throw new IngressoConsumidoException("Impossivel cancelar um Ingresso já consumido.");
        }

        if (!aptoACancelarPorData()) {
            throw new DataDeCancelamentoInvalidaException("Não é possivel cancelar faltando menos de 1 dia para data do evento");
        }

        this.estado = CANCELADO;
        canceladoEm = now();
    }
}
