package com.example.loltactician.interfaz.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loltactician.R;
import com.example.loltactician.accesoDatos.repositorios.UsuarioRepositorio;

public class PantallaCargaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);

        //Esto es para que se vea la pantalla 2 segundos antes de que redirija a otras actividades
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Coge el usuario y si esta vacio es que no ha iniciado sesion (lo lleva a la pantalla de inicio de sesion), si no lo lleva al mainActivity
                if (!UsuarioRepositorio.getInstance().hayUsuarioConectado()){
                    startActivity(new Intent(PantallaCargaActivity.this, InicioSesionActivity.class));
                }else{
                    startActivity(new Intent(PantallaCargaActivity.this, MainActivity.class));
                }
            }
        }, 2000);
    }
}