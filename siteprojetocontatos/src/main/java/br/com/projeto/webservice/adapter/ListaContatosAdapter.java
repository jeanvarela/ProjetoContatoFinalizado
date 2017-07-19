package br.com.projeto.webservice.adapter;

import java.util.List;

import br.com.projeto.modelo.entidade.contato.Contato;

public class ListaContatosAdapter {

	private List<Contato> contatos;
	
	
	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	public List<Contato> getContatos() {
		// TODO Auto-generated method stub
		return contatos;
	}
}
