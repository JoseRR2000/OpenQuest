package com.example.openquest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText textoNombre;
    private EditText textoMail;
    private EditText textoContrasena;
    private Button botonEnviar;
    private Retrofit retro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        enviar();
    }

    private void enviar() {
        textoNombre = findViewById(R.id.txtNombreUsuario);
        textoMail = findViewById(R.id.txtEmailUsuario);
        textoContrasena = findViewById(R.id.textContrasenaUsuario);
        botonEnviar = findViewById(R.id.botonEnviar);

        retro = new Retrofit();


        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreUsuario = textoNombre.getText().toString().trim();
                String contrasenaUsuario = textoContrasena.getText().toString().trim();
                String mailUsuario = textoMail.getText().toString().trim();

                if (nombreUsuario.isEmpty() || contrasenaUsuario.isEmpty() || mailUsuario.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show();
                }

                if (!mailUsuario.endsWith("@gmail.com")) {
                    Toast.makeText(RegisterActivity.this, "Debes introducir un mail válido", Toast.LENGTH_LONG).show();
                }

                retro.getApi().registerUser(nombreUsuario, mailUsuario, contrasenaUsuario).enqueue(new Callback<RegistrationResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            RegistrationResponse regResponse = response.body();
                            if (regResponse.isSuccess()) {
                                // Registro exitoso
                                Toast.makeText(RegisterActivity.this, "¡Registro exitoso! Ahora puedes iniciar sesión.", Toast.LENGTH_LONG).show();
                                finish(); // Cierra esta actividad y regresa a la anterior (MainActivity)
                            } else {
                                // Registro fallido (ej. usuario/email ya existe, error en el servidor)
                                Toast.makeText(RegisterActivity.this, "Error de registro: " + regResponse.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // La respuesta del servidor no fue exitosa (código 4xx, 5xx)
                            Toast.makeText(RegisterActivity.this, "Error en el servidor al registrar: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Error de red al registrar: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}