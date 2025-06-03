package com.example.loltactician.interfaz.actividades;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loltactician.APIs.RiotApiClient;
import com.example.loltactician.R;
import com.example.loltactician.accesoDatos.modelos.ChampionDetails;
import com.example.loltactician.interfaz.adaptadores.HabilidadesAdapter;

import java.util.ArrayList;

public class DetallesCampeonActivity extends AppCompatActivity {

    private TextView tvNombre, tvTitulo, tvHistoria;
    private ImageView ivCampeon;
    private RecyclerView rvHabilidades;
    private HabilidadesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_campeon);

        //Inicializar vistas
        tvNombre = findViewById(R.id.tv_nombre);
        tvTitulo = findViewById(R.id.tv_titulo);
        tvHistoria = findViewById(R.id.tv_historia);
        ivCampeon = findViewById(R.id.iv_campeon);
        rvHabilidades = findViewById(R.id.rv_habilidades);

        rvHabilidades.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HabilidadesAdapter(new ArrayList<>());
        rvHabilidades.setAdapter(adapter);

        //Obtener el ID del campeón de los extras
        String championId = getIntent().getStringExtra("CHAMPION_ID");
        if (championId != null) {
            cargarDetallesCampeon(championId);
        } else {
            Toast.makeText(this, "Error: ID de campeón no proporcionado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarDetallesCampeon(String championId) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando detalles del campeón...");
        progressDialog.show();

        RiotApiClient.getChampionDetails(championId, new RiotApiClient.ChampionDetailCallback() {
            @Override
            public void onSuccess(ChampionDetails championDetails) {
                progressDialog.dismiss();

                //Actualizar la interfaz con los detalles
                tvNombre.setText(championDetails.getNombre());
                tvTitulo.setText(championDetails.getTitulo());
                tvHistoria.setText(championDetails.getHistoria());

                //Cargar imagen con Glide
                Glide.with(DetallesCampeonActivity.this)
                        .load(championDetails.getImagenUrl())
                        .into(ivCampeon);

                //Actualizar el adaptador de habilidades
                adapter.actualizarHabilidades(championDetails.getHabilidades());
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                Toast.makeText(DetallesCampeonActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}