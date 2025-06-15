package com.example.openquest;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class PantallaCrearPregunta extends AppCompatActivity {

    private Spinner spinnerIdioma;
    private RadioGroup radioGroupCategorias;
    private EditText preguntaUsuario;
    private EditText respuesta1Usuario;
    private EditText respuesta2Usuario;
    private EditText respuesta3Usuario;
    private EditText respuesta4Usuario;
    private EditText respuestaCorrectaUsuario;
    private Button botonEnviar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_crear_pregunta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializar();
        configurarSpinnerIdioma();
        enviarPregunta();
    }

    private void inicializar() {
        spinnerIdioma = findViewById(R.id.spinner_idioma_crear_pregunta);
        radioGroupCategorias = findViewById(R.id.rg_categoria_crear_pregunta);
        preguntaUsuario = findViewById(R.id.pregunta_usuario);
        respuesta1Usuario = findViewById(R.id.respuesta1_suario);
        respuesta2Usuario = findViewById(R.id.respuesta2_usuario);
        respuesta3Usuario = findViewById(R.id.respuesta3_usuario);
        respuesta4Usuario = findViewById(R.id.respuesta4_usuario);
        respuestaCorrectaUsuario = findViewById(R.id.respuesta_correcta_usuario);
        botonEnviar = findViewById(R.id.boton_enviar_pregunta_usuario);
    }

    private void configurarSpinnerIdioma() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.idiomas_pregunta, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdioma.setAdapter(adapter);
    }

    private void enviarPregunta() {
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pregunta = preguntaUsuario.getText().toString().trim();
                String respuesta1 = respuesta1Usuario.getText().toString().trim();
                String respuesta2 = respuesta2Usuario.getText().toString().trim();
                String respuesta3 = respuesta3Usuario.getText().toString().trim();
                String respuesta4 = respuesta4Usuario.getText().toString().trim();
                String respuestaCorrecta = respuestaCorrectaUsuario.getText().toString().trim();
                int categoriaSeleccionada = radioGroupCategorias.getCheckedRadioButtonId();

                if (categoriaSeleccionada != -1) {
                    RadioButton selectedRadioButton = findViewById(categoriaSeleccionada);
                    String categoria = "";
                    String mapeo = "";

                    categoria = selectedRadioButton.getText().toString();

                    switch (categoria) {
                        case "GEOGRAFÍA":
                        case "GEOGRAPHY":
                            mapeo = "1";
                            break;
                        case "HISTORIA":
                        case "HISTORY":
                            mapeo = "2";
                            break;
                        case "ENTRETENIMIENTO":
                        case "ENTERTAIMENT":
                            mapeo = "3";
                            break;
                        case "DEPORTES":
                        case "SPORTS":
                            mapeo = "4";
                            break;
                        case "INFORMÁTICA":
                        case "COMPUTING":
                            mapeo = "5";
                            break;
                        case "MEDICINA":
                        case "MEDICINE":
                            mapeo = "6";
                            break;
                        default:
                            Toast.makeText(PantallaCrearPregunta.this, getString(R.string.category_not_found), Toast.LENGTH_SHORT).show();
                            return;
                    }

                    if (pregunta.isEmpty() || respuesta1.isEmpty() || respuesta2.isEmpty() || respuesta3.isEmpty() || respuesta4.isEmpty() || respuestaCorrecta.isEmpty()) {
                        Toast.makeText(PantallaCrearPregunta.this, getString(R.string.not_all_answers), Toast.LENGTH_SHORT).show();
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

                            nuevaPregunta.setCategoria(categoria);
                            nuevaPregunta.setPregunta(pregunta);
                            nuevaPregunta.setCategoria(mapeo);
                            nuevaPregunta.setRespuesta1(respuesta1);
                            nuevaPregunta.setRespuesta2(respuesta2);
                            nuevaPregunta.setRespuesta3(respuesta3);
                            nuevaPregunta.setRespuesta4(respuesta4);
                            nuevaPregunta.setRespuestaCorrecta(respuestaCorrecta);

                            Retrofit retro = new Retrofit();

                            retro.getApi().crearPregunta(nuevaPregunta).enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        ApiResponse apiResponse = response.body();
                                        if (apiResponse.getMensaje() != null) {
                                            Toast.makeText(PantallaCrearPregunta.this, getString(R.string.question_created), Toast.LENGTH_LONG).show();
                                            preguntaUsuario.setText("");
                                            respuesta1Usuario.setText("");
                                            respuesta2Usuario.setText("");
                                            respuesta3Usuario.setText("");
                                            respuesta4Usuario.setText("");
                                            respuestaCorrectaUsuario.setText("");
                                            radioGroupCategorias.clearCheck();
                                            spinnerIdioma.setSelection(0);
                                            finish();
                                        } else if (apiResponse.getError() != null) {
                                            Toast.makeText(PantallaCrearPregunta.this, getString(R.string.create_question_error) + apiResponse.getError(), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(PantallaCrearPregunta.this, getString(R.string.create_question_error_http) + response.code(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    Toast.makeText(PantallaCrearPregunta.this, getString(R.string.network_error) + t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(PantallaCrearPregunta.this, getString(R.string.responses_do_not_match), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(PantallaCrearPregunta.this, R.string.category_unselected_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}