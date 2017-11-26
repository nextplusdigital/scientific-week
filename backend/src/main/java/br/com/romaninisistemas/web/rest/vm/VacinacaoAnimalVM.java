package br.com.romaninisistemas.web.rest.vm;

import br.com.romaninisistemas.domain.Animal;
import br.com.romaninisistemas.domain.Remedio;
import br.com.romaninisistemas.domain.Vacinacao;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * View Model object for storing a user's credentials.
 */
public class VacinacaoAnimalVM {

    private Long id;

    private Integer identificacao;

    private Integer quantidade;

    @NotNull
    private String descricaoAnimal;

    @NotNull
    private String descricaoVacinacao;

    @NotNull
    private String nomeRemedio;

    public VacinacaoAnimalVM() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(Integer identificacao) {
        this.identificacao = identificacao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricaoAnimal() {
        return descricaoAnimal;
    }

    public void setDescricaoAnimal(String descricaoAnimal) {
        this.descricaoAnimal = descricaoAnimal;
    }

    public String getDescricaoVacinacao() {
        return descricaoVacinacao;
    }

    public void setDescricaoVacinacao(String descricaoVacinacao) {
        this.descricaoVacinacao = descricaoVacinacao;
    }

    public String getNomeRemedio() {
        return nomeRemedio;
    }

    public void setNomeRemedio(String nomeRemedio) {
        this.nomeRemedio = nomeRemedio;
    }

    @Override
    public String toString() {
        return "VacinacaoAnimalVM{" +
            "id=" + id +
            ", identificacao=" + identificacao +
            ", quantidade=" + quantidade +
            ", descricaoAnimal='" + descricaoAnimal + '\'' +
            ", descricaoVacinacao='" + descricaoVacinacao + '\'' +
            ", nomeRemedio='" + nomeRemedio + '\'' +
            '}';
    }
}
