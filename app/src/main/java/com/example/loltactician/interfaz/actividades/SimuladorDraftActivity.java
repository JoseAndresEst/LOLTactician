package com.example.loltactician.interfaz.actividades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loltactician.R;
import com.example.loltactician.accesoDatos.modelos.Campeon;
import com.example.loltactician.accesoDatos.modelos.Draft;
import com.example.loltactician.accesoDatos.modelos.Seleccion;
import com.example.loltactician.accesoDatos.repositorios.CampeonRepositorio;
import com.example.loltactician.interfaz.adaptadores.CampeonAdapter;
import com.example.loltactician.controladores.ControladorDraft;
import com.example.loltactician.interfaz.adaptadores.SeleccionAdapter;


import java.util.ArrayList;
import java.util.List;

public class SimuladorDraftActivity extends AppCompatActivity {

    private RecyclerView campeonesRecyclerView;
    private CampeonAdapter campeonAdapter;
    private ControladorDraft controladorDraft;
    List<Campeon> listaCampeones;

    private TextView estadoDraftTextView;
    private TextView turnoTextView;
    private TextView equipoActualTextView;
    private Button btnDeshacer;
    private Button btnFinalizar;

    private RecyclerView bansEquipoARecyclerView;
    private RecyclerView bansEquipoBRecyclerView;
    private RecyclerView picksEquipoARecyclerView;
    private RecyclerView picksEquipoBRecyclerView;

