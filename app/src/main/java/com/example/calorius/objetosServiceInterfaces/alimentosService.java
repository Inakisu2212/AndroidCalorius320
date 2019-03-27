package com.example.calorius.objetosServiceInterfaces;

import com.example.calorius.objetos.alimentos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface alimentosService {
    @GET("api/alimentos")
    Call<List<alimentos>> getAlimentos();
}
