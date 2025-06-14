package com.example.openquest;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetallePreguntaEditar extends AppCompatActivity {

    private int idPregunta;
    private String categoria;
    private Spinner spinnerIdioma;
    private EditText preguntaEditada;
    private EditText respuesta1Editada;
    private EditText respuesta2Editada;
    private EditText respuesta3Editada;
    private EditText respuesta4Editada;
    private EditText respuestaCorrectaEditada;
    private Button botonEditar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_pregunta_editar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializar();
        configurarSpinnerIdioma();
        editar();
    }

    private void inicializar() {
        idPregunta = getIntent().getIntExtra("ID_PREGUNTA", -1);
        categoria = getIntent().getStringExtra("CATEGORIA_ID");
        spinnerIdioma = findViewById(R.id.spinner_idioma_editar_pregunta);
        preguntaEditada = findViewById(R.id.pregunta_edicion);
        respuesta1Editada = findViewById(R.id.respuesta1_edicion);
        respuesta2Editada = findViewById(R.id.respuesta2_edicion);
        respuesta3Editada = findViewById(R.id.respuesta3_edicion);
        respuesta4Editada = findViewById(R.id.respuesta4_edicion);
        respuestaCorrectaEditada = findViewById(R.id.respuesta_correcta_edicion);
        botonEditar = findViewById(R.id.boton_editar_pregunta_usuario);
    }

    private void configurarSpinnerIdioma() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.idiomas_pregunta, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdioma.setAdapter(adapter);
    }
    private void editar() {
        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pregunta = preguntaEditada.getText().toString().trim();
                String respuesta1 = respuesta1Editada.getText().toString().trim();
                String respuesta2 = respuesta2Editada.getText().toString().trim();
                String respuesta3 = respuesta3Editada.getText().toString().trim();
                String respuesta4 = respuesta4Editada.getText().toString().trim();
                String respuestaCorrecta = respuestaCorrectaEditada.getText().toString().trim();

                if (pregunta.isEmpty() || respuesta1.isEmpty() || respuesta2.isEmpty() || respuesta3.isEmpty() || respuesta4.isEmpty() || respuestaCorrecta.isEmpty()) {
                    Toast.makeText(ActivityDetallePreguntaEditar.this, getString(R.string.not_all_answers), Toast.LENGTH_SHORT).show();
                }
                else {
                    String[] respuestasOpciones = {respuesta1, respuesta2, respuesta3, respuesta4};
                    boolean respuestaCorrectaEncontrada = false;
                    for (int i = 0; i < respuestasOpciones.length; i++) {
                        if (respuestaCorrecta.equalsIgnoreCase(respuestasOpciones[i])) {
                            respuestaCorrectaEncontrada = true;
                            break;
                        }
                    }

                    if (respuestaCorrectaEncontrada) {
                        Pregunta nuevaPregunta = new Pregunta();

                        nuevaPregunta.setIdPregunta(idPregunta);
                        nuevaPregunta.setPregunta(pregunta);
                        nuevaPregunta.setCategoria(categoria);
                        nuevaPregunta.setRespuesta1(respuesta1);
                        nuevaPregunta.setRespuesta2(respuesta2);
                        nuevaPregunta.setRespuesta3(respuesta3);
                        nuevaPregunta.setRespuesta4(respuesta4);
                        nuevaPregunta.setRespuestaCorrecta(respuestaCorrecta);

                        Retrofit retro = new Retrofit();

                        retro.getApi().editarPregunta(nuevaPregunta).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    ApiResponse apiResponse = response.body();
                                    if (apiResponse.getMensaje() != null) {
                                        Toast.makeText(ActivityDetallePreguntaEditar.this, apiResponse.getMensaje(), Toast.LENGTH_LONG).show();
                                        preguntaEditada.setText("");
                                        respuesta1Editada.setText("");
                                        respuesta2Editada.setText("");
                                        respuesta3Editada.setText("");
                                        respuesta4Editada.setText("");
                                        respuestaCorrectaEditada.setText("");
                                        spinnerIdioma.setSelection(0);
                                        finish();
                                    } else if (apiResponse.getError() != null) {
                                        Toast.makeText(ActivityDetallePreguntaEditar.this, getString(R.string.create_question_error) + apiResponse.getError(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(ActivityDetallePreguntaEditar.this, getString(R.string.create_question_error_http) + response.code(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(ActivityDetallePreguntaEditar.this, getString(R.string.network_error) + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(ActivityDetallePreguntaEditar.this, getString(R.string.responses_do_not_match), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}