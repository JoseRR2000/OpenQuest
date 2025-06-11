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
    private RadioButton radioButtonGeografia;
    private RadioButton radioButtonHistoria;
    private RadioButton radioButtonEntretenimiento;
    private RadioButton radioButtonDeportes;
    private RadioButton radioButtonInformatica;
    private RadioButton radioButtonMedicina;
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
        radioButtonGeografia = findViewById(R.id.rb_geografia_crear_pregunta);
        radioButtonHistoria = findViewById(R.id.rb_historia_crear_pregunta);
        radioButtonEntretenimiento = findViewById(R.id.rb_entretenimiento_crear_pregunta);
        radioButtonDeportes = findViewById(R.id.rb_deportes_crear_pregunta);
        radioButtonInformatica = findViewById(R.id.rb_informatica_crear_pregunta);
        radioButtonMedicina = findViewById(R.id.rb_medicina_crear_pregunta);
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
                            mapeo = "1";
                            break;
                        case "HISTORIA":
                            mapeo = "2";
                            break;
                        case "ENTRETENIMIENTO":
                            mapeo = "3";
                            break;
                        case "DEPORTES":
                            mapeo = "4";
                            break;
                        case "INFORMÁTICA":
                            mapeo = "5";
                            break;
                        case "MEDICINA":
                            mapeo = "6";
                            break;
                        default:
                            Toast.makeText(PantallaCrearPregunta.this, "Error: Categoría no reconocida.", Toast.LENGTH_SHORT).show();
                            return;
                    }

                    String idiomaSeleccionado = spinnerIdioma.getSelectedItem().toString();

                    if (pregunta.isEmpty() || respuesta1.isEmpty() || respuesta2.isEmpty() || respuesta3.isEmpty() || respuesta4.isEmpty() || respuestaCorrecta.isEmpty()) {
                        Toast.makeText(PantallaCrearPregunta.this, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(PantallaCrearPregunta.this, apiResponse.getMensaje(), Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(PantallaCrearPregunta.this, "Error al crear pregunta: " + apiResponse.getError(), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(PantallaCrearPregunta.this, "Error al crear pregunta: HTTP " + response.code(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    Toast.makeText(PantallaCrearPregunta.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(PantallaCrearPregunta.this, "La respuesta correcta no coincide con ninguna de las opciones.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(PantallaCrearPregunta.this, "Por favor, selecciona una categoría.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}