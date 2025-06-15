package com.example.openquest;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PartidaAdapter extends RecyclerView.Adapter<PartidaAdapter.ViewHolder> {
    private List<Partida> partidas;
    private Context context;
    private String idiomaGuardado;

    // Constructor que acepta el Context
    public PartidaAdapter(List<Partida> partidas, Context context) {
        this.partidas = partidas;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idiomaGuardado = prefs.getString("MisPreferencias", "es");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textoDificultad, textoDetalle;

        public ViewHolder(View view) {
            super(view);
            textoDificultad = view.findViewById(R.id.textoDificultad);
            textoDetalle = view.findViewById(R.id.textoDetalle);
        }
    }

    @NonNull
    @Override
    public PartidaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_partida, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartidaAdapter.ViewHolder holder, int position) {
        Partida p = partidas.get(position);

        String dificultadTexto;
        switch (p.getDificultad()) {
            case "Fácil":
                dificultadTexto = context.getString(R.string.easy);
                break;
            case "Normal":
                dificultadTexto = context.getString(R.string.normal);
                break;
            case "Difícil":
                dificultadTexto = context.getString(R.string.hard);
                break;
            default:
                dificultadTexto = p.getDificultad();
                break;
        }
        holder.textoDificultad.setText(context.getString(R.string.difficulty) + ": " + dificultadTexto);
        // ---------------------------------------------------------------


        String fechaFormateada;
        if (p.getFecha() != null) {
            Locale currentLocale = new Locale(idiomaGuardado);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", currentLocale);
            fechaFormateada = formatter.format(p.getFecha());
        } else {
            fechaFormateada = context.getString(R.string.no_date);
        }

        holder.textoDetalle.setText(context.getString(R.string.user_ranking) + ": " + p.getJugador() + " | " + context.getString(R.string.score_ranking) + ": " + p.getPuntuacion() + " | " + context.getString(R.string.rounds_ranking) + ": " + p.getRondasJugadas() + "\n" + context.getString(R.string.date_ranking) + ": " + fechaFormateada);
    }

    @Override
    public int getItemCount() {
        return partidas.size();
    }
}