package br.com.romaninisistemas.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
public class PessoaVM {

    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String descricao;

    public PessoaVM() {
    }

    public PessoaVM(String descricao) {
        this.descricao = descricao;
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

    @Override
    public String toString() {
        return "PessoaVM{" +
            "id=" + id +
            ", descricao='" + descricao + '\'' +
            '}';
    }
}
