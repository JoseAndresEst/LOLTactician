package com.example.loltactician.interfaz.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loltactician.R;
import com.example.loltactician.accesoDatos.repositorios.UsuarioRepositorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    CardView campeones, draft;
    ImageView cerrarsesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        campeones=findViewById(R.id.cardCampeones);
        draft=findViewById(R.id.cardDraft);
        cerrarsesion=findViewById(R.id.cerrar_sesion);


        campeones.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TestCampeonActivity.class)));
        draft.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SimuladorDraftActivity.class)));
        cerrarsesion.setOnClickListener(v -> {
            UsuarioRepositorio.getInstance().cerrarSesion();
            startActivity(new Intent(MainActivity.this,InicioSesionActivity.class));
            finish();
        });
    }
}