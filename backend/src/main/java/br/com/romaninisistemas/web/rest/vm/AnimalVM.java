package br.com.romaninisistemas.web.rest.vm;

import br.com.romaninisistemas.config.Constants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
public class AnimalVM {

    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String descricao;

    @NotNull
    private String tipoAnimal;

    public AnimalVM() {
    }

    public AnimalVM(String descricao, String tipoAnimal) {
        this.descricao = descricao;
        this.tipoAnimal = tipoAnimal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(String tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }


    @Override
    public String toString() {
        return "AnimalVM{" +
            "descricao='" + descricao + '\'' +
            ", tipoAnimal='" + tipoAnimal + '\'' +
            '}';
    }
}
