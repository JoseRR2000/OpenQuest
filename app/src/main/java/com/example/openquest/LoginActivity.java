package com.example.openquest;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {

    private EditText mailLogin;
    private EditText passwordLogin;
    private Button btnEnviar;
    private Retrofit retro;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        login();
    }

    private void login() {
        mailLogin = findViewById(R.id.txtLoginMail);
        passwordLogin = findViewById(R.id.txtPasswordLogin);
        btnEnviar = findViewById(R.id.btnEnviar);

        retro = new Retrofit();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mailLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.not_all_answers), Toast.LENGTH_SHORT).show();
                }

                if (!email.endsWith("@gmail.com")) {
                    Toast.makeText(LoginActivity.this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
                }

                retro.getApi().login(email, password).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.isSuccess()) {
                                Toast.makeText(LoginActivity.this, getString(R.string.welcome), Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putBoolean(KEY_LOGGED_IN, true);
                                editor.putInt(KEY_USER_ID, loginResponse.getUserId());
                                editor.putString(KEY_USERNAME, loginResponse.getUsername());
                                editor.apply();

                                entrarPantallaJuego(loginResponse.getUserId(), loginResponse.getUsername());

                            } else {
                                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.server_error) + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, getString(R.string.network_error) + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void entrarPantallaJuego(int userId, String username) {
        Intent intent = new Intent(LoginActivity.this, PantallaJuego.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        finish();
    }
}