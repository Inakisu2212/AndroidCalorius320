package com.example.calorius.objetosServiceInterfaces;

import com.example.calorius.objetos.usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface usuarioService {
    @GET("api/usuarios")
    Call<List<usuario>> getUsuarios(); //Más de lo mismo, no se implementa al parecer.
}
