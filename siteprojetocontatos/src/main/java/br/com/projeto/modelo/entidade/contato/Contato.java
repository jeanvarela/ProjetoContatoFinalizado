package br.com.projeto.modelo.entidade.contato;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.projeto.modelo.entidade.GenericDomain;
import br.com.projeto.modelo.entidade.usuario.Usuario;

@Entity
@Table(name = "contato")
public class Contato extends GenericDomain{

	private static final long serialVersionUID = 1L;
	
	@Column(length = 100, nullable = false)
	private String nome;
	
	@Column(length = 20, nullable = false)
	private String telefone;

	@ManyToOne
	private Usuario usuario;
	
	@Column(nullable = false)
    private Double    latitude;
    
	@Column(nullable = false)
    private Double    longitude;   

	@Column(length = 100, nullable = false)
    private String  descricaoLocalizacao;

	@Column(nullable = false)
    private int     sincronizado;	
	
	

	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getDescricaoLocalizacao() {
		return descricaoLocalizacao;
	}

	public void setDescricaoLocalizacao(String descricaoLocalizacao) {
		this.descricaoLocalizacao = descricaoLocalizacao;
	}

	public int getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(int sincronizado) {
		this.sincronizado = sincronizado;
	}
}
