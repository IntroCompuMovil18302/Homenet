package javeriana.edu.co.homenet.activities.anfitrion;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;

import javeriana.edu.co.homenet.R;

public class AnfitrionPublicarAlojamientoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int RESULT_LOAD_IMAGE = 1;

    Spinner spinner;

    String tipoAlojamiento;

    Button agregarImg;
    Button ubicacion;
    Button publicar;

    EditText valor;
    EditText descripcion;

    RecyclerView rvImagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_publicar_alojamiento);

        // inflar variables
        spinner = findViewById(R.id.spTipoAPA);
        agregarImg = findViewById(R.id.btSubirImgAPA);
        ubicacion = findViewById(R.id.btUbicacionAPA);
        publicar = findViewById(R.id.btPublicarAPA);
        valor = findViewById(R.id.etValorAPA);
        descripcion = findViewById(R.id.etDescripcionAPA);
        rvImagenes = findViewById(R.id.rvImagenesAlojAPA);

        // spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipoAlojamiento, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        // recyclerView
        rvImagenes.setHasFixedSize(true);
        rvImagenes.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        accionBotones();
    }


    // Seccion spinner -----------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        tipoAlojamiento = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    // FIN Seccion spinner -----------------------------------------------


    // seccion recycler view --------------------------------------------


    // FIN seccion recycler view --------------------------------------------


    public void accionBotones() {
        agregarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ENTRO acccion boton");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccionar imagenes"), RESULT_LOAD_IMAGE);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }
        });
    }
}