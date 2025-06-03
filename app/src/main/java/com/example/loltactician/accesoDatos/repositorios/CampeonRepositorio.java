package com.example.loltactician.accesoDatos.repositorios;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.loltactician.APIs.RiotApiClient;
import com.example.loltactician.accesoDatos.modelos.Campeon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampeonRepositorio {
    private static CampeonRepositorio instance;
    private MutableLiveData<List<Campeon>> campeonesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> cargandoLiveData = new MutableLiveData<>(false);
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    //Mapa para buscar campeones por ID rápidamente
    private Map<String, Campeon> mapaCampeones = new HashMap<>();

    //Constructor privado para Singleton
    private CampeonRepositorio() {
        cargarCampeones();
    }

    //Metodo para obtener la instancia única
    public static synchronized CampeonRepositorio getInstance() {
        if (instance == null) {
            instance = new CampeonRepositorio();
        }
        return instance;
    }

    //Metodo para cargar los campeones desde la API
    public void cargarCampeones() {
        cargandoLiveData.setValue(true);
        errorLiveData.setValue(null);

        RiotApiClient.getChampions(new RiotApiClient.ChampionsCallback() {
            @Override
            public void onSuccess(List<Campeon> campeones) {
                //Actualizar el mapa de campeones para búsquedas rápidas
                mapaCampeones.clear();
                for (Campeon campeon : campeones) {
                    mapaCampeones.put(campeon.getId(), campeon);
                }

                campeonesLiveData.setValue(campeones);
                cargandoLiveData.setValue(false);
            }

            @Override
            public void onError(String message) {
                errorLiveData.setValue(message);
                cargandoLiveData.setValue(false);
            }
        });
    }

    public Campeon getCampeonById(String id) {
        return mapaCampeones.get(id);
    }

    public LiveData<List<Campeon>> getCampeones() {
        return campeonesLiveData;
    }

    public LiveData<Boolean> getCargando() {
        return cargandoLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }
}
