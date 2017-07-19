package br.com.contatos.modelo.repositorio.contato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.contatos.infraestrutura.util.Constantes;
import br.com.contatos.modelo.entidade.contato.Contato;
import br.com.contatos.modelo.entidade.contato.ContatoSQLHelper;
import br.com.contatos.modelo.repositorio.RepositorioBasico;

/**
 * Created by JEAN on 02/06/2017.
 */

public class RepositorioContato extends RepositorioBasico implements IRepositorioContato {

    private ContatoSQLHelper helper;

    public RepositorioContato(Context context){
        super(context);
    }



    @Override
    public long adicionarContato(Contato contato) {
        long idContatoRecuperado = new Long(0);

        try {
           idContatoRecuperado = getWriteDatabase().insert(ContatoSQLHelper.TABELA_CONTATO, null, criaContentValues(contato));
        }catch (Exception e){
            Log.i("Erro",e.getMessage());
            Log.i("Erro",e.getLocalizedMessage());
            Log.i("Erro",e.getCause().toString());
            e.printStackTrace();
        }
        return idContatoRecuperado;
    }


    @Override
    public List<Contato> buscarListaContatos(Long idUsuario){
        String sql = "SELECT * FROM " + ContatoSQLHelper.TABELA_CONTATO;
              sql += " WHERE  " +  ContatoSQLHelper.COLUNA_ID_USUARIO + " = ?";
               sql += " ORDER BY " + ContatoSQLHelper.COLUNA_NOME;

        String[] argumento = new String[]{String.valueOf(idUsuario)};
        Cursor cursor = getWriteDatabase().rawQuery(sql,argumento);

        List<Contato> listaContatos = new ArrayList<Contato>();

        while (cursor.moveToNext()){
            listaContatos.add(criaContato(cursor));
        }

        return listaContatos;
    }

    @Override
    public Contato recuperaContato(long idContato){
        String sql = "SELECT * FROM " + ContatoSQLHelper.TABELA_CONTATO;
               sql +=  " WHERE  " +  ContatoSQLHelper.COLUNA_ID + " = ?";

        String[] argumento = new String[]{String.valueOf(idContato)};
        Cursor cursor = getWriteDatabase().rawQuery(sql,argumento);

        Contato contato = null;

        if (cursor.moveToFirst()){
            contato = criaContato(cursor);
        }

        return contato;
    }

    @Override
    public int atualizaContato(Contato contato) {
        return atualizaContatoComChaveModificadaRemotamente(contato,0);
    }

    public int atualizaContatoComChaveModificadaRemotamente(Contato contato, long idAntigoContato){
        ContentValues contentValues = criaContentValues(contato);

        long idContato = contato.getCodigo();

        if (idAntigoContato > 0){
            idContato = idAntigoContato;
        }

        int resgistrosAfetados = getWriteDatabase().update(ContatoSQLHelper.TABELA_CONTATO, contentValues,
                ContatoSQLHelper.COLUNA_ID + " = ?",
                new String[]{String.valueOf(idContato)});

        return  resgistrosAfetados;
    }

    @Override
    public List<Contato> buscarListaContatosNaoSincronizados(Long idUsuario){
        String sql = "SELECT * FROM " + ContatoSQLHelper.TABELA_CONTATO;
        sql += " WHERE  " +  ContatoSQLHelper.COLUNA_ID_USUARIO + " = ? ";
        sql += " AND (" + ContatoSQLHelper.COLUNA_SINCRONIZADO + " = ? ";
        sql += " OR " + ContatoSQLHelper.COLUNA_SINCRONIZADO + " = ?);";

        String[] argumento = new String[]{String.valueOf(idUsuario),String.valueOf(Constantes.NAO_SINCRONIZADO_CRIADO),
                                                                    String.valueOf(Constantes.NAO_SINCRONIZADO_ALTERADO)};
        Cursor cursor = getWriteDatabase().rawQuery(sql,argumento);

        List<Contato> listaContatos = new ArrayList<Contato>();

        while (cursor.moveToNext()){
            listaContatos.add(criaContato(cursor));
        }

        return listaContatos;
    }

    @Override
    public void atualizaStatusSincronizacao(long idContato, int statusSincronizacao) {

        String sql = "UPDATE " + ContatoSQLHelper.TABELA_CONTATO + " SET " +
                      ContatoSQLHelper.COLUNA_SINCRONIZADO + " = ? "  + " where " +
                      ContatoSQLHelper.COLUNA_ID  + " = ? ";
        String[] argumentos = new String[]{String.valueOf(statusSincronizacao),String.valueOf(idContato)};

        Cursor cursor = getWriteDatabase().rawQuery(sql,argumentos);
        cursor.moveToFirst();
    }

    @Override
    public long recuperaIdentificadoNovoContato(long idUsuario) {
        long id = 0;

        String sql = "SELECT max(" + ContatoSQLHelper.COLUNA_ID + ") FROM " + ContatoSQLHelper.TABELA_CONTATO;
        sql += " WHERE  " + ContatoSQLHelper.COLUNA_ID_USUARIO + " = ? ";
        sql += "  ORDER BY " + ContatoSQLHelper.COLUNA_ID;

        String[] argumento = new String[]{String.valueOf(idUsuario)};
        Cursor cursor = getWriteDatabase().rawQuery(sql, argumento);

        if (cursor != null) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }

        return id;
    }


    private ContentValues criaContentValues(Contato contato){
        ContentValues contentValues = new ContentValues();

        contentValues.put(ContatoSQLHelper.COLUNA_ID,contato.getCodigo());
        contentValues.put(ContatoSQLHelper.COLUNA_NOME, contato.getNome());
        contentValues.put(ContatoSQLHelper.COLUNA_TELEFONE,contato.getTelefone());
        contentValues.put(ContatoSQLHelper.COLUNA_LATITUDE,contato.getLatitude());
        contentValues.put(ContatoSQLHelper.COLUNA_LONGITUDE,contato.getLongitude());
        contentValues.put(ContatoSQLHelper.COLUNA_DESCRICAO_LOCALIZACAO,contato.getDescricaoLocalizacao());
        contentValues.put(ContatoSQLHelper.COLUNA_SINCRONIZADO,contato.getSincronizado());
        contentValues.put(ContatoSQLHelper.COLUNA_ID_USUARIO,contato.getIdUsuario());

        return contentValues;
    }

    private Contato criaContato(Cursor cursor){
        Contato contato = new Contato();

        contato.setCodigo(cursor.getLong(cursor.getColumnIndex(ContatoSQLHelper.COLUNA_ID)));
        contato.setNome(cursor.getString(cursor.getColumnIndex(ContatoSQLHelper.COLUNA_NOME)));
        contato.setTelefone(cursor.getString(cursor.getColumnIndex(ContatoSQLHelper.COLUNA_TELEFONE)));
        contato.setLatitude(cursor.getDouble(cursor.getColumnIndex(ContatoSQLHelper.COLUNA_LATITUDE)));
        contato.setLongitude(cursor.getDouble(cursor.getColumnIndex(ContatoSQLHelper.COLUNA_LONGITUDE)));
        contato.setDescricaoLocalizacao(cursor.getString(cursor.getColumnIndex(ContatoSQLHelper.COLUNA_DESCRICAO_LOCALIZACAO)));
        contato.setSincronizado(cursor.getInt(cursor.getColumnIndex(ContatoSQLHelper.COLUNA_SINCRONIZADO)));
        contato.setIdUsuario(cursor.getLong(cursor.getColumnIndex(ContatoSQLHelper.COLUNA_ID_USUARIO)));

        return contato;
    }
}
