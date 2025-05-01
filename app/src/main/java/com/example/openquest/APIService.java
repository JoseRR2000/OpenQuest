package com.example.openquest;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("obtener_preguntas.php")
    Call<List<Pregunta>> obtenerPreguntas();
}
