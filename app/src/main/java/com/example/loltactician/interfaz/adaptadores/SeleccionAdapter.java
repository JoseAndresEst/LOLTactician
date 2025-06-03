package com.example.loltactician.interfaz.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loltactician.R;
import com.example.loltactician.accesoDatos.repositorios.CampeonRepositorio;
import com.example.loltactician.accesoDatos.modelos.Seleccion;
import com.example.loltactician.accesoDatos.modelos.Campeon;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SeleccionAdapter extends RecyclerView.Adapter<SeleccionAdapter.SeleccionViewHolder> {

    private List<Seleccion> selecciones = new ArrayList<>();
    private CampeonRepositorio campeonRepositorio = CampeonRepositorio.getInstance();

    public void actualizarSelecciones(List<Seleccion> nuevasSelecciones) {
        this.selecciones = nuevasSelecciones;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SeleccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seleccion, parent, false);
        return new SeleccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeleccionViewHolder holder, int position) {
        Seleccion seleccion = selecciones.get(position);
        Campeon campeon = campeonRepositorio.getCampeonById(seleccion.getIdCampeon());

        if (campeon != null) {
            //Mostrar imagen del campeón
            Glide.with(holder.itemView.getContext())
                    .load(campeon.getImagenUrl())
                    .placeholder(R.drawable.placeholder_champion)
                    .into(holder.campeonImageView);

            //Mostrar nombre del campeón
            holder.nombreCampeonTextView.setText(campeon.getNombre());

            //Configurar el color o estilo según el tipo de selección (BAN/PICK)
            if (seleccion.getTipo() == Seleccion.TipoSeleccion.BAN) {
                holder.itemView.setAlpha(0.5f); // Campeones baneados se muestran más transparentes
                holder.overlayBanView.setVisibility(View.VISIBLE); // Mostrar una "X" o símbolo de ban
            } else {
                holder.itemView.setAlpha(1.0f);
                holder.overlayBanView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return selecciones.size();
    }

    static class SeleccionViewHolder extends RecyclerView.ViewHolder {
        ImageView campeonImageView;
        TextView nombreCampeonTextView;
        View overlayBanView; // Vista para mostrar un indicador de BAN (como una X)

        public SeleccionViewHolder(@NonNull View itemView) {
            super(itemView);
            campeonImageView = itemView.findViewById(R.id.campeon_imagen);
            nombreCampeonTextView = itemView.findViewById(R.id.campeon_nombre);
            overlayBanView = itemView.findViewById(R.id.overlay_ban);
        }
    }
}
