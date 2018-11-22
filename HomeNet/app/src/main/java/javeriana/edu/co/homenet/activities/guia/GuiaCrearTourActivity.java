package javeriana.edu.co.homenet.activities.guia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.CrearPerfilActivity;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.activities.anfitrion.AnfitrionMenuActivity;
import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;
import javeriana.edu.co.homenet.models.HistoricoTour;
import javeriana.edu.co.homenet.models.Tour;
import javeriana.edu.co.homenet.models.Ubicacion;
import javeriana.edu.co.homenet.models.Usuario;
import javeriana.edu.co.homenet.utils.DateFormater;

public class GuiaCrearTourActivity extends AppCompatActivity {


    final static int REQUEST_GALLERY = 1;
    final static int IMAGE_PICKER_REQUEST = 2;
    public static final String PATH_TOURS = "Tours/";
    public static final String PATH_STORAGE = "fotosTours/";
    DatePickerDialog dpdInicio;
    TimePickerDialog mTimePicker;

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
                    item.setIntent(new Intent(GuiaCrearTourActivity.this, GuiaHistorialToures.class));
                    return true;
                case R.id.navigation_config:
                    item.setIntent(new Intent(GuiaCrearTourActivity.this, GuiaCalificacionesActivity.class));
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
    HashSet<String> moneditas = new HashSet<String>();

    ArrayAdapter<String> adapter;
    Calendar cCalendar;

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
        guardar = findViewById(R.id.btGuardarGCT);
        addRecorrdo = findViewById(R.id.btRecorridoGCT);
        addImage = findViewById(R.id.btImageGCT);
        monedas = findViewById(R.id.spMonedaGCT);
        titulo = findViewById(R.id.etTituloGCT);
        fecha = findViewById(R.id.etFechaGCT);
        hora = findViewById(R.id.etHoraGCT);
        duracion = findViewById(R.id.etDuracionGCT);
        descripcion = findViewById(R.id.etDescripcionGCT);
        capacidad = findViewById(R.id.etCapacidadGCT);
        precio = findViewById(R.id.etPrecioGCT);
        nProgressDialog = new ProgressDialog(GuiaCrearTourActivity.this);

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cCalendar = Calendar.getInstance();
                int day = cCalendar.get(Calendar.DAY_OF_MONTH);
                int month = cCalendar.get(Calendar.MONTH);
                int year = cCalendar.get(Calendar.YEAR);

