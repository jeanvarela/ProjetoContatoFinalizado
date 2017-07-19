package br.com.projeto.modelo.entidade.usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.projeto.modelo.entidade.GenericDomain;

@Entity
@Table(name = "usuario")
public class Usuario extends GenericDomain{

	private static final long serialVersionUID = 1L;
	
	@Column(length = 100, nullable = false)
	private String nome;
	
	@Column(length = 50, nullable = false)
	private String email;
	
	@Column(length = 100, nullable = false)
	private String senha;

	@Column(nullable = false)
    private int     sincronizado;
		
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
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}	
	
	public void setSincronizado(int sincronizado) {
		this.sincronizado = sincronizado;
	}
	
	public int getSincronizado() {
		return sincronizado;
	}
}
