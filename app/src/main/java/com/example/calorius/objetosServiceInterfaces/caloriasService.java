package com.example.calorius.objetosServiceInterfaces;

import com.example.calorius.objetos.calorias;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface caloriasService {
    @GET("api/calorias")
    Call<List<calorias>> getCalorias(); //Este metodo no se implementa, al parecer

    @POST("api/calorias")
    Call<calorias> registrarCalorias(@Body calorias caloria); //Este método ".
}
