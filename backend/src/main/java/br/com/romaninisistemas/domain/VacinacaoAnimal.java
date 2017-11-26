package br.com.romaninisistemas.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A VacinacaoAnimal.
 */
@Entity
@Table(name = "vacinacao_animal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "vacinacaoanimal")
public class VacinacaoAnimal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;


    @Column(name = "identificacao", nullable = false)
    private Integer identificacao;


    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @ManyToOne
    private Animal animal;

    @ManyToOne
    private Vacinacao vacinacao;

    @ManyToOne
    private Remedio remedio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdentificacao() {
        return identificacao;
    }

    public VacinacaoAnimal identificacao(Integer identificacao) {
        this.identificacao = identificacao;
        return this;
    }

    public void setIdentificacao(Integer identificacao) {
        this.identificacao = identificacao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public VacinacaoAnimal quantidade(Integer quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Animal getAnimal() {
        return animal;
    }

    public VacinacaoAnimal animal(Animal animal) {
        this.animal = animal;
        return this;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Vacinacao getVacinacao() {
        return vacinacao;
    }

    public VacinacaoAnimal vacinacao(Vacinacao vacinacao) {
        this.vacinacao = vacinacao;
        return this;
    }

    public void setVacinacao(Vacinacao vacinacao) {
        this.vacinacao = vacinacao;
    }

    public Remedio getRemedio() {
        return remedio;
    }

    public VacinacaoAnimal remedio(Remedio remedio) {
        this.remedio = remedio;
        return this;
    }

    public void setRemedio(Remedio remedio) {
        this.remedio = remedio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VacinacaoAnimal vacinacaoAnimal = (VacinacaoAnimal) o;
        if (vacinacaoAnimal.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vacinacaoAnimal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VacinacaoAnimal{" +
            "id=" + getId() +
            ", identificacao='" + getIdentificacao() + "'" +
            ", quantidade='" + getQuantidade() + "'" +
            "}";
    }
}
