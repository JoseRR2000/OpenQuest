package com.example.openquest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaJuego extends AppCompatActivity {
    private int sonidoPulsar;
    private boolean efectosActivados;
    private boolean musicaActivada;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable actualizarRanking;
    private SoundPool sp;
    private SwitchCompat scSonido;
    private SwitchCompat scMusica;
    private SwitchCompat scRondas;
    private SwitchCompat scTiempo;
    private Spinner spIdioma;
    private Spinner spDificultad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_juego);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cargarEfectos();
        cambiarPestanas();
        obtenerRanking();
        actualizarRanking();
        opciones();
        cargarAjustes();
    }

    private void cargarAjustes() {
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        efectosActivados = prefs.getBoolean("efectos", true);
        musicaActivada = prefs.getBoolean("musica", true);
        scSonido.setChecked(efectosActivados);
        scMusica.setChecked(musicaActivada);

        String idioma = prefs.getString("idioma", "es"); // valor por defecto: español
        if (spIdioma != null) {
            spIdioma.setSelection(idioma.equals("es") ? 1 : 0);
        }


    }

    /*Funcion que se encarga de cargar el sonido de efectos*/
    private void cargarEfectos() {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();

            sonidoPulsar = sp.load(this, R.raw.pulsar_boton, 1);
    }

    /*Funcion encargada de controlar las pulsaciones de la barra de navegacion*/
    private void controlarNavegacion(ScrollView s1, ScrollView s2, ScrollView s3, View v1, View v2, View v3, boolean esRanking) {

        /*Hacemos visible la seccion que el usuario pulse e invisibles las demas*/
        s1.setVisibility(View.VISIBLE);
        s2.setVisibility(View.GONE);
        s3.setVisibility(View.GONE);
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.INVISIBLE);
        v3.setVisibility(View.INVISIBLE);

        if (esRanking) {
            handler.post(actualizarRanking);
        } else {
            handler.removeCallbacks(actualizarRanking);
        }

        /*Reproducimos sonido por cada pulsacion*/
        if (efectosActivados) {
            sp.play(sonidoPulsar, 1, 1, 0, 0, 1);
        }
    }

    /*Esta funcion se encarga de ejecutar la funcion anterior en cada listener
    * de los botones de la barra de navegacion*/
    private void cambiarPestanas() {
        LinearLayout botonJugar = findViewById(R.id.botonJugar);
        LinearLayout botonRanking = findViewById(R.id.botonRanking);
        LinearLayout botonOpciones = findViewById(R.id.botonOpciones);
        LinearLayout botonEditor = findViewById(R.id.botonEditor);
        LinearLayout botonLogros = findViewById(R.id.botonLogros);
        ScrollView pestanaJugar = findViewById(R.id.scroll1);
        ScrollView pestanaRanking = findViewById(R.id.scroll2);
        ScrollView pestanaOpciones = findViewById(R.id.scroll3);
        View lineaJugar = findViewById(R.id.linea_jugar);
        View lineaRanking = findViewById(R.id.linea_ranking);
        View lineaOpciones = findViewById(R.id.linea_opciones);

        /*Listeners*/
        botonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlarNavegacion(pestanaJugar, pestanaRanking, pestanaOpciones, lineaJugar, lineaRanking, lineaOpciones, false);
            }
        });

        botonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlarNavegacion(pestanaRanking, pestanaJugar, pestanaOpciones, lineaRanking, lineaJugar, lineaOpciones, true);
            }
        });

        botonOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlarNavegacion(pestanaOpciones, pestanaJugar, pestanaRanking, lineaOpciones, lineaJugar, lineaRanking, false);
            }
        });
    }

    /*Funcion encargada de darnos el ranking de puntuaciones*/
    private void obtenerRanking() {
        LinearLayout contenedor = findViewById(R.id.layout_ranking);
        TextView textoVacio = findViewById(R.id.texto_vacio);
        Retrofit retro = new Retrofit();

        retro.api.obtenerPartidas().enqueue(new Callback<List<Partida>>() {

           @Override
            public void onResponse(@NonNull Call<List<Partida>> call, @NonNull Response<List<Partida>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Partida> partidas = response.body();
                    if (partidas.isEmpty()) {
                        textoVacio.setVisibility(View.VISIBLE);
                    } else {
                        textoVacio.setVisibility(View.GONE);
                        contenedor.removeAllViews();
                        for (Partida p : partidas) {
                            TextView tv = new TextView(PantallaJuego.this);
                            tv.setText(p.getJugador() + " - " + p.getPuntuacion() + " pts - " + p.getFecha());
                            tv.setPadding(10, 10, 10, 10);
                            contenedor.addView(tv);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Partida>> call, @NonNull Throwable t) {
                textoVacio.setText("Tenemos problemas con el servidor");
                textoVacio.setVisibility(View.VISIBLE);
            }
        });
    }

    /*Funcion que maneja el menu de opciones*/
    private void opciones() {
        ImageView bandera = findViewById(R.id.imagenBandera);
        scSonido = findViewById(R.id.switch_efecto_sonido);
        scMusica = findViewById(R.id.switch_musica_juego);
        scRondas = findViewById(R.id.switch_limite_rondas);
        scTiempo = findViewById(R.id.switch_limite_tiempo);
        spIdioma = findViewById(R.id.spinner_idioma);
        spDificultad = findViewById(R.id.spinner_dificultad);
        String[] idiomas = {getString(R.string.english), getString(R.string.spanish)};
        String[] dificultades = {"Fácil", "Normal", "Difícil"};

        /*Switches*/
        scSonido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                efectosActivados = isChecked;

                SharedPreferences configEfectos = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                SharedPreferences.Editor configuracion = configEfectos.edit();
                configuracion.putBoolean("efectos", isChecked);
                configuracion.apply();
            }
        });

        scMusica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                musicaActivada = isChecked;

                SharedPreferences configMusica = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                SharedPreferences.Editor configuracion = configMusica.edit();
                configuracion.putBoolean("musica", isChecked);
                configuracion.apply();
            }
        });

        //Introducir switches de limite rondas y limite tiempo en el futuro


        /*Spinners*/
        ArrayAdapter<String> adaptadorIdiomas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, idiomas);
        adaptadorIdiomas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spIdioma.setAdapter(adaptadorIdiomas);

        ArrayAdapter<String> adaptadorDificultad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dificultades);
        adaptadorDificultad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDificultad.setAdapter(adaptadorDificultad);

        String idiomaActual = Locale.getDefault().getLanguage();
        spIdioma.setSelection(idiomaActual.equals("es") ? 1 : 0);

        final boolean[] primeraVez = {true};

        spIdioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long id) {
                if (primeraVez[0]) {
                    primeraVez[0] = false;
                    return;
                }

                String nuevoIdioma = "";

                if (posicion == 0) {
                    nuevoIdioma = "en";
                    bandera.setImageResource(R.drawable.bandera);
                }
                else {
                    nuevoIdioma = "es";
                    bandera.setImageResource(R.drawable.bandera);
                }

                cambiarIdioma(nuevoIdioma);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void cambiarIdioma(String nuevoIdioma) {
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String idiomaActual = prefs.getString("idioma", "es");

        if (!nuevoIdioma.equals(idiomaActual)) {
            prefs.edit().putString("idioma", nuevoIdioma).apply();
            Locale nuevoLocale = new Locale(nuevoIdioma);
            Locale.setDefault(nuevoLocale);
            Configuration config = new Configuration();
            config.setLocale(nuevoLocale);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());

            recreate();
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String idioma = prefs.getString("idioma", "es");

        Locale nuevoLocale = new Locale(idioma);
        Locale.setDefault(nuevoLocale);

        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(nuevoLocale);

        Context contextoActualizado = newBase.createConfigurationContext(config);
        super.attachBaseContext(contextoActualizado);
    }


    private void actualizarRanking() {
        actualizarRanking = new Runnable() {
            @Override
            public void run() {
                obtenerRanking();
                handler.postDelayed(this, 2000);
            }
        };

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (sp != null) {
            sp.release();
            sp = null;
        }
    }
}