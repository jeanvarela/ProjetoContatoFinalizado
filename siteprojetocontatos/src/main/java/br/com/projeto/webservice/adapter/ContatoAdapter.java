package br.com.projeto.webservice.adapter;

import java.io.Serializable;

public class ContatoAdapter implements Serializable {

	private static final long serialVersionUID = 1L;

    private Long      codigo;
    private Long      idUsuario;  
    
    private Double    latitude;
    private Double    longitude;
      
    private String    nome;
    private String    telefone;
    private String    descricaoLocalizacao;
    
    private int       sincronizado;
    
    public Long getCodigo() {
		return codigo;
	}
	
    public void setCodigo(Long codigo) {
		this.codigo = codigo;
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
	
    public Long getIdUsuario() {
		return idUsuario;
	}
	
    public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	
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