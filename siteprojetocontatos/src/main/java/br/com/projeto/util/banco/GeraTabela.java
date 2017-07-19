package br.com.projeto.util.banco;

import javax.persistence.Persistence;

public class GeraTabela {
	
	public static void main(String[] args) {
		Persistence.createEntityManagerFactory("crudPU");
	}

}
