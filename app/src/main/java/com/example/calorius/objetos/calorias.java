package com.example.calorius.objetos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class calorias {
    @SerializedName("email")
    @Expose
    private String emailCaloria;
    @SerializedName("fecha")
    @Expose
    private String fechaCaloria;
    @SerializedName("tipocomida")
    @Expose
    private String tipocomidaCaloria;
    @SerializedName("codigoalimento")
    @Expose
    private Integer codigoAlimentoCaloria;
    @SerializedName("cantidad")
    @Expose
    private Integer cantidadCaloria;

    public String getEmailCaloria() {
        return emailCaloria;
    }

    public void setEmailCaloria(String emailCaloria) {
        this.emailCaloria = emailCaloria;
    }

    public String getFechaCaloria() {
        return fechaCaloria;
    }

    public void setFechaCaloria (String fechaCaloria) {
        this.fechaCaloria = fechaCaloria;
    }

    public String getTipocomidaCaloria() {
        return tipocomidaCaloria;
    }

    public void setTipocomidaCaloria(String tipocomidaCaloria) {
        this.tipocomidaCaloria = tipocomidaCaloria;
    }

    public Integer getCodigoAlimentoCaloria() {
        return codigoAlimentoCaloria;
    }

    public void setCodigoAlimentoCaloria(Integer codigoAlimentoCaloria) {
        this.codigoAlimentoCaloria = codigoAlimentoCaloria;
    }

    public Integer getCantidadCaloria() {
        return cantidadCaloria;
    }

    public void setCantidadCaloria(Integer cantidadCaloria) {
        this.cantidadCaloria = cantidadCaloria;
    }
}
