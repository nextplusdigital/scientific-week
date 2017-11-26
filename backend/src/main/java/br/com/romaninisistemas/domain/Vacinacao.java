package br.com.romaninisistemas.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Vacinacao.
 */
@Entity
@Table(name = "vacinacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "vacinacao")
public class Vacinacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @ManyToOne
    private Pessoa responsavel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public Vacinacao data(LocalDate data) {
        this.data = data;
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public Vacinacao responsavel(Pessoa pessoa) {
        this.responsavel = pessoa;
        return this;
    }

    public void setResponsavel(Pessoa pessoa) {
        this.responsavel = pessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vacinacao vacinacao = (Vacinacao) o;
        if (vacinacao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vacinacao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vacinacao{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            "}";
    }
}
