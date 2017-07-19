package br.com.contatos.infraestrutura.enumerado;

/**
 * Created by JEAN on 05/06/2017.
 */

public enum OpcoeAcoes {

    ACABOU_INSERIR_OU_ATUALIZAR(1);

    public int valor;

    OpcoeAcoes(int valor) {
        valor = valor;
    }

    public int getOpcao(){
        return valor;
    }
}
