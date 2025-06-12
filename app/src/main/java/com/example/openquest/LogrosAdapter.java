package com.example.openquest;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LogrosAdapter extends RecyclerView.Adapter<LogrosAdapter.ViewHolder> {
    private final List<Logro> logros;

    public LogrosAdapter(List<Logro> logros) {
        this.logros = logros;
    }

    @NonNull
    @Override
    public LogrosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logro, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion;
        ImageView icono;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreLogro);
            descripcion = itemView.findViewById(R.id.descripcionLogro);
            icono = itemView.findViewById(R.id.iconoLogro);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LogrosAdapter.ViewHolder holder, int position) {
        Logro logro = logros.get(position);
        holder.nombre.setText(logro.getNombre());
        holder.descripcion.setText(logro.getDescripcion());

        if (logro.isConseguido()) {
            holder.itemView.setAlpha(1f);
            holder.icono.setColorFilter(Color.parseColor("#FFD700"));
        } else {
            holder.itemView.setAlpha(0.5f);
            holder.icono.setColorFilter(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return logros.size();
    }
}
