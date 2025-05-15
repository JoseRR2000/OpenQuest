package com.example.openquest;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("obtener_preguntas.php")
    Call<List<Pregunta>> obtenerPreguntas();

    @GET("preguntas_por_categoria.php")
    Call<List<Pregunta>> obtenerPreguntasPorCategoria(@Query("categoria") String categoria);

    @GET("obtener_partidas.php")
    Call<List<Partida>> obtenerPartidas();

    @GET("obtener_logros.php")
    Call<List<Logro>> obtenerLogros();
}
