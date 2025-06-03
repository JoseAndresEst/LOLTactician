package com.example.loltactician.interfaz.actividades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loltactician.R;
import com.example.loltactician.accesoDatos.repositorios.CampeonRepositorio;
import com.example.loltactician.interfaz.adaptadores.CampeonAdapter;
import com.example.loltactician.accesoDatos.modelos.Campeon;

import java.util.ArrayList;
import java.util.List;

public class TestCampeonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CampeonAdapter adapter;
    private List<Campeon> listaCampeones;
    private CampeonRepositorio repositorio;
    private ProgressBar progressBar;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_campeon);

        recyclerView = findViewById(R.id.recycler_campeones);
        progressBar = findViewById(R.id.progress_bar);
        tvError = findViewById(R.id.tv_error);

        recyclerView.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns(this)));

        //Inicializar lista vacía y adaptador
        listaCampeones = new ArrayList<>();
        adapter = new CampeonAdapter(this, listaCampeones, new CampeonAdapter.OnCampeonClickListener() {
            @Override
            public void onCampeonClick(Campeon campeon) {
                //Aquí maneja el click del campeón
                Intent intent = new Intent(TestCampeonActivity.this, DetallesCampeonActivity.class);
                intent.putExtra("CHAMPION_ID", campeon.getId());
                startActivity(intent);
            }
        });

        // Asignar adaptador al RecyclerView
        recyclerView.setAdapter(adapter);

        // Obtener la instancia del repositorio
        repositorio = CampeonRepositorio.getInstance();

        // Observar cambios en la lista de campeones
        repositorio.getCampeones().observe(this, nuevaLista -> {
            if (nuevaLista != null) {
                listaCampeones = nuevaLista;
                adapter.actualizarCampeones(nuevaLista);
            }
        });

        // Observar estado de carga
        repositorio.getCargando().observe(this, cargando -> {
            progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
        });

        // Observar errores
        repositorio.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                tvError.setText(error);
                tvError.setVisibility(View.VISIBLE);
            } else {
                tvError.setVisibility(View.GONE);
            }
        });

        // Cargar datos de campeones (opcional si ya se hace en el constructor del repositorio)
        repositorio.cargarCampeones();
    }

    private int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        //Tamaño para cada elemento
        int itemWidth = 70;
        //Calcula cuántos elementos caben en la pantalla
        int noOfColumns = Math.max(1, (int) (dpWidth / itemWidth));
        return noOfColumns;
    }
}