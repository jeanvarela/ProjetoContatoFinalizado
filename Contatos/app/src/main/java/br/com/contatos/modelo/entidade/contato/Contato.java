package br.com.contatos.modelo.entidade.contato;

import java.io.Serializable;

/**
 * Created by JEAN on 02/06/2017.
 */

public class Contato implements Serializable {

    private Long codigo;

    private double   latitude;
    private double   longitude;
    private String   nome;
    private String   telefone;
    private String   descricaoLocalizacao;
    private int      sincronizado;
    private Long     idUsuario;

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Nome: " + this.nome + " - " +
               "Codigo: " + this.codigo + " - " +
               "Latitude: " + this.latitude + " - " +
               "Longitude: " + this.longitude + " - " +
               "Sincronizado: " + this.sincronizado;
    }
}