                dpdInicio = new DatePickerDialog(GuiaCrearTourActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        String fechaIn = Integer.toString(mDay) + "/" + Integer.toString(mMonth + 1) + "/" + Integer.toString(mYear);
                        fecha.setText(fechaIn);
                    }
                }, year, month, day);

                dpdInicio.setTitle("Fecha Inicio");
                dpdInicio.show();
            }
        });

        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(GuiaCrearTourActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hora.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Seleccione Hora");
                mTimePicker.show();
            }
        });

        currencies.add("Monedas");
        currenciesREST();

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
                if (validateForm()) {
                    nProgressDialog.setMessage("Creando el tour...");
                    nProgressDialog.show();
                    crearTour();
                }

            }
        });

    }

    private void currenciesREST() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://openexchangerates.org/api/currencies.json", new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.names();
                    for (int i = 0; i < array.length(); i++) {
                        String obj = String.valueOf(array.get(i));
                        currencies.add(obj);
                    }
                    adapter = new ArrayAdapter<String>(GuiaCrearTourActivity.this,
                            R.layout.support_simple_spinner_dropdown_item, currencies);

                    monedas.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.w("TAG_XD", error.toString());
                error.printStackTrace();

            }

        });

        int socketTimeout = 30000;

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        requestQueue.add(stringRequest);

    }


    private void loadGalerryImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent pickImage = new Intent(Intent.ACTION_PICK);
            pickImage.setType("image/*");
            startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        BitmapDrawable bdrawable;
                        try{
                            bdrawable = new BitmapDrawable(GuiaCrearTourActivity.this.getResources(),selectedImage);
                            addImage.setBackground(bdrawable);
                            addImage.setText("");
                        }
                        catch(Exception e){

                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void requestPermission(Activity context, String permission, String explanation, int requestId) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                Toast.makeText(context, explanation, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestId);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_GALLERY: {
                loadGalerryImage();
                break;
            }
        }
    }

    private void crearTour() {
        myRef = database.getReference(PATH_TOURS);
        String key = myRef.push().getKey();
        myRef = database.getReference(PATH_TOURS + key);
        final FirebaseUser user = mAuth.getCurrentUser();
        final StorageReference sR = storage.getReference(PATH_STORAGE.concat("/")
                + key);
        Bitmap bitmap = ((BitmapDrawable) addImage.getBackground()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        StorageMetadata sM = new StorageMetadata.Builder()
                .setCustomMetadata("text", "foto-tour")
                .build();
        UploadTask uT = sR.putBytes(imageInByte, sM);
        uT.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sR.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        urlImage = uri;
                        List<Ubicacion> recorrido = new ArrayList<>();
                        Tour nT = new Tour(
                                Integer.parseInt(capacidad.getText().toString()),
                                descripcion.getText().toString(),
                                Integer.parseInt(duracion.getText().toString()),
                                fecha.getText().toString(),
                                user.getUid(),
                                hora.getText().toString(),
                                monedas.getSelectedItem().toString(),
                                Integer.parseInt(precio.getText().toString()),
                                titulo.getText().toString(),
                                urlImage.toString(),
                                recorrido);
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

    private boolean validateForm() {
        boolean flag = true;
        try {
            if (!String.valueOf(duracion.getText()).toString().replaceAll(" ", "").equals("")) {
                if (Integer.parseInt(duracion.getText().toString()) < 0) {
                    flag = false;
                    duracion.setError("Debe ser positivo");
                }
            }else{
                duracion.setError("Llenado obligatorio");
            }
        } catch (Exception e) {
            duracion.setError("Debe llenarse");
            flag = false;
        }

        try {
            if (!String.valueOf(capacidad.getText()).toString().replaceAll(" ", "").equals("")) {
                if (Integer.parseInt(capacidad.getText().toString()) < 0) {
                    flag = false;
                    capacidad.setError("Debe ser positivo");
                }
            }else{
                capacidad.setEGuiaCrearTourActivity.javarror("Llenado obligatorio");
            }
        } catch (Exception e) {
            capacidad.setError("Debe ser llenado");
            flag = false;
        }

        try {
            if (!String.valueOf(precio.getText()).toString().replaceAll(" ", "").equals("")) {
                if (Integer.parseInt(precio.getText().toString()) < 0) {
                    flag = false;
                    precio.setError("Debe ser positivo");
                }
            }else{
                precio.setError("Debe ser llenado");
            }
        } catch (Exception e) {
            flag = false;
            precio.setError("Debe ser llenado");
        }


        if (String.valueOf(monedas.getSelectedItem()).equals("Monedas")) {
            flag = false;
        } else {
            flag = false;
        }

        try {
            Date d1 = DateFormater.stringToDate(String.valueOf(fecha.getText()));
            Date d2 = DateFormater.today();
            if (!String.valueOf(fecha.getText()).toString().replaceAll(" ", "").equals("")) {
                if (d1.before(d2)) {
                    flag = false;
                    fecha.setError("Fecha en el pasado");
                }
            }else{
                fecha.setError("Llenado obligatorio");
            }

        } catch (Exception e) {
            flag = false;
            fecha.setError("Llenado obligatorio");
        }

        try {
            Date d1 = DateFormater.stringToDate(String.valueOf(fecha.getText()));
            Date d2 = DateFormater.today();
            if (d1.equals(d2)) {

                if (!String.valueOf(hora.getText()).replaceAll(" ", "").equals("")) {
                    d2 = DateFormater.stringToDateHour(String.valueOf(hora.getText()));
                    long hour1 = (d1.getTime() % 86400000) / 3600000;
                    long hour2 = (d2.getTime() % 86400000) / 3600000;
                    if (hour1 < hour2) {
                        flag = false;
                    }
                }
            }
        } catch (Exception e) {
            flag = false;
            hora.setError("Es de llenado obligatorio");
        }

        if(titulo.getText().toString().replaceAll(" ","").equals("")){
            flag = false;
            titulo.setError("Llenado Obligatorio");
        }

        if(descripcion.getText().toString().replaceAll(" ","").equals("")){
            flag = false;
            descripcion.setError("Llenado obligatorio");
        }


        return flag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuLogOut){
            mAuth.signOut();
            Intent intent = new Intent(GuiaCrearTourActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
