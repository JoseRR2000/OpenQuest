package com.example.openquest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
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
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Juego extends AppCompatActivity {
    private CountDownTimer contador;
    private TextView tiempo;
    private List<Pregunta> listaPreguntas;
    private int indicePregunta;
    private Random random;
    private TextView textoPregunta;
    private TextView respuesta1;
    private TextView respuesta2;
    private TextView respuesta3;
    private TextView respuesta4;
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

        obtenerPreguntas();
        iniciarTemporizador();
        seleccionRespuestas();
    }

    private void iniciarTemporizador() {
        if (contador != null) {
            contador.cancel();
        }

        tiempo = findViewById(R.id.texto_tiempo);

        contador = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                tiempo.setText(String.valueOf(millisUntilFinished / 1000));
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
            String categoria = getIntent().getStringExtra("categoria");
            obtenerPreguntasPorCategoria(categoria);
        }
    }

    private void obtenerTodasLasPreguntas() {
        Retrofit retro = new Retrofit();
        retro.api.obtenerPreguntas().enqueue(new Callback<List<Pregunta>>() {
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

    private void obtenerPreguntasPorCategoria(String categoria) {
        Retrofit retro = new Retrofit();
        retro.api.obtenerPreguntasPorCategoria(categoria).enqueue(new Callback<List<Pregunta>>() {
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

        if (indicePregunta < listaPreguntas.size()) {
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

        respuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarRespuesta(respuesta1);
            }
        });

        respuesta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarRespuesta(respuesta2);
            }
        });

        respuesta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarRespuesta(respuesta3);
            }
        });

        respuesta4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarRespuesta(respuesta4);
            }
        });
    }

    private void comprobarRespuesta(TextView seleccionada) {
        contador.cancel();
        btnSiguiente = findViewById(R.id.btn_siguiente);
        Pregunta pregunta = listaPreguntas.get(indicePregunta);
        String respuestaCorrecta = pregunta.getRespuestaCorrecta();

        if (seleccionada.getText().toString().equals(respuestaCorrecta)) {
            seleccionada.setBackgroundColor(Color.GREEN);
        }
        else {
            seleccionada.setBackgroundColor(Color.RED);

            if (respuesta1.getText().toString().equals(respuestaCorrecta)) {
                respuesta1.setBackgroundColor(Color.GREEN);
            } else if (respuesta2.getText().toString().equals(respuestaCorrecta)) {
                respuesta2.setBackgroundColor(Color.GREEN);
            } else if (respuesta3.getText().toString().equals(respuestaCorrecta)) {
                respuesta3.setBackgroundColor(Color.GREEN);
            } else if (respuesta4.getText().toString().equals(respuestaCorrecta)) {
                respuesta4.setBackgroundColor(Color.GREEN);
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
}