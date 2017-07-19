package br.com.projeto.util.dao;

import java.util.List;

import br.com.projeto.modelo.entidade.GenericDomain;

public interface IGenericDAO<T> {
	
	public GenericDomain salvar(GenericDomain entidade);
	public List<T> listar(String... propriedades);
	public T buscar(Long id);
	public void excluir(T entidade);		
	public GenericDomain editar(GenericDomain entidade);
}