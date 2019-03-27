package com.example.calorius.objetos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class alimentos {
    @SerializedName("codigo")
    @Expose
    private Integer codigoAlimento;
    @SerializedName("nombre")
    @Expose
    private String nombreAlimento;
    @SerializedName("calorias")
    @Expose
    private Integer caloriasAlimento;

    public Integer getCodigoAlimento() {
        return codigoAlimento;
    }

    /*public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }*/

    public String getNombreAlimento() {
        return nombreAlimento;
    }

    /*public void setPasswordUsuario(String passwordUsuario) {
        this.passwordUsuario = passwordUsuario;
    }*/

    public Integer getCaloriasAlimento() {
        return caloriasAlimento;
    }

    /*public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }*/
}
