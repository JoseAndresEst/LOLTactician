package com.example.loltactician.accesoDatos.repositorios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.loltactician.accesoDatos.modelos.Draft;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DraftRepositorio {
    private static DraftRepositorio instance;
    private FirebaseFirestore db;
    private MutableLiveData<List<Draft>> draftsUsuario = new MutableLiveData<>();

    //Constructor privado (Singleton)
    private DraftRepositorio() {
        // Obtener referencia a Firebase Firestore
        db = FirebaseFirestore.getInstance();
        draftsUsuario.setValue(new ArrayList<>());
    }

    //Método para obtener la instancia única
    public static synchronized DraftRepositorio getInstance() {
        if (instance == null) {
            instance = new DraftRepositorio();
        }
        return instance;
    }

    //Obtener referencia a la colección de drafts del usuario actual
    private CollectionReference getDraftsCollection() {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario != null) {
            return db.collection("users").document(usuario.getUid()).collection("drafts");
        }
        throw new IllegalStateException("Usuario no autenticado");
    }

    public Task<Void> guardarDraft(Draft draft) {
        CollectionReference draftsRef = getDraftsCollection();
        if (draft.getId() == null || draft.getId().isEmpty()) {
            DocumentReference newDraftRef = draftsRef.document();
            draft.setId(newDraftRef.getId());
            return newDraftRef.set(draft);
        } else {
            //Actualizar draft existente
            return draftsRef.document(draft.getId()).set(draft);
        }
    }

    public LiveData<List<Draft>> cargarDraftsUsuario(String idUsuario) {
        CollectionReference userDraftsRef = db.collection("users").document(idUsuario).collection("drafts");

        userDraftsRef.addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                //Manejar error
                return;
            }

            if (snapshots != null) {
                List<Draft> lista = new ArrayList<>();
                for (DocumentSnapshot document : snapshots.getDocuments()) {
                    Draft draft = document.toObject(Draft.class);
                    if (draft != null) {
                        lista.add(draft);
                    }
                }
                draftsUsuario.setValue(lista);
            }
        });

        return draftsUsuario;
    }

    public LiveData<List<Draft>> cargarMisDrafts() {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario != null) {
            return cargarDraftsUsuario(usuario.getUid());
        }
        return draftsUsuario; //Retorna lista vacía si no hay usuario autenticado
    }

    public Task<Void> eliminarDraft(String idDraft) {
        return getDraftsCollection().document(idDraft).delete();
    }

    public static CollectionReference obtenerReferencia() {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario != null) {
            return FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(usuario.getUid())
                    .collection("drafts");
        }
        throw new IllegalStateException("Usuario no autenticado");
    }
}
