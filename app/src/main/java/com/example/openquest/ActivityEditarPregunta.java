package com.example.openquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityEditarPregunta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_pregunta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        seleccionCategoria();
    }

    private void seleccionCategoria() {
        LinearLayout geografia = findViewById(R.id.modo_geografia_editar);
        LinearLayout historia = findViewById(R.id.modo_historia_editar);
        LinearLayout entretenimiento = findViewById(R.id.modo_entretenimiento_editar);
        LinearLayout deportes = findViewById(R.id.modo_deportes_editar);
        LinearLayout informatica = findViewById(R.id.modo_informatica_editar);
        LinearLayout medicina = findViewById(R.id.modo_medicina_editar);

        geografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEditarPregunta.this, ActivityEdicionPregunta.class);
                intent.putExtra("modo", "geografia");
                intent.putExtra("categoria", 1);
                startActivity(intent);
            }
        });

        historia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEditarPregunta.this, ActivityEdicionPregunta.class);
                intent.putExtra("modo", "historia");
                intent.putExtra("categoria", 2);
                startActivity(intent);
            }
        });

        entretenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEditarPregunta.this, ActivityEdicionPregunta.class);
                intent.putExtra("modo", "entretenimiento");
                intent.putExtra("categoria", 3);
                startActivity(intent);
            }
        });

        deportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEditarPregunta.this, ActivityEdicionPregunta.class);
                intent.putExtra("modo", "deportes");
                intent.putExtra("categoria", 4);
                startActivity(intent);
            }
        });

        informatica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEditarPregunta.this, ActivityEdicionPregunta.class);
                intent.putExtra("modo", "informatica");
                intent.putExtra("categoria", 5);
                startActivity(intent);
            }
        });

        medicina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEditarPregunta.this, ActivityEdicionPregunta.class);
                intent.putExtra("modo", "medicina");
                intent.putExtra("categoria", 6);
                startActivity(intent);
            }
        });
    }
}