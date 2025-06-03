package com.example.loltactician.controladores;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.loltactician.accesoDatos.modelos.Campeon;
import com.example.loltactician.accesoDatos.modelos.Draft;
import com.example.loltactician.accesoDatos.modelos.Seleccion;
import com.example.loltactician.accesoDatos.repositorios.CampeonRepositorio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControladorDraft {
    private static final int TOTAL_PICKS_POR_EQUIPO = 5;
    private static final int TOTAL_BANS_POR_EQUIPO = 5;

    //Secuencia estándar de draft competitivo (alternando equipos)
    private static final boolean[] SECUENCIA_BAN = {
            true, false, true, false, true, false, true, false, true, false
    };

    private static final boolean[] SECUENCIA_PICK = {
            true, false, false, true, true, false, false, true, true, false
    };

    //Estados posibles del draft
    public enum EstadoDraft {
        FASE_BAN,
        FASE_PICK,
        FINALIZADO
    }

    private Draft draftActual;
    private MutableLiveData<EstadoDraft> estadoActual = new MutableLiveData<>(EstadoDraft.FASE_BAN);
    private MutableLiveData<Integer> turnoActual = new MutableLiveData<>(0);
    private MutableLiveData<Boolean> esEquipoAzul = new MutableLiveData<>(true);
    private MutableLiveData<List<Seleccion>> seleccionesLiveData = new MutableLiveData<>();

    private List<String> campeonesProhibidos = new ArrayList<>();
    private CampeonRepositorio campeonRepositorio;

    public ControladorDraft(String nombreDraft, String idUsuario) {
        this.draftActual = new Draft();
        this.draftActual.setIdUsuario(idUsuario);
        this.draftActual.setNombreDraft(nombreDraft);
        this.draftActual.setFechaCreacion(new Date());

        this.campeonRepositorio = CampeonRepositorio.getInstance();

        List<Seleccion> seleccionesVacias = new ArrayList<>();
        seleccionesLiveData.setValue(seleccionesVacias);
    }

    public boolean realizarBan(String idCampeon) {
        if (estadoActual.getValue() != EstadoDraft.FASE_BAN) {
            return false;
        }

        int indexTurno = turnoActual.getValue();

        if (indexTurno >= SECUENCIA_BAN.length) {
            // Cambiamos a fase de picks
            estadoActual.setValue(EstadoDraft.FASE_PICK);
            turnoActual.setValue(0);
            esEquipoAzul.setValue(true);
            return false;
        }

        // Verificar que el campeón no esté ya prohibido
        if (campeonesProhibidos.contains(idCampeon)) {
            return false;
        }

        // Obtener el campeón a prohibir
        Campeon campeon = campeonRepositorio.getCampeonById(idCampeon);
        if (campeon == null) {
            return false;
        }

        // Crear nueva selección (ban)
        Seleccion nuevaSeleccion = new Seleccion();
        nuevaSeleccion.setTipo(Seleccion.TipoSeleccion.BAN);
        nuevaSeleccion.setEquipo(esEquipoAzul.getValue() ? Seleccion.Equipo.EQUIPO_A : Seleccion.Equipo.EQUIPO_B);
        nuevaSeleccion.setOrdenSeleccion(indexTurno);
        nuevaSeleccion.setIdCampeon(idCampeon);

        // Actualizar el draft actual
        List<Seleccion> seleccionesActuales = new ArrayList<>(seleccionesLiveData.getValue());
        seleccionesActuales.add(nuevaSeleccion);
        seleccionesLiveData.setValue(seleccionesActuales);
        draftActual.setSelecciones(seleccionesActuales);

        // Agregar a lista de prohibidos
        campeonesProhibidos.add(idCampeon);

        // Avanzar al siguiente turno
        turnoActual.setValue(indexTurno + 1);
        esEquipoAzul.setValue(SECUENCIA_BAN[Math.min(indexTurno + 1, SECUENCIA_BAN.length - 1)]);

        // Verificar si hemos terminado la fase de bans
        if (indexTurno + 1 >= SECUENCIA_BAN.length) {
            estadoActual.setValue(EstadoDraft.FASE_PICK);
            turnoActual.setValue(0);
            esEquipoAzul.setValue(SECUENCIA_PICK[0]);
        }

        return true;
    }

    public boolean realizarPick(String idCampeon) {
        if (estadoActual.getValue() != EstadoDraft.FASE_PICK) {
            return false;
        }

        int indexTurno = turnoActual.getValue();

        if (indexTurno >= SECUENCIA_PICK.length) {
            //El draft ha finalizado
            estadoActual.setValue(EstadoDraft.FINALIZADO);
            return false;
        }

        // Verificar que el campeón no esté ya prohibido o seleccionado
        if (campeonesProhibidos.contains(idCampeon)) {
            return false;
        }

        // Obtener el campeón a seleccionar
        Campeon campeon = campeonRepositorio.getCampeonById(idCampeon);
        if (campeon == null) {
            return false;
        }

        //Crear nueva selección (pick)
        Seleccion nuevaSeleccion = new Seleccion();
        nuevaSeleccion.setTipo(Seleccion.TipoSeleccion.PICK);
        nuevaSeleccion.setEquipo(esEquipoAzul.getValue() ? Seleccion.Equipo.EQUIPO_A : Seleccion.Equipo.EQUIPO_B);
        nuevaSeleccion.setOrdenSeleccion(indexTurno);
        nuevaSeleccion.setIdCampeon(idCampeon);

        //Actualizar el draft actual
        List<Seleccion> seleccionesActuales = new ArrayList<>(seleccionesLiveData.getValue());
        seleccionesActuales.add(nuevaSeleccion);
        seleccionesLiveData.setValue(seleccionesActuales);
        draftActual.setSelecciones(seleccionesActuales);

        //Agregar a lista de prohibidos
        campeonesProhibidos.add(idCampeon);

        // Avanzar al siguiente turno
        turnoActual.setValue(indexTurno + 1);
        if (indexTurno + 1 < SECUENCIA_PICK.length) {
            esEquipoAzul.setValue(SECUENCIA_PICK[indexTurno + 1]);
        }

        //Verificar si hemos terminado la fase de picks
        if (indexTurno + 1 >= SECUENCIA_PICK.length) {
            estadoActual.setValue(EstadoDraft.FINALIZADO);
        }

        return true;
    }

    public boolean deshacerUltimaSeleccion() {
        List<Seleccion> seleccionesActuales = seleccionesLiveData.getValue();
        if (seleccionesActuales == null || seleccionesActuales.isEmpty()) {
            return false;
        }

        Seleccion ultimaSeleccion = seleccionesActuales.get(seleccionesActuales.size() - 1);

        //Remover de la lista de prohibidos
        campeonesProhibidos.remove(ultimaSeleccion.getIdCampeon());

        // Eliminar la última selección
        seleccionesActuales.remove(seleccionesActuales.size() - 1);
        seleccionesLiveData.setValue(seleccionesActuales);
        draftActual.setSelecciones(seleccionesActuales);

        //Ajustar el estado del draft según la selección eliminada
        if (ultimaSeleccion.getTipo() == Seleccion.TipoSeleccion.PICK) {
            //Si estabamos en fase de picks
            if (turnoActual.getValue() > 0) {
                turnoActual.setValue(turnoActual.getValue() - 1);
                esEquipoAzul.setValue(SECUENCIA_PICK[turnoActual.getValue()]);
            } else if (estadoActual.getValue() == EstadoDraft.FINALIZADO) {
                estadoActual.setValue(EstadoDraft.FASE_PICK);
                turnoActual.setValue(SECUENCIA_PICK.length - 1);
                esEquipoAzul.setValue(SECUENCIA_PICK[SECUENCIA_PICK.length - 1]);
            }
        } else {
            //Si estabamos en fase de bans
            if (turnoActual.getValue() > 0) {
                turnoActual.setValue(turnoActual.getValue() - 1);
                esEquipoAzul.setValue(SECUENCIA_BAN[turnoActual.getValue()]);
            }

            //Si estabamos en fase de picks pero debemos volver a bans
            if (estadoActual.getValue() == EstadoDraft.FASE_PICK && turnoActual.getValue() == 0) {
                estadoActual.setValue(EstadoDraft.FASE_BAN);
                turnoActual.setValue(SECUENCIA_BAN.length - 1);
                esEquipoAzul.setValue(SECUENCIA_BAN[SECUENCIA_BAN.length - 1]);
            }
        }

        return true;
    }

    public Draft finalizarDraft() {
        estadoActual.setValue(EstadoDraft.FINALIZADO);
        return draftActual;
    }

    public List<Campeon> getCampeonesDisponibles() {
        List<Campeon> todosCampeones = campeonRepositorio.getCampeones().getValue();
        List<Campeon> disponibles = new ArrayList<>();

        if (todosCampeones == null) {
            return disponibles;
        }

        for (Campeon campeon : todosCampeones) {
            if (!campeonesProhibidos.contains(campeon.getId())) {
                disponibles.add(campeon);
            }
        }

        return disponibles;
    }

    public LiveData<EstadoDraft> getEstadoActual() {
        return estadoActual;
    }

    public LiveData<Integer> getTurnoActual() {
        return turnoActual;
    }

    public LiveData<Boolean> getEsEquipoAzul() {
        return esEquipoAzul;
    }

    public LiveData<List<Seleccion>> getSelecciones() {
        return seleccionesLiveData;
    }

    public Draft getDraftActual() {
        return draftActual;
    }
}
