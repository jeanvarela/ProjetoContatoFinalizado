package br.com.contatos.modelo.repositorio.usuario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.contatos.infraestrutura.util.Constantes;
import br.com.contatos.modelo.entidade.usuario.Usuario;
import br.com.contatos.modelo.entidade.usuario.UsuarioSQLHelper;
import br.com.contatos.modelo.repositorio.RepositorioBasico;

/**
 * Created by JEAN on 21/05/2017.
 */

public class RepositorioUsuario extends RepositorioBasico implements IRepositorioUsuario {


    public RepositorioUsuario(Context ctx) {
        super(ctx);
    }

    @Override
    public long inserirUsuario(Usuario usuario) {

        long id = getWriteDatabase().insert(UsuarioSQLHelper.TABELA_USUARIO, null, criaContentValues(usuario));
        getWriteDatabase().close();

        return id;
    }

    @Override
    public void registraLogin(long idUsuario, int logado) {

        String sql = "UPDATE " + UsuarioSQLHelper.TABELA_USUARIO + " SET " +
                     UsuarioSQLHelper.COLUNA_LOGADO + " = ? "  + " where " +
                     UsuarioSQLHelper.COLUNA_ID  + " = ? ";
        String[] argumentos = new String[]{String.valueOf(logado),String.valueOf(idUsuario)};

        Cursor cursor = getWriteDatabase().rawQuery(sql,argumentos);
        cursor.moveToFirst();
    }

    @Override
    public Usuario recuperaUsuario(String email, String senha) {

        String sql = "SELECT * FROM " + UsuarioSQLHelper.TABELA_USUARIO +
                     " WHERE  " +  UsuarioSQLHelper.COLUNA_EMAIL + " = ? AND " +
                     UsuarioSQLHelper.COLUNA_SENHA + " = ?";
        String[] argumentos = new String[]{email,senha};

        Cursor cursor = getWriteDatabase().rawQuery(sql,argumentos);

        Usuario usuario = converterCursoUsuario(cursor);

        cursor.close();
        return usuario;
    }

    @Override
    public List<Usuario> recuperaUsuarios() {

        String sql = "SELECT * FROM " + UsuarioSQLHelper.TABELA_USUARIO;

        Cursor cursor = getWriteDatabase().rawQuery(sql,null);

        List<Usuario> usuarios = new ArrayList<Usuario>();

        while (cursor.moveToNext()){
           Usuario usuario = new Usuario();

            usuario.setCodigo(cursor.getLong(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_ID)));
            usuario.setNome(cursor.getString(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_NOME)));
            usuario.setSenha(cursor.getString(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_SENHA)));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_EMAIL)));
            usuario.setLogado(cursor.getInt(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_LOGADO)));
            usuario.setSincronizado(cursor.getInt(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_SINCRONIZADO)));
            usuarios.add(usuario);
        }

        cursor.close();

        return usuarios;
    }

    private ContentValues criaContentValues(Usuario usuario){
        ContentValues cv = new ContentValues();

        cv.put(UsuarioSQLHelper.COLUNA_NOME, usuario.getNome());
        cv.put(UsuarioSQLHelper.COLUNA_SENHA, usuario.getSenha());
        cv.put(UsuarioSQLHelper.COLUNA_ID,usuario.getCodigo());
        cv.put(UsuarioSQLHelper.COLUNA_SINCRONIZADO,usuario.getSincronizado());
        cv.put(UsuarioSQLHelper.COLUNA_EMAIL,usuario.getEmail());
        cv.put(UsuarioSQLHelper.COLUNA_LOGADO, usuario.getLogado());

        return cv;
    }

    public Usuario recuperaUsuarioLogado(){
        String sql = "SELECT * FROM " + UsuarioSQLHelper.TABELA_USUARIO +
                     " WHERE  " +  UsuarioSQLHelper.COLUNA_LOGADO +  " = ?";
        String[] argumentos = new String[]{String.valueOf(Constantes.LOGADO)};

        Cursor cursor = getWriteDatabase().rawQuery(sql,argumentos);

        return converterCursoUsuario(cursor);
    }

    private Usuario converterCursoUsuario(Cursor cursor){
        Usuario usuario = null;

        if (cursor.moveToFirst()){
            usuario = new Usuario();

            usuario.setCodigo(cursor.getLong(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_ID)));
            usuario.setNome(cursor.getString(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_NOME)));
            usuario.setSenha(cursor.getString(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_SENHA)));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_EMAIL)));
            usuario.setLogado(cursor.getInt(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_LOGADO)));
            usuario.setSincronizado(cursor.getInt(cursor.getColumnIndex(UsuarioSQLHelper.COLUNA_SINCRONIZADO)));
        }

        return  usuario;
    }
}
