package javeriana.edu.co.homenet.activities.guia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.CrearPerfilActivity;
import javeriana.edu.co.homenet.activities.anfitrion.AnfitrionMenuActivity;
import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;
import javeriana.edu.co.homenet.models.Tour;
import javeriana.edu.co.homenet.models.Ubicacion;
import javeriana.edu.co.homenet.models.Usuario;

public class GuiaCrearTourActivity extends AppCompatActivity {


    final static int REQUEST_GALLERY = 1;
    final static int IMAGE_PICKER_REQUEST = 2;
    public static final String PATH_TOURS="Tours/";
    public static final String PATH_STORAGE="fotosTours/";

    BottomNavigationView navigation;
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tours:
                    item.setIntent(new Intent(GuiaCrearTourActivity.this, GuiaPrincipalActivity.class));
                    return true;
                case R.id.navigation_new_tour:
                    return true;
                case R.id.navigation_history_tours:
                    return true;
                case R.id.navigation_config:
                    return true;
            }
            return false;
        }
    };
    Button guardar;
    Button addRecorrdo;
    Button addImage;
    Spinner monedas;
    EditText titulo;
    EditText fecha;
    EditText hora;
    EditText duracion;
    EditText descripcion;
    EditText capacidad;
    EditText precio;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;

    private ProgressDialog nProgressDialog;
    private Uri urlImage;

    private List<String> currencies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_crear_tour);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_new_tour);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
       /* guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new GuiaRenovarTourActivity();
                newFragment.show(getSupportFragmentManager(), "missiles");
            }
        });*/
        guardar = findViewById(R.id.btGuardarGCT);
        addRecorrdo = findViewById(R.id.btRecorridoGCT);
        addImage = findViewById(R.id.btImageGCT);
        monedas = findViewById(R.id.spMonedaGCT);
        titulo = findViewById(R.id. etTituloGCT);
        fecha = findViewById(R.id.etFechaGCT);
        hora = findViewById(R.id.etHoraGCT);
        duracion = findViewById(R.id.etDuracionGCT);
        descripcion = findViewById(R.id.etDescripcionGCT);
        capacidad = findViewById(R.id.etCapacidadGCT);
        precio = findViewById(R.id.etPrecioGCT);
        nProgressDialog = new ProgressDialog(GuiaCrearTourActivity.this);

        currencies.add("Moneda");
        currenciesREST();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, currencies);
        monedas.setAdapter(adapter);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission((Activity) v.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE, "Se necesita acceder a la galería", REQUEST_GALLERY);
                loadGalerryImage();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    nProgressDialog.setMessage("Creando el tour...");
                    nProgressDialog.show();
                    crearTour();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void currenciesREST() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String query = "https://restcountries.eu/rest/v2/all?fields=currencies";
        StringRequest req = new StringRequest(Request.Method.GET, query,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        String data = (String)response;
                        try {
                            JSONArray jsonCurrencies = new JSONArray(data);
                            for(int i=0; i<jsonCurrencies.length(); i++)
                            {
                                JSONObject jo = (JSONObject) jsonCurrencies.get(i);
                                JSONArray n = (JSONArray)jo.get("currencies");
                                for(int j=0; j<n.length();j++){
                                    JSONObject joi = (JSONObject) n.get(j);
                                    String ni = (String)joi.get("code");
                                    currencies.add(ni);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG", "Error handling rest invocation"+error.getCause());
                    }
                });
        queue.add(req);
    }

    private void loadGalerryImage(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent pickImage = new Intent(Intent.ACTION_PICK);
            pickImage.setType("image/*");
            startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
        }
        else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case IMAGE_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        BitmapDrawable bdrawable = new BitmapDrawable(GuiaCrearTourActivity.this.getResources(),selectedImage);
                        addImage.setBackground(bdrawable);
                        addImage.setText("");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void requestPermission(Activity context, String permission, String explanation, int requestId ){
        if (ContextCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,permission)) {
                Toast.makeText(context, explanation, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestId);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case  REQUEST_GALLERY: {
                loadGalerryImage();
                break;
            }
        }
    }

    private void crearTour(){
        myRef = database.getReference(PATH_TOURS);
        String key = myRef.push().getKey();
        myRef = database.getReference(PATH_TOURS+key);
        final FirebaseUser user = mAuth.getCurrentUser();
        final StorageReference sR = storage.getReference(PATH_STORAGE.concat("/")
                +key);
        Bitmap bitmap = ((BitmapDrawable)addImage.getBackground()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        StorageMetadata sM = new StorageMetadata.Builder()
                .setCustomMetadata("text","foto-tour")
                .build();
        UploadTask uT = sR.putBytes(imageInByte,sM);
        uT.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sR.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        urlImage = uri;
                        List<Ubicacion> recorrido = new ArrayList<>();
                        Tour nT = new Tour(Integer.parseInt(capacidad.getText().toString()),
                                descripcion.getText().toString(),
                                Integer.parseInt(duracion.getText().toString()),
                                fecha.getText().toString(),
                                hora.getText().toString(),
                                monedas.getSelectedItem().toString(),
                                Integer.parseInt(precio.getText().toString()),
                                titulo.getText().toString(),
                                urlImage.toString(),
                                recorrido,
                                user.getUid());
                        myRef.setValue(nT);
                        nProgressDialog.dismiss();
                        Toast.makeText(GuiaCrearTourActivity.this, "Tour creado correctamente", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        uT.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GuiaCrearTourActivity.this, "Creación de tour fallida", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateForm(){
        return true;
    }
}
