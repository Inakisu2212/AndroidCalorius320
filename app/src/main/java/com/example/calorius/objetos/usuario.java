package com.example.calorius.objetos;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class usuario {
    @SerializedName("email")
    @Expose
    private String emailUsuario;
    @SerializedName("password")
    @Expose
    private String passwordUsuario;
    @SerializedName("foto")
    @Expose
    private String fotoUsuario;

    public String getEmailUsuario() {
        return emailUsuario;
    }

    /*public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }*/

    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    /*public void setPasswordUsuario(String passwordUsuario) {
        this.passwordUsuario = passwordUsuario;
    }*/

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    /*public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }*/

}
