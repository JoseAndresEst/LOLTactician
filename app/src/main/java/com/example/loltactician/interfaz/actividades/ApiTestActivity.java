package com.example.loltactician.interfaz.actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loltactician.APIs.RiotApiClient;
import com.example.loltactician.accesoDatos.modelos.*;
import com.example.loltactician.R;

import java.util.List;

public class ApiTestActivity extends AppCompatActivity {

    private TextView resultText;
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);

        resultText = findViewById(R.id.resultText);
        testButton = findViewById(R.id.testButton);

        testButton.setOnClickListener(v -> testApi());
    }

    private void testApi() {
        resultText.setText("Solicitando datos...");

        RiotApiClient.getChampions(new RiotApiClient.ChampionsCallback() {
            @Override
            public void onSuccess(List<Campeon> campeones) {
                StringBuilder sb = new StringBuilder();
                sb.append("Éxito! Campeones recibidos: ").append(campeones.size()).append("\n\n");

                // Mostrar los primeros 5 campeones para verificar
                for (int i = 0; i < Math.min(5, campeones.size()); i++) {
                    Campeon c = campeones.get(i);
                    sb.append(c.getNombre())
                            .append(" (").append(c.getRol()).append(")\n")
                            .append("Dificultad: ").append(c.getDificultad()).append("\n")
                            .append("Imagen: ").append(c.getImagenUrl()).append("\n\n");
                }

                runOnUiThread(() -> resultText.setText(sb.toString()));
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> resultText.setText("Error: " + message));
            }
        });
    }
}