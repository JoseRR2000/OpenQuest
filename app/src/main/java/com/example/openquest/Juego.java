package com.example.openquest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Juego extends AppCompatActivity {
    private CountDownTimer contador;
    private TextView tiempo;
    private long tiempoRestante;
    private int tiempoBase;
    private int rondas;
    private int puntuacion = 0;

    private String dificultad;
    private TextView textoPuntuacion;
    private TextView textoPuntos;
    private List<Pregunta> listaPreguntas;
    private int indicePregunta;
    private TextView textoPregunta;
    private TextView respuesta1;
    private TextView respuesta2;
    private TextView respuesta3;
    private TextView respuesta4;
    private LinearLayout cajaRespuesta1;
    private LinearLayout cajaRespuesta2;
    private LinearLayout cajaRespuesta3;
    private LinearLayout cajaRespuesta4;
    private Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juego);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        indicePregunta = 0;

        dificultadRecibida();
        obtenerPreguntas();
        seleccionRespuestas();
    }
    private void dificultadRecibida() {
        dificultad = getIntent().getStringExtra("dificultad");

        if (dificultad == null) {
            dificultad = "fácil";
        }

        switch (dificultad) {
            case "Fácil":
                tiempoBase = 30000;
                rondas = 15;
                break;
            case "Normal":
                tiempoBase = 20000;
                rondas = 10;
                break;
            case "Difícil":
                tiempoBase = 10000;
                rondas = 5;
                break;
            default:
                tiempoBase = 30000;
                rondas = 15;
        }
    }

    private void iniciarTemporizador() {
        if (contador != null) {
            contador.cancel();
        }

        tiempo = findViewById(R.id.texto_tiempo);

        contador = new CountDownTimer(tiempoBase, 1000) {
            public void onTick(long millisUntilFinished) {
                tiempo.setText(String.valueOf(millisUntilFinished / 1000));
                tiempoRestante = millisUntilFinished;
            }

            public void onFinish() {
                tiempo.setText("0");
                Toast.makeText(Juego.this, "¡Tiempo agotado!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void obtenerPreguntas() {
        String modoJuego = getIntent().getStringExtra("modo");

        if ("todas".equals(modoJuego)) {
            obtenerTodasLasPreguntas();
        }
        else if ("categoria".equals(modoJuego)){
            int categoria = getIntent().getIntExtra("categoria", -1);
            obtenerPreguntasPorCategoria(categoria);
        }
    }

    private void obtenerTodasLasPreguntas() {
        Retrofit retro = new Retrofit();
        retro.getApi().obtenerPreguntas().enqueue(new Callback<List<Pregunta>>() {
            @Override
            public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPreguntas = response.body();
                    Collections.shuffle(listaPreguntas);
                    mostrarSiguientePregunta();
                }
            }

            @Override
            public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                Toast.makeText(Juego.this, "Error al obtener preguntas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerPreguntasPorCategoria(int categoria) {
        Retrofit retro = new Retrofit();
        retro.getApi().obtenerPreguntasPorCategoria(categoria).enqueue(new Callback<List<Pregunta>>() {
            @Override
            public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPreguntas = response.body();
                    Collections.shuffle(listaPreguntas);
                    mostrarSiguientePregunta();
                }
            }

            @Override
            public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                Toast.makeText(Juego.this, "Error al obtener preguntas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarSiguientePregunta() {
        textoPregunta = findViewById(R.id.texto_pregunta);
        respuesta1 = findViewById(R.id.texto_respuesta_1);
        respuesta2 = findViewById(R.id.texto_respuesta_2);
        respuesta3 = findViewById(R.id.texto_respuesta_3);
        respuesta4 = findViewById(R.id.texto_respuesta_4);
        cajaRespuesta1 = findViewById(R.id.cajaRespuesta1);
        cajaRespuesta2 = findViewById(R.id.cajaRespuesta2);
        cajaRespuesta3 = findViewById(R.id.cajaRespuesta3);
        cajaRespuesta4 = findViewById(R.id.cajaRespuesta4);

        cajaRespuesta1.setClickable(true);
        cajaRespuesta2.setClickable(true);
        cajaRespuesta3.setClickable(true);
        cajaRespuesta4.setClickable(true);

        cajaRespuesta1.setBackgroundColor(Color.WHITE);
        cajaRespuesta2.setBackgroundColor(Color.WHITE);
        cajaRespuesta3.setBackgroundColor(Color.WHITE);
        cajaRespuesta4.setBackgroundColor(Color.WHITE);

        if (indicePregunta >= rondas || indicePregunta >= listaPreguntas.size()) {
            // Finalizar partida o ir a pantalla de resultados
            Toast.makeText(this, "¡Partida finalizada!", Toast.LENGTH_LONG).show();
            finalizarJuego();
            finish();
        }
        else {
            Pregunta pregunta = listaPreguntas.get(indicePregunta);

            textoPregunta.setText(pregunta.getPregunta());

            List<String> respuestas = Arrays.asList(
                    pregunta.getRespuesta1(),
                    pregunta.getRespuesta2(),
                    pregunta.getRespuesta3(),
                    pregunta.getRespuesta4()
            );

            Collections.shuffle(respuestas);

            respuesta1.setText(respuestas.get(0));
            respuesta2.setText(respuestas.get(1));
            respuesta3.setText(respuestas.get(2));
            respuesta4.setText(respuestas.get(3));

            iniciarTemporizador();
        }
    }

    private void seleccionRespuestas() {
        respuesta1 = findViewById(R.id.texto_respuesta_1);
        respuesta2 = findViewById(R.id.texto_respuesta_2);
        respuesta3 = findViewById(R.id.texto_respuesta_3);
        respuesta4 = findViewById(R.id.texto_respuesta_4);
        cajaRespuesta1 = findViewById(R.id.cajaRespuesta1);
        cajaRespuesta2 = findViewById(R.id.cajaRespuesta2);
        cajaRespuesta3 = findViewById(R.id.cajaRespuesta3);
        cajaRespuesta4 = findViewById(R.id.cajaRespuesta4);

        cajaRespuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarRespuesta(respuesta1, cajaRespuesta1, cajaRespuesta2, cajaRespuesta3, cajaRespuesta4);
            }
        });

        cajaRespuesta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarRespuesta(respuesta2, cajaRespuesta2, cajaRespuesta1, cajaRespuesta3, cajaRespuesta4);
            }
        });

        cajaRespuesta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarRespuesta(respuesta3, cajaRespuesta3, cajaRespuesta1, cajaRespuesta2, cajaRespuesta4);
            }
        });

        cajaRespuesta4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarRespuesta(respuesta4, cajaRespuesta4, cajaRespuesta1, cajaRespuesta2, cajaRespuesta3);
            }
        });
    }

    private void comprobarRespuesta(TextView seleccionada, LinearLayout cajaCorrecta, LinearLayout cajaIncorrecta1, LinearLayout cajaIncorrecta2, LinearLayout cajaIncorrecta3) {
        contador.cancel();
        textoPuntuacion = findViewById(R.id.texto_puntuacion);
        textoPuntos = findViewById(R.id.texto_puntos);
        btnSiguiente = findViewById(R.id.btn_siguiente);
        Pregunta pregunta = listaPreguntas.get(indicePregunta);
        String respuestaCorrecta = pregunta.getRespuestaCorrecta();

        if (seleccionada.getText().toString().equals(respuestaCorrecta)) {
            cajaCorrecta.setBackgroundColor(Color.GREEN);
            cajaIncorrecta1.setClickable(false);
            cajaIncorrecta2.setClickable(false);
            cajaIncorrecta3.setClickable(false);

            int puntosGanados = (int) (tiempoRestante / 100);
            puntuacion += puntosGanados;

            if (textoPuntuacion != null) {
                textoPuntuacion.setText("Puntuación: ");
                textoPuntos.setText(String.valueOf(puntuacion));
            }
        }
        else {
            cajaCorrecta.setBackgroundColor(Color.RED);

            if (respuesta1.getText().toString().equals(respuestaCorrecta)) {
                cajaRespuesta1.setBackgroundColor(Color.GREEN);
                cajaRespuesta2.setClickable(false);
                cajaRespuesta3.setClickable(false);
                cajaRespuesta4.setClickable(false);
            } else if (respuesta2.getText().toString().equals(respuestaCorrecta)) {
                cajaRespuesta2.setBackgroundColor(Color.GREEN);
                cajaRespuesta1.setClickable(false);
                cajaRespuesta3.setClickable(false);
                cajaRespuesta4.setClickable(false);
            } else if (respuesta3.getText().toString().equals(respuestaCorrecta)) {
                cajaRespuesta3.setBackgroundColor(Color.GREEN);
                cajaRespuesta1.setClickable(false);
                cajaRespuesta2.setClickable(false);
                cajaRespuesta4.setClickable(false);
            } else if (respuesta4.getText().toString().equals(respuestaCorrecta)) {
                cajaRespuesta4.setBackgroundColor(Color.GREEN);
                cajaRespuesta1.setClickable(false);
                cajaRespuesta2.setClickable(false);
                cajaRespuesta3.setClickable(false);
            }
        }
        btnSiguiente.setVisibility(View.VISIBLE);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indicePregunta++;
                mostrarSiguientePregunta();
            }
        });
    }

    private void finalizarJuego() {
        Partida nuevaPartida = new Partida();
        Retrofit retro = new Retrofit();

        nuevaPartida.setPuntuacion(puntuacion);
        nuevaPartida.setDificultad(dificultad);
        nuevaPartida.setRondasJugadas(rondas);

        retro.getApi().guardarPartida(nuevaPartida).enqueue(new Callback<ApiResponse>() { // ¡Aquí ApiResponse!
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Accedes a los campos de la respuesta usando los getters de ApiResponse
                    if (response.body().isSuccess()) {
                        Toast.makeText(Juego.this, "Partida guardada con éxito! ID: " + response.body().getId(), Toast.LENGTH_SHORT).show();
                        // Puedes redirigir o actualizar la UI aquí
                    } else {
                        Toast.makeText(Juego.this, "Error al guardar partida: " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Manejar errores de HTTP (códigos 4xx, 5xx) o cuerpo de respuesta nulo
                    Toast.makeText(Juego.this, "Error en la respuesta del servidor: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Manejar errores de red (no se pudo conectar al servidor)
                Toast.makeText(Juego.this, "Error de red al guardar partida: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        nuevaPartida.setPuntuacion(0);
    }
}