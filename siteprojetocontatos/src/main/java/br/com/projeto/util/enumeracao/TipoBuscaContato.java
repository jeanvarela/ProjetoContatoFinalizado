package br.com.projeto.util.enumeracao;

public enum TipoBuscaContato {

    PESQUISA_NOME(1),
    PESQUISA_CELULAR(2);

    private int valor;

    private TipoBuscaContato(int descricao) {
        this.valor = descricao;
    }

    public int getValor() {
        return valor;
    }
}
