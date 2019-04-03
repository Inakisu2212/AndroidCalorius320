package com.example.calorius.objetosServiceInterfaces;

import com.example.calorius.objetos.usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface usuarioService {
    @GET("api/usuarios")
    Call<List<usuario>> getUsuarios(); //Más de lo mismo, no se implementa al parecer.

    @GET("api/usuarios/obtenerUsuario/{email}/{password}")
    Call<usuario> getObtenerUsuario(@Path("email") String email, @Path("password") String password);
}
