package com.example.loltactician.interfaz.actividades;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.loltactician.R;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;


import com.example.loltactician.accesoDatos.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.example.loltactician.accesoDatos.repositorios.UsuarioRepositorio;

public class InicioSesionActivity extends AppCompatActivity {
    EditText emailET, contraseñaET;
    Button botonIniciarSesion;
    TextView crearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        emailET=findViewById(R.id.editTextEmail);
        contraseñaET=findViewById(R.id.editTextContraseña);
        botonIniciarSesion=findViewById(R.id.botonIniciarSesion);
        crearCuenta=findViewById(R.id.textViewMensaje2);

        botonIniciarSesion.setOnClickListener(v -> inicioSesion());
        //Si se pulsa crear cuenta se va a la actividad de CrearCuenta
        crearCuenta.setOnClickListener(v -> startActivity(new Intent(InicioSesionActivity.this, CrearCuentaActivity.class)));
    }

    private void inicioSesion(){
        //Recoge los datos y los pasa a otra funcion para comprobar que son correctos
        String email=emailET.getText().toString();
        String contraseña=contraseñaET.getText().toString();

        //Si los datos son validos llama a otra funcion que inicia sesion comparando los datos con los de la base de datos de firebase
        if(email.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        }else if(validacionDatos(email, contraseña)){
            botonIniciarSesion.setEnabled(false);

            UsuarioRepositorio.getInstance().iniciarSesion(email, contraseña, InicioSesionActivity.this);
            startActivity(new Intent(InicioSesionActivity.this, MainActivity.class));
        }
    }

    private boolean validacionDatos(String email, String contraseña){
        //Comprueba que el email que se introduce sea valido, mediante la clase Patterns, que tiene implementado directamente la comprobacion con EMAIL.ADDRESS
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailET.setError("El correo electronico no es valido");
            return false;
        }

        //Comprobacion de que la contraseña es de una longitud de minimo 4 caracteres
        if(contraseña.length()<4){
            contraseñaET.setError("Longitud no valida");
            return false;
        }

        return true;
    }
}