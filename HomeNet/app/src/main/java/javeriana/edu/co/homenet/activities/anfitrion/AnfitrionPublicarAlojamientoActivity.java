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


    // Seccion spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        tipoAlojamiento = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // seccion recycler view
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode== RESULT_OK){ // TODO Probar si funciona sin verificar el GALLERY INTENT
            if(data.getClipData()!=null){
                ClipData imagenes = data.getClipData();
                int totalItemsSelected = data.getClipData().getItemCount();

                for(int i=0 ; i<totalItemsSelected;i++){
                    ClipData.Item item = imagenes.getItemAt(i);
                    Uri uri = item.getUri();
                    listaImagenes.add(uri);
                    /*
                    StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    */
                }
                mAdapter = new ImageAdapter(getActivity(),listaImagenes);
                mRecyclerView.setAdapter(mAdapter);
                Toast.makeText(getActivity(), "Se subieron los archivos", Toast.LENGTH_SHORT).show();
            }else if(data.getData()!=null){
                Uri uri = data.getData();
                listaImagenes.add(uri);
                mAdapter = new ImageAdapter(getActivity(),listaImagenes);
                mRecyclerView.setAdapter(mAdapter);
                /*
                nProgressDialog.setMessage("Subiendo archivo...");
                nProgressDialog.show();
                Uri uri = data.getData();
                StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Se subio el archivo", Toast.LENGTH_SHORT).show();
                        nProgressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                */
            }
        }
    }

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