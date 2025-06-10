package com.example.openquest;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @GET("obtener_preguntas.php")
    Call<List<Pregunta>> obtenerPreguntas();

    @GET("preguntas_por_categoria.php")
    Call<List<Pregunta>> obtenerPreguntasPorCategoria(@Query("categoria") int categoria);

    @GET("obtener_partidas.php")
    Call<List<Partida>> obtenerPartidas();

    @GET("obtener_logros.php")
    Call<List<Logro>> obtenerLogros();

    @POST("almacen_partidas.php") // El nombre de tu script PHP para guardar partidas
    Call<ApiResponse> guardarPartida(@Body Partida partida);

    @FormUrlEncoded
    @POST("registrar_usuario.php") Call<RegistrationResponse> registerUser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login_usuario.php") Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

}
