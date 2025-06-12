package com.example.openquest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAdministracionPerfil extends AppCompatActivity {

    private EditText txtNuevoNombre;
    private EditText txtNuevoPassword;
    private int loggedInUserId;
    private String nombreUsuarioLogeado;
    private Button botonEditarPerfil;
    private Button botonEliminarPerfil;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administracion_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        administrarPerfil();
    }

    private void administrarPerfil() {
        txtNuevoNombre = findViewById(R.id.txtNombreNuevo);
        txtNuevoPassword = findViewById(R.id.txtPasswordNuevo);
        botonEditarPerfil = findViewById(R.id.btn_editar_perfil);
        botonEliminarPerfil = findViewById(R.id.btn_eliminar_perfil);

        //Recuperar los datos del usuario de SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loggedInUserId = prefs.getInt(KEY_USER_ID, -1); // -1 como valor por defecto si no se encuentra
        nombreUsuarioLogeado = prefs.getString(KEY_USERNAME, "Invitado"); // "Invitado" como valor por defecto

        if (nombreUsuarioLogeado != null && !nombreUsuarioLogeado.isEmpty()) {
            txtNuevoNombre.setText(nombreUsuarioLogeado); // Esto pondrá el nombre actual en el campo de edición
        }

        botonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nuevoNombre = txtNuevoNombre.getText().toString().trim();
                String nuevoPassword = txtNuevoPassword.getText().toString().trim();

                if (loggedInUserId == -1) {
                    Toast.makeText(ActivityAdministracionPerfil.this, "Error: No se pudo obtener el ID de usuario. Vuelve a iniciar sesión.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Si ambos campos están vacíos
                if (nuevoNombre.isEmpty() && nuevoPassword.isEmpty()) {
                    Toast.makeText(ActivityAdministracionPerfil.this, "Introduce al menos un nuevo nombre o una nueva contraseña.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar contraseña si se intenta cambiar
                if (!nuevoPassword.isEmpty()) {
                    if (nuevoPassword.length() < 6) {
                        Toast.makeText(ActivityAdministracionPerfil.this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                    }
                }

                Map<String, String> datosUsuario = new HashMap<>();
                datosUsuario.put("user_id", String.valueOf(loggedInUserId)); // Envía el ID del usuario

                if (!nuevoNombre.isEmpty()) {
                    datosUsuario.put("new_name", nuevoNombre);
                }
                if (!nuevoPassword.isEmpty()) {
                    datosUsuario.put("new_password", nuevoPassword);
                }

                Retrofit retro = new Retrofit();

                retro.getApi().editarUsuario(datosUsuario).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse.getMensaje() != null) {
                            Toast.makeText(ActivityAdministracionPerfil.this, apiResponse.getMensaje(), Toast.LENGTH_LONG).show();

                            if (!nuevoNombre.isEmpty()) {
                                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString(KEY_USERNAME, nuevoNombre);
                                editor.apply();
                            }
                            txtNuevoPassword.setText("");
                        }
                        else if (apiResponse.getError() != null) {
                            Toast.makeText(ActivityAdministracionPerfil.this, "Error al actualizar perfil: " + apiResponse.getError(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(ActivityAdministracionPerfil.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        botonEliminarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(loggedInUserId)); // Envía el ID del usuario a eliminar

                Retrofit retro = new Retrofit();

                retro.getApi().eliminarUsuario(params).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        ApiResponse apiResponse = response.body();

                        if (apiResponse.isSuccess()) {
                            Toast.makeText(ActivityAdministracionPerfil.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                            // Desloguear al usuario después de eliminar el perfil
                            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.remove(KEY_USER_ID);
                            editor.remove(KEY_USERNAME);
                            editor.putBoolean(KEY_LOGGED_IN, false);
                            editor.putBoolean("es_invitado", true);
                            editor.apply();

                            // Redirigir a la pantalla de inicio de sesión o principal como invitado
                            Intent intent = new Intent(ActivityAdministracionPerfil.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ActivityAdministracionPerfil.this, "Error al eliminar: " + apiResponse.getError(), Toast.LENGTH_LONG).show();
                            Log.e("PerfilAdminAPI", "Error al eliminar perfil: " + apiResponse.getError());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(ActivityAdministracionPerfil.this, "Error de conexión al eliminar: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}