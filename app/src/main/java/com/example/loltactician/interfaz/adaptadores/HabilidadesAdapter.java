package com.example.loltactician.interfaz.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loltactician.R;
import com.example.loltactician.accesoDatos.modelos.*;

import java.util.List;

public class HabilidadesAdapter extends RecyclerView.Adapter<HabilidadesAdapter.HabilidadViewHolder> {

    private List<Habilidad> habilidades;

    public HabilidadesAdapter(List<Habilidad> habilidades) {
        this.habilidades = habilidades;
    }

    public void actualizarHabilidades(List<Habilidad> habilidades) {
        this.habilidades = habilidades;
        notifyDataSetChanged();
    }

    @Override
    public HabilidadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_habilidad, parent, false);
        return new HabilidadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HabilidadViewHolder holder, int position) {
        holder.bind(habilidades.get(position));
    }

    @Override
    public int getItemCount() {
        return habilidades.size();
    }

    static class HabilidadViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTecla, tvNombre, tvDescripcion;
        private ImageView ivHabilidad;

        public HabilidadViewHolder(View itemView) {
            super(itemView);
            tvTecla = itemView.findViewById(R.id.tv_tecla);
            tvNombre = itemView.findViewById(R.id.tv_nombre_habilidad);
            tvDescripcion = itemView.findViewById(R.id.tv_descripcion_habilidad);
            ivHabilidad = itemView.findViewById(R.id.iv_habilidad);
        }

        public void bind(Habilidad habilidad) {
            tvTecla.setText(habilidad.getTecla());
            tvNombre.setText(habilidad.getNombre());
            tvDescripcion.setText(habilidad.getDescripcion());

            // Cargar imagen con Glide o Picasso
            Glide.with(itemView.getContext())
                    .load(habilidad.getImagenUrl())
                    .into(ivHabilidad);
        }
    }
}
