package com.example.openquest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaJuego extends AppCompatActivity {
    private int sonidoPulsar;
    private boolean efectosActivados;
    private boolean musicaActivada;
    private Dificultad dificultad;
    private String dificultadSeleccionada;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable actualizarRanking;
    private SoundPool sp;
    private MediaPlayer mp;
    private SwitchCompat scSonido;
    private SwitchCompat scMusica;
    private SwitchCompat scRondas;
    private SwitchCompat scTiempo;
    private Spinner spIdioma;
    private Spinner spDificultad;
    private RecyclerView recyclerLogros;

    private RecyclerView recyclerPartidas;
    private LogrosAdapter adapter;
    private PartidaAdapter adapterPartidas;
    private List<Logro> listaLogros = new ArrayList<>();

    private List<Partida> listaPartidas = new ArrayList<>();

    private LinearLayout layoutPerfil;

    private ScrollView pestanaRanking;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_LOGGED_IN = "isLoggedIn";

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
        comprobarLogin();
        cargarAdaptadores();
        cargarAudio();
        cambiarPestanas();
        actualizarRanking();
        jugar();
        opciones();
        editor();
        logros();
        cargarAjustes();
        perfil();
    }

    private void comprobarLogin() {
        layoutPerfil = findViewById(R.id.layoutCuentaOpciones);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean(KEY_LOGGED_IN, false);

        if (isLoggedIn) {
            layoutPerfil.setVisibility(View.VISIBLE);
        } else {
            layoutPerfil.setVisibility(View.GONE);
            layoutPerfil.setClickable(false);
        }
    }
    private void cargarAdaptadores() {
        recyclerLogros = findViewById(R.id.recyclerLogros);
        recyclerPartidas = findViewById(R.id.recyclerHistorial);

        recyclerLogros.setLayoutManager(new LinearLayoutManager(this));
        recyclerPartidas.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LogrosAdapter(listaLogros);
        adapterPartidas = new PartidaAdapter(listaPartidas);

        recyclerLogros.setAdapter(adapter);
        recyclerPartidas.setAdapter(adapterPartidas);
    }

    private void cargarAjustes() {
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        efectosActivados = prefs.getBoolean("efectos", true);
        musicaActivada = prefs.getBoolean("musica", true);
        scSonido.setChecked(efectosActivados);
        scMusica.setChecked(musicaActivada);

        String idioma = prefs.getString("idioma", "es");
        if (spIdioma != null) {
            spIdioma.setSelection(idioma.equals("es") ? 1 : 0);
        }
    }

    /*Funcion que se encarga de cargar el sonido de efectos*/
    private void cargarAudio() {
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
    private void controlarNavegacion(ScrollView s1, ScrollView s2, ScrollView s3, ScrollView s4, ScrollView s5, View v1, View v2, View v3, View v4, View v5, boolean esRanking) {

        /*Hacemos visible la seccion que el usuario pulse e invisibles las demas*/
        s1.setVisibility(View.VISIBLE);
        s2.setVisibility(View.GONE);
        s3.setVisibility(View.GONE);
        s4.setVisibility(View.GONE);
        s5.setVisibility(View.GONE);

        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.INVISIBLE);
        v3.setVisibility(View.INVISIBLE);
        v4.setVisibility(View.INVISIBLE);
        v5.setVisibility(View.INVISIBLE);

        if (esRanking) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean estaLogueado = prefs.getBoolean(KEY_LOGGED_IN, false);

            if (estaLogueado) {
                handler.post(actualizarRanking);
            }
            else {
                listaPartidas.clear();
                adapterPartidas.notifyDataSetChanged();
            }
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
        pestanaRanking = findViewById(R.id.scroll2);
        ScrollView pestanaOpciones = findViewById(R.id.scroll3);
        ScrollView pestanaEditor = findViewById(R.id.scroll4);
        ScrollView pestanaLogros = findViewById(R.id.scroll5);
        View lineaJugar = findViewById(R.id.linea_jugar);
        View lineaRanking = findViewById(R.id.linea_ranking);
        View lineaOpciones = findViewById(R.id.linea_opciones);
        View lineaEditor = findViewById(R.id.linea_editor);
        View lineaLogros = findViewById(R.id.linea_logros);

        /*Listeners*/
        botonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlarNavegacion(pestanaJugar, pestanaRanking, pestanaOpciones, pestanaEditor, pestanaLogros, lineaJugar, lineaRanking, lineaOpciones, lineaEditor, lineaLogros, false);
            }
        });

        botonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                boolean estaLogueado = prefs.getBoolean(KEY_LOGGED_IN, false);

                if (estaLogueado) {
                    controlarNavegacion(pestanaRanking, pestanaJugar, pestanaOpciones, pestanaEditor, pestanaLogros, lineaRanking, lineaJugar, lineaOpciones, lineaEditor, lineaLogros, true);
                }
                else {
                    Toast.makeText(PantallaJuego.this, "Debes iniciar sesión para ver el ranking global.", Toast.LENGTH_LONG).show();
                }
            }
        });

        botonOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlarNavegacion(pestanaOpciones, pestanaJugar, pestanaRanking, pestanaEditor, pestanaLogros, lineaOpciones, lineaJugar, lineaRanking, lineaEditor, lineaLogros, false);
            }
        });

        botonEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlarNavegacion(pestanaEditor, pestanaJugar, pestanaRanking, pestanaOpciones, pestanaLogros, lineaEditor, lineaJugar, lineaRanking, lineaOpciones, lineaLogros, false);
            }
        });

        botonLogros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlarNavegacion(pestanaLogros, pestanaJugar, pestanaRanking, pestanaOpciones, pestanaEditor, lineaLogros, lineaJugar, lineaRanking, lineaOpciones, lineaEditor, false);
            }
        });
    }

    private void jugar() {
        LinearLayout todasCategorias = findViewById(R.id.modo_todas_categorias);
        LinearLayout geografia = findViewById(R.id.modo_geografia);
        LinearLayout historia = findViewById(R.id.modo_historia);
        LinearLayout entretenimiento = findViewById(R.id.modo_entretenimiento);
        LinearLayout deportes = findViewById(R.id.modo_deportes);
        LinearLayout informatica = findViewById(R.id.modo_informatica);
        LinearLayout medicina = findViewById(R.id.modo_medicina);

        todasCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantallaJuego.this, Juego.class);
                intent.putExtra("modo", "todas");
                intent.putExtra("dificultad", dificultadSeleccionada);
                startActivity(intent);
            }
        });

        geografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantallaJuego.this, Juego.class);
                intent.putExtra("modo", "categoria");
                intent.putExtra("categoria", 1);
                intent.putExtra("dificultad", dificultadSeleccionada);
                startActivity(intent);
            }
        });

        historia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantallaJuego.this, Juego.class);
                intent.putExtra("modo", "categoria");
                intent.putExtra("categoria", 2);
                intent.putExtra("dificultad", dificultadSeleccionada);
                startActivity(intent);
            }
        });

        entretenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantallaJuego.this, Juego.class);
                intent.putExtra("modo", "categoria");
                intent.putExtra("categoria", 3);
                intent.putExtra("dificultad", dificultadSeleccionada);
                startActivity(intent);
            }
        });

        deportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantallaJuego.this, Juego.class);
                intent.putExtra("modo", "categoria");
                intent.putExtra("categoria", 4);
                intent.putExtra("dificultad", dificultadSeleccionada);
                startActivity(intent);
            }
        });

        informatica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantallaJuego.this, Juego.class);
                intent.putExtra("modo", "categoria");
                intent.putExtra("categoria", 5);
                intent.putExtra("dificultad", dificultadSeleccionada);
                startActivity(intent);
            }
        });

        medicina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantallaJuego.this, Juego.class);
                intent.putExtra("modo", "categoria");
                intent.putExtra("categoria", 6);
                intent.putExtra("dificultad", dificultadSeleccionada);
                startActivity(intent);
            }
        });
    }

    /*Funcion encargada de darnos el ranking de puntuaciones*/
    private void obtenerRanking() {
        Retrofit retro = new Retrofit();
        retro.getApi().obtenerPartidas().enqueue(new Callback<List<Partida>>() {
            @Override
            public void onResponse(Call<List<Partida>> call, Response<List<Partida>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Partida> partidasRecibidas = response.body();
                    if (partidasRecibidas.isEmpty()) {
                        // Muestra un mensaje si no hay partidas
                        // Puedes añadir un TextView en tu layout_ranking para esto, por ejemplo:
                        // TextView textoNoPartidas = findViewById(R.id.textoNoPartidas);
                        // textoNoPartidas.setVisibility(View.VISIBLE);
                        // recyclerViewRanking.setVisibility(View.GONE);
                        Toast.makeText(PantallaJuego.this, "No hay partidas registradas.", Toast.LENGTH_SHORT).show();
                        listaPartidas.clear(); // Limpia la lista si no hay datos
                    } else {
                        // Actualiza la lista del adaptador y notifica los cambios
                        listaPartidas.clear(); // Limpia la lista anterior
                        listaPartidas.addAll(partidasRecibidas); // Añade las nuevas partidas
                        adapterPartidas.notifyDataSetChanged(); // ¡Crucial para que el RecyclerView se redibuje!
                        // Si tenías un mensaje de "no partidas", asegúrate de ocultarlo aquí
                        // textoNoPartidas.setVisibility(View.GONE);
                        // recyclerViewRanking.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Manejo de errores de la API (códigos 4xx, 5xx)
                    Toast.makeText(PantallaJuego.this, "Error al cargar ranking: HTTP " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Partida>> call, Throwable t) {
                Toast.makeText(PantallaJuego.this, "Error de red al cargar ranking: " + t.getMessage(), Toast.LENGTH_LONG).show();
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

                if (isChecked) {
                    if (mp == null) {
                        mp = MediaPlayer.create(PantallaJuego.this, R.raw.musica_juego);
                        mp.setLooping(true);
                    }
                    mp.start();
                } else {
                    if (mp != null && mp.isPlaying()) {
                        mp.pause();
                    }
                }
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
        spDificultad.setSelection(0);

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
                } else {
                    nuevoIdioma = "es";
                    bandera.setImageResource(R.drawable.bandera);
                }

                cambiarIdioma(nuevoIdioma);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDificultad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dificultadSeleccionada = adapterView.getItemAtPosition(i).toString();
                dificultad = obtenerRondasPorDificultad(dificultadSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private Dificultad obtenerRondasPorDificultad(String dificultad) {
        switch (dificultad) {
            case "Fácil":
                return new Dificultad(15, 30000);

            case "Normal":
                return new Dificultad(10, 20000);

            case "Difícil":
                return new Dificultad(5, 10000);

            default:
                return new Dificultad(15, 30000);
        }
    }

    private void editor() {
        Button botonCrearPreguntas = findViewById(R.id.btn_crear);
        Button botonEditarPreguntas = findViewById(R.id.btn_editar);

        botonCrearPreguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantallaJuego.this, PantallaCrearPregunta.class);
                startActivity(intent);
            }
        });
    }

    private void logros() {
        Retrofit retro = new Retrofit();

        retro.getApi().obtenerLogros().enqueue(new Callback<List<Logro>>() {

            @Override
            public void onResponse(@NonNull Call<List<Logro>> call, @NonNull Response<List<Logro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaLogros.clear();
                    listaLogros.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Logro>> call, @NonNull Throwable t) {
                Toast.makeText(PantallaJuego.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void perfil() {
        LinearLayout perfil = findViewById(R.id.layoutCuentaOpciones);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PantallaJuego.this, ActivityAdministracionPerfil.class);
                startActivity(intent);
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

        if (mp != null) {
            mp.release();
        }
    }
}