package com.example.openquest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityAdministracionPerfil extends AppCompatActivity {

    private EditText txtNuevoNombre; // Para el EditText donde se edita el nombre
    private int loggedInUserId;
    private String nombreUsuarioLogeado;

    private Button botonEliminarPerfil;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";

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
        txtNuevoNombre = findViewById(R.id.txtNombreNuevo); // Asegúrate que este ID sea el de tu EditText en XML
        botonEliminarPerfil = findViewById(R.id.btn_eliminar_perfil);

        //Recuperar los datos del usuario de SharedPreferences (el más robusto)
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loggedInUserId = prefs.getInt(KEY_USER_ID, -1); // -1 como valor por defecto si no se encuentra
        nombreUsuarioLogeado = prefs.getString(KEY_USERNAME, "Invitado"); // "Invitado" como valor por defecto

        // También podrías intentar recuperarlo del Intent si vienes directamente de LoginActivity
        // Pero SharedPreferences es mejor para persistencia de la sesión
        // if (getIntent().hasExtra("USERNAME")) {
        //     loggedInUsername = getIntent().getStringExtra("USERNAME");
        // }

        // 3. Mostrar el nombre del usuario en el EditText (o TextView)
        if (nombreUsuarioLogeado != null && !nombreUsuarioLogeado.isEmpty()) {
            txtNuevoNombre.setText(nombreUsuarioLogeado); // Esto pondrá el nombre actual en el campo de edición
        }
    }
}