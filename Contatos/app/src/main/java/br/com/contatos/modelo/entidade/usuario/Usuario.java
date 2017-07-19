package br.com.contatos.modelo.entidade.usuario;

import java.io.Serializable;

/**
 * Created by JEAN on 21/05/2017.
 */

public class Usuario implements Serializable {

    private Long codigo;

    private String  nome;
    private String  email;
    private String senha;

    private int     sincronizado;

    private transient  int     logado;

    public Usuario(){
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSincronizado(int sincronizado) {
        this.sincronizado = sincronizado;
    }

    public int getSincronizado() {
        return sincronizado;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

    public void setLogado(int logado) {
        this.logado = logado;
    }

    public int getLogado() {
        return logado;
    }

    @Override
    public String toString() {
        return "Usuario - id: " + this.codigo + " - Nome: " + this.nome + " - Email: " + this.email + " - Logado: " + this.logado  ;
    }
}