    private SeleccionAdapter bansEquipoAAdapter;
    private SeleccionAdapter bansEquipoBAdapter;
    private SeleccionAdapter picksEquipoAAdapter;
    private SeleccionAdapter picksEquipoBAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulador_draft);

        //Inicializar el controlador de draft
        controladorDraft = new ControladorDraft("Mi Draft Simulado", "usuario_actual");

        initViews();

        //Configurar RecyclerView de campeones existente
        setupCampeonesRecyclerView();

        //Configurar RecyclerViews para bans y picks
        setupSeleccionesRecyclerViews();

        //Observar cambios en el estado del draft
        observeDraftState();

        // Setup botones
        setupButtons();
    }

    private void initViews() {
        campeonesRecyclerView = findViewById(R.id.recycler_campeones_seleccion);
        estadoDraftTextView = findViewById(R.id.estado_draft_text);
        turnoTextView = findViewById(R.id.turno_text);
        equipoActualTextView = findViewById(R.id.equipo_actual_text);
        btnDeshacer = findViewById(R.id.btn_deshacer);
        btnFinalizar = findViewById(R.id.btn_finalizar);

        bansEquipoARecyclerView = findViewById(R.id.bans_equipo_a_recyclerview);
        bansEquipoBRecyclerView = findViewById(R.id.bans_equipo_b_recyclerview);
        picksEquipoARecyclerView = findViewById(R.id.picks_equipo_a_recyclerview);
        picksEquipoBRecyclerView = findViewById(R.id.picks_equipo_b_recyclerview);
    }

    private void setupCampeonesRecyclerView() {
        campeonesRecyclerView.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns(this)));

        listaCampeones = new ArrayList<>();
        campeonAdapter = new CampeonAdapter(this, listaCampeones, new CampeonAdapter.OnCampeonClickListener() {
            @Override
            public void onCampeonClick(Campeon campeon) {
                Toast.makeText(SimuladorDraftActivity.this,
                        "Campeón seleccionado: " + campeon.getNombre(),
                        Toast.LENGTH_SHORT).show();
                realizarSeleccionDeCampeon(campeon);
            }
        });

        campeonesRecyclerView.setAdapter(campeonAdapter);
        CampeonRepositorio repositorio;
        repositorio = CampeonRepositorio.getInstance();

        // Observar cambios en la lista de campeones
        repositorio.getCampeones().observe(this, nuevaLista -> {
            if (nuevaLista != null) {
                listaCampeones = nuevaLista;
                campeonAdapter.actualizarCampeones(nuevaLista);
            }
        });

        //Cargar datos de campeones
        repositorio.cargarCampeones();
    }

    private void realizarSeleccionDeCampeon(Campeon campeon) {
        ControladorDraft.EstadoDraft estadoActual = controladorDraft.getEstadoActual().getValue();

        boolean seleccionExitosa = false;

        if (estadoActual == ControladorDraft.EstadoDraft.FASE_BAN) {
            seleccionExitosa = controladorDraft.realizarBan(campeon.getId());
        } else if (estadoActual == ControladorDraft.EstadoDraft.FASE_PICK) {
            seleccionExitosa = controladorDraft.realizarPick(campeon.getId());
        }

        if (seleccionExitosa) {
            // Actualizar el adaptador de campeones para reflejar el cambio
            campeonAdapter.actualizarCampeones(controladorDraft.getCampeonesDisponibles());
        } else {
            Toast.makeText(this, "No se pudo seleccionar este campeón", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSeleccionesRecyclerViews() {
        bansEquipoAAdapter = new SeleccionAdapter();
        bansEquipoBAdapter = new SeleccionAdapter();
        picksEquipoAAdapter = new SeleccionAdapter();
        picksEquipoBAdapter = new SeleccionAdapter();

        LinearLayoutManager layoutManagerBansA = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerBansB = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerPicksA = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerPicksB = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        bansEquipoARecyclerView.setLayoutManager(layoutManagerBansA);
        bansEquipoBRecyclerView.setLayoutManager(layoutManagerBansB);
        picksEquipoARecyclerView.setLayoutManager(layoutManagerPicksA);
        picksEquipoBRecyclerView.setLayoutManager(layoutManagerPicksB);

        bansEquipoARecyclerView.setAdapter(bansEquipoAAdapter);
        bansEquipoBRecyclerView.setAdapter(bansEquipoBAdapter);
        picksEquipoARecyclerView.setAdapter(picksEquipoAAdapter);
        picksEquipoBRecyclerView.setAdapter(picksEquipoBAdapter);
    }

    private void observeDraftState() {
        //Observar cambios en el estado del draft
        controladorDraft.getEstadoActual().observe(this, estadoDraft -> {
            actualizarUIEstadoDraft(estadoDraft);
        });

        //Observar cambios en el turno
        controladorDraft.getTurnoActual().observe(this, turno -> {
            turnoTextView.setText("Turno: " + (turno + 1));
        });

        //Observar cambios en el equipo actual
        controladorDraft.getEsEquipoAzul().observe(this, esEquipoAzul -> {
            String equipo = esEquipoAzul ? "Equipo Azul" : "Equipo Rojo";
            equipoActualTextView.setText("Turno de: " + equipo);

            // Cambiar el color de fondo o algún indicador visual
            if (esEquipoAzul) {
                equipoActualTextView.setBackgroundColor(getResources().getColor(R.color.equipo_azul));
            } else {
                equipoActualTextView.setBackgroundColor(getResources().getColor(R.color.equipo_rojo));
            }
        });

        // Observar cambios en las selecciones
        controladorDraft.getSelecciones().observe(this, selecciones -> {
            actualizarUISelecciones(selecciones);
        });
    }

    private void actualizarUIEstadoDraft(ControladorDraft.EstadoDraft estado) {
        switch (estado) {
            case FASE_BAN:
                estadoDraftTextView.setText("FASE DE BANEOS");
                break;
            case FASE_PICK:
                estadoDraftTextView.setText("FASE DE SELECCIÓN");
                break;
            case FINALIZADO:
                estadoDraftTextView.setText("DRAFT FINALIZADO");
                // Deshabilitar selección de campeones
                campeonAdapter.setSeleccionHabilitada(false);
                // Mostrar algún mensaje o acción adicional
                Toast.makeText(this, "¡Draft finalizado!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void actualizarUISelecciones(List<Seleccion> selecciones) {
        List<Seleccion> bansA = new ArrayList<>();
        List<Seleccion> bansB = new ArrayList<>();
        List<Seleccion> picksA = new ArrayList<>();
        List<Seleccion> picksB = new ArrayList<>();

        //Clasificar las selecciones según tipo y equipo
        for (Seleccion seleccion : selecciones) {
            if (seleccion.getTipo() == Seleccion.TipoSeleccion.BAN) {
                if (seleccion.getEquipo() == Seleccion.Equipo.EQUIPO_A) {
                    bansA.add(seleccion);
                } else {
                    bansB.add(seleccion);
                }
            } else { // PICK
                if (seleccion.getEquipo() == Seleccion.Equipo.EQUIPO_A) {
                    picksA.add(seleccion);
                } else {
                    picksB.add(seleccion);
                }
            }
        }

        //Actualizar los adaptadores
        bansEquipoAAdapter.actualizarSelecciones(bansA);
        bansEquipoBAdapter.actualizarSelecciones(bansB);
        picksEquipoAAdapter.actualizarSelecciones(picksA);
        picksEquipoBAdapter.actualizarSelecciones(picksB);
    }

    private void setupButtons() {
        btnDeshacer.setOnClickListener(v -> {
            boolean deshecho = controladorDraft.deshacerUltimaSeleccion();
            if (deshecho) {
                campeonAdapter.actualizarCampeones(controladorDraft.getCampeonesDisponibles());
            } else {
                Toast.makeText(this, "No hay selecciones para deshacer", Toast.LENGTH_SHORT).show();
            }
        });

        btnFinalizar.setOnClickListener(v -> {
            Draft draftFinalizado = controladorDraft.finalizarDraft();

            // Mostrar un mensaje de éxito
            Toast.makeText(this, "Draft guardado correctamente", Toast.LENGTH_SHORT).show();

            // Opcional: navegar a otra pantalla o mostrar un resumen
            mostrarResumenDraft(draftFinalizado);
        });
    }

    private void mostrarResumenDraft(Draft draft) {
        //Iniciar la actividad de resumen
        Intent intent = DraftResumenActivity.createIntent(this, draft);
        startActivity(intent);
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