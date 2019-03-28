package com.example.calorius.objetosServiceInterfaces;

import com.example.calorius.objetos.usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface usuarioService {
    @GET("api/usuarios")
    Call<List<usuario>> getUsuarios(); //Más de lo mismo, no se implementa al parecer.

    @GET("api/usuarios/{email}")
    Call<usuario> getUsuario(@Path("email") String email);
}
