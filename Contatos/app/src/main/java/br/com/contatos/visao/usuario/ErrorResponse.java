package br.com.contatos.visao.usuario;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JEAN on 25/06/2017.
 */

public class ErrorResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private Error error;

    public String getMessage() {
        return message;
    }

    public Error getError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(Error error) {
        this.error = error;
    }
}