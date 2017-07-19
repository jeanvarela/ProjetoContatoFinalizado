package br.com.contatos.infraestrutura.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JEAN on 21/05/2017.
 *
 * Função: essa classe tem como objetivo fornecer métodos utilitários para validação
 */

public class UtilValidacao {

    /**
     * Created by JEAN on 21/05/2017.
     * <p>
     * Função: verificar se um texto/string foi preenchido
     *
     * @param texto: o contéudo que se deseja verificar
     * @return Retorna 'true' se o texto foi preenchido, caso contrário retorna 'false'.
     */
    public static boolean verificaCampoTextoNaoVazio(String texto) {
        if (texto == null || texto.trim().equals("") || texto.equals(null) || texto.equals("null")) {
            return false;
        }

        return true;
    }

    /**
     * Created by JEAN on 21/05/2017.
     * <p>
     * Função: verificar se o email é válido
     *
     * @param email: o email que se deseja validar
     * @return Retorna 'true' se o email é válido, caso contrário retorna 'false'.
     */
    public static boolean validaEmail(String email) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
                return true;
        }

        return false;
    }


    /**
     * Created by JEAN on 21/05/2017.
     * <p>
     * Função: verificar se o número de telefone é válido
     *
     * @param telefone: o número de telefone que se deseja validar
     * @return Retorna 'true' se o número de telefone é válido, caso contrário retorna 'false'.
     */
    public static boolean validaTelefone(String telefone) {

        String telefoneSemCaracteresEspeciais = telefone.trim().replaceAll("[(]", "").replaceAll("[)]", "")
                                                               .replaceAll("[-]", "");

        if (telefoneSemCaracteresEspeciais.length() == Constantes.QUANTIDADE_DIGITOS_TELEFONE) {
            return true;
        }

        return false;
    }

    public  static boolean verificaStringIguais(String valor1, String valor2){
        return valor1 != null && valor2 != null && valor1.contentEquals(valor2);
    }

}