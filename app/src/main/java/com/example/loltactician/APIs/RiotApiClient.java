package com.example.loltactician.APIs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import com.google.gson.annotations.SerializedName;
import com.example.loltactician.accesoDatos.modelos.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiotApiClient {
    private static final String BASE_URL = "https://ddragon.leagueoflegends.com/";
    private static Retrofit retrofit = null;
    private static String currentVersion = null;

    //Interfaz que define los endpoints de la API
    public interface RiotApiService {
        @GET("api/versions.json")
        Call<List<String>> getVersions();

        @GET("cdn/{version}/data/es_ES/champion.json")
        Call<ChampionListResponse> getChampions(@Path("version") String version);

        @GET("cdn/{version}/data/es_ES/champion/{championId}.json")
        Call<ChampionDetailResponse> getChampionDetail(
                @Path("version") String version,
                @Path("championId") String championId);
    }

    //Métodos para obtener la instancia de Retrofit
    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    //Metodo para obtener la versión actual del juego
    public static void getCurrentVersion(final VersionCallback callback) {
        if (currentVersion != null) {
            callback.onSuccess(currentVersion);
            return;
        }

        RiotApiService apiService = getRetrofitInstance().create(RiotApiService.class);
        Call<List<String>> call = apiService.getVersions();

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    currentVersion = response.body().get(0); // La primera versión es la más reciente
                    callback.onSuccess(currentVersion);
                } else {
                    callback.onError("Error al obtener la versión: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    //Metodo para obtener todos los campeones
    public static void getChampions(final ChampionsCallback callback) {
        getCurrentVersion(new VersionCallback() {
            @Override
            public void onSuccess(String version) {
                RiotApiService apiService = getRetrofitInstance().create(RiotApiService.class);
                Call<ChampionListResponse> call = apiService.getChampions(version);

                call.enqueue(new Callback<ChampionListResponse>() {
                    @Override
                    public void onResponse(Call<ChampionListResponse> call, Response<ChampionListResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Campeon> campeones = convertToCampeonList(response.body());
                            callback.onSuccess(campeones);
                        } else {
                            callback.onError("Error al obtener campeones: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ChampionListResponse> call, Throwable t) {
                        callback.onError("Error de conexión: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    //Metodo para obtener los detalles de un campeón específico
    public static void getChampionDetails(String championId, final ChampionDetailCallback callback) {
        getCurrentVersion(new VersionCallback() {
            @Override
            public void onSuccess(String version) {
                RiotApiService apiService = getRetrofitInstance().create(RiotApiService.class);
                Call<ChampionDetailResponse> call = apiService.getChampionDetail(version, championId);

                call.enqueue(new Callback<ChampionDetailResponse>() {
                    @Override
                    public void onResponse(Call<ChampionDetailResponse> call, Response<ChampionDetailResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ChampionDetails championDetails = convertToChampionDetails(response.body(), championId);
                            callback.onSuccess(championDetails);
                        } else {
                            callback.onError("Error al obtener detalles del campeón: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ChampionDetailResponse> call, Throwable t) {
                        callback.onError("Error de conexión: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    //Convertir la respuesta JSON a objetos del modelo
    private static List<Campeon> convertToCampeonList(ChampionListResponse response) {
        List<Campeon> campeones = new ArrayList<>();

        for (Map.Entry<String, ChampionData> entry : response.data.entrySet()) {
            ChampionData champData = entry.getValue();

            //Crear URL de la imagen usando el formato estándar de Riot Games
            String imagenUrl = "https://ddragon.leagueoflegends.com/cdn/" +
                    currentVersion + "/img/champion/" +
                    champData.id + ".png";

            Campeon campeon = new Campeon(
                    champData.id,
                    champData.name,
                    champData.tags.get(0),
                    champData.info.difficulty,
                    champData.blurb,
                    0.0, //Winrate por defecto, se puede actualizar después
                    imagenUrl //Añadimos la URL de la imagen
            );
            campeones.add(campeon);
        }

        return campeones;
    }

    //Convertir la respuesta de detalles a un objeto del modelo
    private static ChampionDetails convertToChampionDetails(ChampionDetailResponse response, String championId) {
        ChampionDetailData detailData = response.data.get(championId);

        if (detailData == null) {
            return null;
        }

        // Crear URLs para las imágenes
        String championImageUrl = "https://ddragon.leagueoflegends.com/cdn/" +
                currentVersion + "/img/champion/" + detailData.id + ".png";

        // Crear lista de habilidades
        List<Habilidad> habilidades = new ArrayList<>();

        // Añadir pasiva
        String pasivaImageUrl = "https://ddragon.leagueoflegends.com/cdn/" +
                currentVersion + "/img/passive/" + detailData.passive.image.full;
        habilidades.add(new Habilidad(
                "P",
                detailData.passive.name,
                detailData.passive.description,
                pasivaImageUrl
        ));

        // Añadir habilidades Q, W, E, R
        String[] teclas = {"Q", "W", "E", "R"};
        for (int i = 0; i < detailData.spells.size() && i < 4; i++) {
            SpellData spell = detailData.spells.get(i);
            String spellImageUrl = "https://ddragon.leagueoflegends.com/cdn/" +
                    currentVersion + "/img/spell/" + spell.image.full;
            habilidades.add(new Habilidad(
                    teclas[i],
                    spell.name,
                    spell.description,
                    spellImageUrl
            ));
        }

        // Crear el objeto de detalles completo
        return new ChampionDetails(
                detailData.id,
                detailData.name,
                detailData.title,
                detailData.lore,
                detailData.tags,
                new ChampionStats(
                        detailData.info.attack,
                        detailData.info.defense,
                        detailData.info.magic,
                        detailData.info.difficulty
                ),
                habilidades,
                championImageUrl
        );
    }

    //Interfaz de callback para detalles de campeón
    public interface ChampionDetailCallback {
        void onSuccess(ChampionDetails championDetails);
        void onError(String message);
    }

    //Interfaces de callback para manejar respuestas asíncronas
    public interface VersionCallback {
        void onSuccess(String version);
        void onError(String message);
    }

    public interface ChampionsCallback {
        void onSuccess(List<Campeon> campeones);
        void onError(String message);
    }

    //Clases para deserialización de JSON
    static class ChampionListResponse {
        @SerializedName("data")
        Map<String, ChampionData> data = new HashMap<>();
    }

    static class ChampionDetailResponse {
        @SerializedName("data")
        Map<String, ChampionDetailData> data = new HashMap<>();
    }

    static class ChampionData {
        @SerializedName("id")
        String id;

        @SerializedName("key")
        String key;

        @SerializedName("name")
        String name;

        @SerializedName("title")
        String title;

        @SerializedName("blurb")
        String blurb;

        @SerializedName("info")
        ChampInfo info;

        @SerializedName("tags")
        List<String> tags;
    }

    static class ChampionDetailData extends ChampionData {
        @SerializedName("lore")
        String lore;

        @SerializedName("spells")
        List<SpellData> spells;

        @SerializedName("passive")
        PassiveData passive;
    }

    static class ChampInfo {
        @SerializedName("attack")
        int attack;

        @SerializedName("defense")
        int defense;

        @SerializedName("magic")
        int magic;

        @SerializedName("difficulty")
        int difficulty;
    }

    static class SpellData {
        @SerializedName("id")
        String id;

        @SerializedName("name")
        String name;

        @SerializedName("description")
        String description;

        @SerializedName("image")
        ImageData image;
    }

    static class PassiveData {
        @SerializedName("name")
        String name;

        @SerializedName("description")
        String description;

        @SerializedName("image")
        ImageData image;
    }

    static class ImageData {
        @SerializedName("full")
        String full;
    }
}
