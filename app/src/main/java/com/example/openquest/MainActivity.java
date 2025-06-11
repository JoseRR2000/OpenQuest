package com.example.openquest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        iniciarInvitado();
        registrarse();
        iniciarSesion();
    }

    private void iniciarInvitado() {
        Button botonInvitado = findViewById(R.id.btn_guest);

        botonInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();

                // 1. Establecer el estado de sesión como NO logeado (invitado)
                editor.putBoolean(KEY_LOGGED_IN, false);

                // 2. Limpiar cualquier ID de usuario o nombre de usuario guardado previamente
                // Esto es crucial para asegurar que no se carguen datos de una sesión anterior
                editor.remove(KEY_USER_ID);
                editor.remove(KEY_USERNAME);

                // 3. Aplicar los cambios
                editor.apply();

                Intent intent = new Intent(MainActivity.this, PantallaJuego.class);
                startActivity(intent);
            }
        });
    }

    private void registrarse() {
        Button botonRegistrarse = findViewById(R.id.btn_register);

        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void iniciarSesion() {
        Button botonLogin = findViewById(R.id.btn_login);

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}