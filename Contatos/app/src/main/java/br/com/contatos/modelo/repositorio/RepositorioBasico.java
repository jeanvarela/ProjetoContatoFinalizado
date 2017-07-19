package br.com.contatos.modelo.repositorio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.contatos.infraestrutura.util.GeraBanco;

/**
 * Created by JEAN on 02/06/2017.
 */

public class RepositorioBasico {

    private SQLiteDatabase writeDatabase;
    private GeraBanco helper;

    public  RepositorioBasico(Context contexto){
        if (helper == null) {
            helper = new GeraBanco(contexto);
            writeDatabase = helper.getWritableDatabase();
        }
    }

    public SQLiteDatabase getWriteDatabase() {
        return writeDatabase;
    }
}
