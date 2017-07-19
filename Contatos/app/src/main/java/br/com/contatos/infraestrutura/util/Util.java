package br.com.contatos.infraestrutura.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by JEAN on 21/05/2017.
 *
 * Função: Essa classe foi criada para concentrar métodos utilitários.
 */

public class Util {

    public static String formataEndereco(Address endereco){
        StringBuffer enderecoFormatado = new StringBuffer();

        if (endereco != null){

            if (UtilValidacao.verificaCampoTextoNaoVazio(endereco.getAddressLine(0)) &&
                !endereco.getAddressLine(0).equals("Unnamed")){
                enderecoFormatado.append(" ");
                enderecoFormatado.append(endereco.getAddressLine(0));
            }

            if (UtilValidacao.verificaCampoTextoNaoVazio(endereco.getAddressLine(1))){
                enderecoFormatado.append(" ");
                enderecoFormatado.append(endereco.getAddressLine(1));
            }

            if (UtilValidacao.verificaCampoTextoNaoVazio(endereco.getAddressLine(2))){
                enderecoFormatado.append(" ");
                enderecoFormatado.append(endereco.getAddressLine(2));
            }

        }

        return enderecoFormatado.toString();
    }

    public static boolean verificaConectado(Context contexto){
        ConnectivityManager manager = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (activeNetwork != null)
            return true;

        return false;
    }

    public  static SharedPreferences.Editor rescuperaSharedPreferencesEditor(Context contexto){
        SharedPreferences preferencias = contexto.getSharedPreferences(Constantes.NOME_SHARED_PREFERENCES,MODE_PRIVATE);
        return preferencias.edit();
    }

    public  static SharedPreferences rescuperaSharedPreferences(Context contexto) {
        return contexto.getSharedPreferences(Constantes.NOME_SHARED_PREFERENCES, MODE_PRIVATE);
    }

    public  static boolean isPhone(String stringBusca){
        if (stringBusca != null && stringBusca.length() > 0 &&
            Character.isDigit(Character.valueOf(stringBusca.charAt(0)))){
            return true;
        }

        return false;
    }


}
