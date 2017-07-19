package br.com.projeto.util.enumeracao;

public enum EstadoSincronizacao {

    SINCRONIZADO(1),
    NAO_SINCRONIZADO(0);

    private int valor;

    private EstadoSincronizacao(int descricao) {
        this.valor = descricao;
    }

    public int getValor() {
        return valor;
    }
}
