package br.com.contatos.modelo.entidade.usuario;

/**
 * Created by JEAN on 21/05/2017.
 */

public class UsuarioSQLHelper  {

    public static final String TABELA_USUARIO = "usuario";
    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_NOME = "nome";
    public static final String COLUNA_SENHA = "senha";
    public static final String COLUNA_EMAIL = "email";
    public static final String COLUNA_SINCRONIZADO = "sincronizado";
    public static final String COLUNA_LOGADO = "logado";

    public static String geraTabela() {
        String script =  "CREATE TABLE "+ TABELA_USUARIO +" (" +
                                          COLUNA_ID +" INTEGER PRIMARY KEY,"+
                                          COLUNA_NOME     +" TEXT NOT NULL, "+
                                          COLUNA_EMAIL     +" TEXT NOT NULL, "+
                                          COLUNA_SENHA +" TEXT NOT NULL, " +
                                          COLUNA_SINCRONIZADO + " INTEGER NOT NULL," +
                                          COLUNA_LOGADO + " INTEGER NOT NULL)";

        return script;
    }
}
