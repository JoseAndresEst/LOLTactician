package com.example.loltactician.interfaz.actividades;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.loltactician.R;
import com.example.loltactician.accesoDatos.modelos.Campeon;
import com.example.loltactician.accesoDatos.modelos.Draft;
import com.example.loltactician.accesoDatos.modelos.Seleccion;
import com.example.loltactician.accesoDatos.repositorios.CampeonRepositorio;
import com.example.loltactician.accesoDatos.repositorios.DraftRepositorio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DraftResumenActivity extends AppCompatActivity {

    private static final String EXTRA_DRAFT = "extra_draft";

    private TextView nombreDraftTextView;
    private TextView fechaCreacionTextView;
    private LinearLayout bansEquipoAzulContainer;
    private LinearLayout picksEquipoAzulContainer;
    private LinearLayout bansEquipoRojoContainer;
    private LinearLayout picksEquipoRojoContainer;
    private Button btnCompartir;
    private Button btnGuardar;

    private Draft draft;
    private CampeonRepositorio campeonRepositorio;

    public static Intent createIntent(Context context, Draft draft) {
        Intent intent = new Intent(context, DraftResumenActivity.class);
        intent.putExtra(EXTRA_DRAFT, draft);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_resumen);

        //Inicializar repositorio
        campeonRepositorio = CampeonRepositorio.getInstance();

        //Recuperar el draft de los extras
        if (getIntent().hasExtra(EXTRA_DRAFT)) {
            draft = (Draft) getIntent().getSerializableExtra(EXTRA_DRAFT);
        } else {
            Toast.makeText(this, "Error: No se encontró información del draft", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //Inicializar vistas
        initViews();

        //Cargar datos del draft
        cargarDatosDraft();

        //Configurar botones
        setupButtons();
    }

    private void initViews() {
        nombreDraftTextView = findViewById(R.id.nombre_draft_text);
        fechaCreacionTextView = findViewById(R.id.fecha_creacion_text);
        bansEquipoAzulContainer = findViewById(R.id.bans_equipo_azul_container);
        picksEquipoAzulContainer = findViewById(R.id.picks_equipo_azul_container);
        bansEquipoRojoContainer = findViewById(R.id.bans_equipo_rojo_container);
        picksEquipoRojoContainer = findViewById(R.id.picks_equipo_rojo_container);
        btnCompartir = findViewById(R.id.btn_compartir);
        btnGuardar = findViewById(R.id.btn_guardar);
    }

    private void cargarDatosDraft() {
        nombreDraftTextView.setText("Nombre del Draft: " + draft.getNombreDraft());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        fechaCreacionTextView.setText("Fecha: " + dateFormat.format(draft.getFechaCreacion()));

        List<Seleccion> bansA = draft.getBansEquipoA();
        List<Seleccion> bansB = draft.getBansEquipoB();
        List<Seleccion> picksA = draft.getPicksEquipoA();
        List<Seleccion> picksB = draft.getPicksEquipoB();

        llenarContenedorSelecciones(bansEquipoAzulContainer, bansA);
        llenarContenedorSelecciones(picksEquipoAzulContainer, picksA);
        llenarContenedorSelecciones(bansEquipoRojoContainer, bansB);
        llenarContenedorSelecciones(picksEquipoRojoContainer, picksB);
    }

    private void llenarContenedorSelecciones(LinearLayout container, List<Seleccion> selecciones) {
        container.removeAllViews();

        for (Seleccion seleccion : selecciones) {
            Campeon campeon = campeonRepositorio.getCampeonById(seleccion.getIdCampeon());
            if (campeon != null) {
                //Inflar la vista para cada campeón
                View itemView = getLayoutInflater().inflate(R.layout.item_campeon_resumen, container, false);

                ImageView imagenCampeon = itemView.findViewById(R.id.campeon_imagen);
                TextView nombreCampeon = itemView.findViewById(R.id.campeon_nombre);

                //Establecer datos
                nombreCampeon.setText(campeon.getNombre());

                //Cargar imagen
                Glide.with(this)
                        .load(campeon.getImagenUrl())
                        .placeholder(R.drawable.placeholder_campeon)
                        .into(imagenCampeon);

                //Si es un ban, aplicar efecto visual
                if (seleccion.getTipo() == Seleccion.TipoSeleccion.BAN) {
                    View overlayBan = itemView.findViewById(R.id.overlay_ban);
                    if (overlayBan != null) {
                        overlayBan.setVisibility(View.VISIBLE);
                    }
                }

                //Agregar al contenedor
                container.addView(itemView);
            }
        }
    }

    private void setupButtons() {
        btnCompartir.setOnClickListener(v -> {
            //Crear una captura de pantalla del layout y compartirla
            subirResumen();
        });

        btnGuardar.setOnClickListener(v -> {
            //Guardar una imagen del resumen
            guardarResumen();
        });
    }

    private void subirResumen() {
        DraftRepositorio.getInstance().guardarDraft(draft);
        startActivity(new Intent(DraftResumenActivity.this, MainActivity.class));
        finish();
    }

    private void guardarResumen() {
        //Pedir permisos si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Usar API moderna para guardar imágenes
            guardarImagenModerna();
        } else {
            //Verificar y solicitar permisos para versiones anteriores
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                guardarImagenLegacy();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void guardarImagenModerna() {
        View rootView = findViewById(android.R.id.content);
        Bitmap bitmap = getBitmapFromView(rootView);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "Draft_" + System.currentTimeMillis() + ".png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Drafts");

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            Toast.makeText(this, "Imagen guardada en la galería", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarImagenLegacy() {
        View rootView = findViewById(android.R.id.content);
        Bitmap bitmap = getBitmapFromView(rootView);

        String fileName = "Draft_" + System.currentTimeMillis() + ".png";
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, fileName);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            //Notificar a la galería
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, null);

            Toast.makeText(this, "Imagen guardada correctamente en la galería", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            guardarImagenLegacy();
        } else {
            Toast.makeText(this, "Se requiere permiso para guardar la imagen", Toast.LENGTH_SHORT).show();
        }
    }
}