package br.com.contatos.modelo.entidade.contato;


import br.com.contatos.modelo.entidade.usuario.UsuarioSQLHelper;

/**
 * Created by JEAN on 02/06/2017.
 */

public class ContatoSQLHelper  {

    public static final String TABELA_CONTATO = "contato";

    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_NOME = "nome";
    public static final String COLUNA_TELEFONE = "telefone";
    public static final String COLUNA_SINCRONIZADO = "sincronizado";
    public static final String COLUNA_LATITUDE = "latitude";
    public static final String COLUNA_LONGITUDE = "longitude";
    public static final String COLUNA_DESCRICAO_LOCALIZACAO  = "descricaoLocalizacao";
    public static final String COLUNA_ID_USUARIO  = "idUsuario";


    public static String geraTabela(){
        String script = "CREATE TABLE "+ TABELA_CONTATO +" (" +
                                         COLUNA_ID +" INTEGER,"+
                                         COLUNA_NOME     +" TEXT NOT NULL, "+
                                         COLUNA_TELEFONE     +" TEXT NOT NULL, "+
                                         COLUNA_DESCRICAO_LOCALIZACAO +" TEXT NOT NULL, " +
                                         COLUNA_SINCRONIZADO + " INTEGER NOT NULL," +
                                         COLUNA_LATITUDE + " DOUBLE PRECISION NOT NULL," +
                                         COLUNA_LONGITUDE  + " DOUBLE PRECISION NOT NULL, " +
                                         COLUNA_ID_USUARIO  + " INTEGER NOT NULL," +
                                         "PRIMARY KEY (" + COLUNA_ID+ ")" +
                                         "FOREIGN KEY(" + COLUNA_ID_USUARIO + ") REFERENCES " +
                                         UsuarioSQLHelper.TABELA_USUARIO + " (" + UsuarioSQLHelper.COLUNA_ID  + "));";
       return script;
    }

}
