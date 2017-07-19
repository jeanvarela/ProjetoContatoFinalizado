package br.com.contatos.infraestrutura.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.contatos.modelo.entidade.contato.ContatoSQLHelper;
import br.com.contatos.modelo.entidade.usuario.UsuarioSQLHelper;

/**
 * Created by JEAN on 02/06/2017.
 */
public class GeraBanco extends SQLiteOpenHelper {

    public GeraBanco(Context context) {
        super(context, Constantes.NOME_BANCO, null, Constantes.VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(UsuarioSQLHelper.geraTabela());
        sqLiteDatabase.execSQL(ContatoSQLHelper.geraTabela());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

