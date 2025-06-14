package com.example.openquest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEdicionPregunta extends AppCompatActivity implements PreguntaAdapter.OnItemClickListener{

    private List<Pregunta> listaPreguntas = new ArrayList<>();
    private PreguntaAdapter adaptadorPreguntas;
    private RecyclerView recyclerPreguntas;
    private String idioma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edicion_pregunta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        seleccionarPregunta();
    }

    private void seleccionarPregunta() {
        recyclerPreguntas = findViewById(R.id.recycler_preguntas_editar); // Referencia al RecyclerView por su ID
        recyclerPreguntas.setLayoutManager(new LinearLayoutManager(this)); // Establece un LayoutManager lineal
        adaptadorPreguntas = new PreguntaAdapter(listaPreguntas, this); // Pasa la lista y el listener
        recyclerPreguntas.setAdapter(adaptadorPreguntas);

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        idioma = prefs.getString("idioma", "es");

        Retrofit retro = new Retrofit();

        int categoria = getIntent().getIntExtra("categoria", -1);

        if (categoria == -1) {
            Toast.makeText(this, "Error: Categoría no recibida.", Toast.LENGTH_SHORT).show();
            return;
        }

        retro.getApi().obtenerPreguntasPorCategoriaEnIdioma(categoria, idioma).enqueue(new Callback<List<Pregunta>>() {
            @Override
            public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                listaPreguntas = response.body();

                if (listaPreguntas.isEmpty()) {
                    Toast.makeText(ActivityEdicionPregunta.this, "No hay preguntas en esta categoría.", Toast.LENGTH_LONG).show();
                }
                adaptadorPreguntas.updateData(listaPreguntas);
            }

            @Override
            public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                Toast.makeText(ActivityEdicionPregunta.this, "Error al obtener preguntas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditarClick(Pregunta pregunta) {
        Intent intent = new Intent(ActivityEdicionPregunta.this, ActivityDetallePreguntaEditar.class);
        intent.putExtra("ID_PREGUNTA", pregunta.getIdPregunta()); // Pasa el ID de la pregunta
        intent.putExtra("PREGUNTA_TEXTO", pregunta.getPregunta());
        intent.putExtra("CATEGORIA_ID",String.valueOf(pregunta.getCategoria())); // Pasa el ID de la categoría
        intent.putExtra("RESPUESTA1", pregunta.getRespuesta1());
        intent.putExtra("RESPUESTA2", pregunta.getRespuesta2());
        intent.putExtra("RESPUESTA3", pregunta.getRespuesta3());
        intent.putExtra("RESPUESTA4", pregunta.getRespuesta4());
        intent.putExtra("RESPUESTA_CORRECTA", pregunta.getRespuestaCorrecta());
        startActivity(intent);
    }
}