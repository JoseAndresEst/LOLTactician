package com.example.loltactician.interfaz.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loltactician.accesoDatos.repositorios.UsuarioRepositorio;
import com.example.loltactician.accesoDatos.modelos.Usuario;
import com.example.loltactician.R;
import com.google.firebase.FirebaseApp;

public class CrearCuentaActivity extends AppCompatActivity {
    EditText emailET, contraseñaET, nombreUsuarioET;
    Button botonCrearCuenta;
    TextView iniciarSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        emailET=findViewById(R.id.editTextEmail);
        contraseñaET=findViewById(R.id.editTextContraseña);
        nombreUsuarioET=findViewById(R.id.editTextNombreUsuario);
        botonCrearCuenta=findViewById(R.id.botonCrearCuenta);
        iniciarSesion=findViewById(R.id.textViewMensaje2);

        botonCrearCuenta.setOnClickListener(v -> crearCuenta());
        iniciarSesion.setOnClickListener(v -> startActivity(new Intent(CrearCuentaActivity.this, InicioSesionActivity.class)));
    }

    private void crearCuenta(){
        //Recoge los datos y los pasa a otra funcion para comprobar que son correctos
        String email=emailET.getText().toString();
        String contraseña=contraseñaET.getText().toString();
        String nombreUsuario=nombreUsuarioET.getText().toString();

        //Si los datos son validos llama a otra funcion que crea la cuenta en la base de datos de firebase
        if(email.isEmpty() || contraseña.isEmpty() || nombreUsuario.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        }else if(validacionDatos(email, contraseña, nombreUsuario)){
            botonCrearCuenta.setEnabled(false);

            UsuarioRepositorio.getInstance().registrarUsuario(email, contraseña, CrearCuentaActivity.this);
            startActivity(new Intent(CrearCuentaActivity.this, InicioSesionActivity.class));
        }
    }

    private boolean validacionDatos(String email, String contraseña, String nombreusuario){
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

        if(nombreusuario.length()<4){
            nombreUsuarioET.setError("Longitud no válida");
            return false;
        }

        return true;
    }
}