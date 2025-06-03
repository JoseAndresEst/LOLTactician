package com.example.loltactician.accesoDatos.repositorios;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.loltactician.accesoDatos.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsuarioRepositorio {
    private static UsuarioRepositorio instance;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private MutableLiveData<Usuario> usuarioActual = new MutableLiveData<>();

    //Constructor privado (Singleton)
    private UsuarioRepositorio() {
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
    }

    //Metodo para obtener la instancia única
    public static synchronized UsuarioRepositorio getInstance() {
        if (instance == null) {
            instance = new UsuarioRepositorio();
        }
        return instance;
    }

    public void registrarUsuario(String email, String contraseña, Activity activity){
        //Crea el usuario en la base de datos de firebase y un listener para cuando la operacion se completa
        auth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                    auth.getCurrentUser().sendEmailVerification();
                    auth.signOut();
                }else{
                    Toast.makeText(activity, "La cuenta no se ha podido crear", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void iniciarSesion(String email, String password, Activity activity) {
        //Inicia sesion
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(activity, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cerrarSesion() {
        auth.signOut();
        usuarioActual.setValue(null);
    }

    public boolean hayUsuarioConectado() {
        return auth.getCurrentUser() != null;
    }


}
