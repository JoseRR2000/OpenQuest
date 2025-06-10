package com.example.openquest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PartidaAdapter extends RecyclerView.Adapter<PartidaAdapter.ViewHolder> {
    private List<Partida> partidas;

    public PartidaAdapter(List<Partida> partidas) {
        this.partidas = partidas;
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

        holder.textoDificultad.setText("Dificultad: " + p.getDificultad());

        String fechaFormateada;
        if (p.getFecha() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            fechaFormateada = formatter.format(p.getFecha());
        } else {
            fechaFormateada = "Fecha no disponible";
        }

        // Include the formatted date in textoDetalle
        holder.textoDetalle.setText("Puntaje: " + p.getPuntuacion() + " | Rondas: " + p.getRondasJugadas() + "\nFecha: " + fechaFormateada);
    }

    @Override
    public int getItemCount() {
        return partidas.size();
    }
}

