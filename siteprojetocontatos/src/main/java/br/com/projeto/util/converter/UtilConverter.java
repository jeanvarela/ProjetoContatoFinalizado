package br.com.projeto.util.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.projeto.modelo.entidade.contato.Contato;
import br.com.projeto.modelo.entidade.usuario.Usuario;
import br.com.projeto.webservice.adapter.ContatoAdapter;

public class UtilConverter {

	public static List<Contato> converterParaListaContato(List<ContatoAdapter> contatosAdapter){
		
		List<Contato> contatos = null;
		
		if (contatosAdapter != null){
			contatos = new ArrayList<Contato>();
			
			for(ContatoAdapter contato: contatosAdapter){
				contatos.add(converterParaContato(contato));
			}
		}
		
		return contatos;
	}
	
	public static List<ContatoAdapter> converteParaListaAdapter(List<Contato> contatos){
		List<ContatoAdapter> contatosAdapter = null;
		
		if (contatos != null){
			contatosAdapter = new ArrayList<ContatoAdapter>();
			
			for(Contato contato: contatos){
				contatosAdapter.add(converteParaAdpater(contato));
			}
		}
		
		return contatosAdapter;
	}
	
	public static Contato converterParaContato(ContatoAdapter contatosAdapter){
		Contato contato = new Contato();
		contato.setCodigo(contatosAdapter.getCodigo());
		contato.setNome(contatosAdapter.getNome());
		contato.setLatitude(contatosAdapter.getLatitude());
		contato.setLongitude(contatosAdapter.getLongitude());
		contato.setSincronizado(contatosAdapter.getSincronizado());
		contato.setTelefone(contatosAdapter.getTelefone());
		contato.setDescricaoLocalizacao(contatosAdapter.getDescricaoLocalizacao());
		
		Usuario usuario = new Usuario();
		usuario.setCodigo(contatosAdapter.getIdUsuario());
		contato.setUsuario(usuario);
		
		return contato;
	}
	
	public static ContatoAdapter converteParaAdpater(Contato contato){
		ContatoAdapter adapter = new ContatoAdapter();
		adapter.setCodigo(contato.getCodigo());
		adapter.setNome(contato.getNome());
		adapter.setTelefone(contato.getTelefone());
		adapter.setLatitude(contato.getLatitude());
		adapter.setLongitude(contato.getLongitude());
		adapter.setDescricaoLocalizacao(contato.getDescricaoLocalizacao());
		adapter.setSincronizado(contato.getSincronizado());
		adapter.setIdUsuario(contato.getUsuario().getCodigo());
		
		return adapter;
	}
	
	
}
