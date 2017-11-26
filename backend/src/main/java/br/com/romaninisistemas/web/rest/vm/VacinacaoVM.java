package br.com.romaninisistemas.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * View Model object for storing a user's credentials.
 */
public class VacinacaoVM {

    private Long id;

    @NotNull
    private Date date;

    @NotNull
    private String nome;

    @NotNull
    private String login;

    public VacinacaoVM() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "VacinacaoVM{" +
            "id=" + id +
            ", date=" + date +
            ", nome='" + nome + '\'' +
            ", login='" + login + '\'' +
            '}';
    }
}
