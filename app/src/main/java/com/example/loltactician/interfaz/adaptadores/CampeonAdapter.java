package com.example.loltactician.interfaz.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loltactician.R;
import com.example.loltactician.accesoDatos.modelos.Campeon;

import java.util.ArrayList;
import java.util.List;


public class CampeonAdapter extends RecyclerView.Adapter<CampeonAdapter.CampeonViewHolder> {

    private List<Campeon> campeones = new ArrayList<>();
    private OnCampeonClickListener listener;
    private boolean seleccionHabilitada = true;
    private Context context;

    public CampeonAdapter(Context context, List<Campeon> campeones, OnCampeonClickListener listener) {
        this.context = context;
        this.campeones = campeones;
        this.listener = listener;
    }

    //Interfaz para el listener de clics
    public interface OnCampeonClickListener {
        void onCampeonClick(Campeon campeon);
    }

    public void setOnCampeonClickListener(OnCampeonClickListener listener) {
        this.listener = listener;
    }

    public void actualizarCampeones(List<Campeon> nuevaLista) {
        this.campeones = nuevaLista;
        notifyDataSetChanged();
    }

    //Habilitar/deshabilitar la selección de campeones
    public void setSeleccionHabilitada(boolean habilitada) {
        this.seleccionHabilitada = habilitada;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CampeonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aquí debes tener tu propio layout para el ítem de campeón
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campeon, parent, false);
        return new CampeonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampeonViewHolder holder, int position) {
        if (campeones == null || position >= campeones.size()) {
            return;
        }

        Campeon campeon = campeones.get(position);


        if (holder.nombreTextView != null && campeon != null) {
            holder.nombreTextView.setText(campeon.getNombre());
        }

        //Verificar que la imagen no sea null antes de cargarla
        if (holder.imagenCampeon != null && campeon != null) {
            Glide.with(holder.itemView.getContext())
                    .load(campeon.getImagenUrl())
                    .placeholder(R.drawable.placeholder_campeon)
                    .into(holder.imagenCampeon);
        }

        //Configurar el clic en el ítem
        holder.itemView.setOnClickListener(v -> {
            if (seleccionHabilitada && listener != null) {
                listener.onCampeonClick(campeon);
            }
        });

        //Cambiar la apariencia si la selección está deshabilitada
        if (seleccionHabilitada) {
            holder.itemView.setAlpha(1.0f);
        } else {
            holder.itemView.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {
        return campeones.size();
    }

    static class CampeonViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenCampeon;
        TextView nombreTextView;

        public CampeonViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenCampeon = itemView.findViewById(R.id.iv_icono_campeon);
            nombreTextView = itemView.findViewById(R.id.tv_nombre_campeon);
        }
    }
}

