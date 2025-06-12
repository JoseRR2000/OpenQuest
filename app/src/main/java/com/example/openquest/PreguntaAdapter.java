package com.example.openquest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PreguntaAdapter extends RecyclerView.Adapter<PreguntaAdapter.PreguntaViewHolder> {

    private List<Pregunta> listaPreguntas;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditarClick(Pregunta pregunta);
    }

    public PreguntaAdapter(List<Pregunta> listaPreguntas, OnItemClickListener listener) {
        this.listaPreguntas = listaPreguntas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PreguntaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pregunta, parent, false);
        return new PreguntaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PreguntaViewHolder holder, int position) {
        Pregunta preguntaActual = listaPreguntas.get(position);
        holder.textViewPregunta.setText(preguntaActual.getPregunta());

        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(preguntaActual);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPreguntas.size();
    }

    private String getCategoriaTexto(String categoriaId) {
        // Aunque ya hicimos la comprobación en onBindViewHolder,
        // este método debería estar diseñado para recibir un String que NO sea null
        // Si por alguna razón llega null aquí (lo cual no debería con la comprobación anterior),
        // es una situación de error interna.
        if (categoriaId == null) {
            return "ERROR: Categoría nula"; // Esto es más para depuración
        }

        // Tu lógica actual para mapear el ID de categoría a un texto legible
        switch (categoriaId) {
            case "1": return "Geografía";
            case "2": return "Historia";
            case "3": return "Entretenimiento";
            case "4": return "Deportes";
            case "5": return "Informática";
            case "6": return "Medicina";
            default: return "Desconocida";
        }
    }

    public static class PreguntaViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewPregunta;
        public Button btnEditar;

        public PreguntaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPregunta = itemView.findViewById(R.id.text_view_pregunta);
            btnEditar = itemView.findViewById(R.id.btn_editar_pregunta_item);
        }
    }

    public void updateData(List<Pregunta> nuevasPreguntas) {
        this.listaPreguntas = nuevasPreguntas;
        notifyDataSetChanged();
    }
}