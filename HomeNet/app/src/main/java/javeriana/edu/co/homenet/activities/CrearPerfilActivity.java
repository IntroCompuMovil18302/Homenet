package javeriana.edu.co.homenet.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.anfitrion.AnfitrionMenuActivity;
import javeriana.edu.co.homenet.activities.guia.GuiaPrincipalActivity;
import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;
import javeriana.edu.co.homenet.models.Usuario;

public class CrearPerfilActivity extends AppCompatActivity {

    public static final String PATH_USERS="users/";
    public static final String PATH_USERS_GUIAS="usersGuias/";
    public static final String PATH_STORAGE="perfilPhotos/";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    final static int REQUEST_GALLERY = 1;
    final static int IMAGE_PICKER_REQUEST = 2;

    final static int REQUEST_CAMERA = 3;
    static final int REQUEST_IMAGE_CAPTURE = 4;

    private ProgressDialog nProgressDialog;

    Button crearPerfilHuesped;
    EditText nombre;
    EditText edad;
    EditText tel;
    Spinner nac;
    Spinner sexo;
    String correo;
    String clave;
    ImageView fotoPerfil;
    Button tomarFoto;
    Button galeria;
    Uri urlImage;
    String tipoUsuario;
    ArrayList<String> countries = new ArrayList<String>();
    String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_crear_perfil);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        nProgressDialog = new ProgressDialog(CrearPerfilActivity.this);

        crearPerfilHuesped = findViewById(R.id.btCrearPerfilHuespedCPHA);
        nombre = findViewById(R.id.etNombreHuespedCPHA);
        edad = findViewById(R.id.etEdadHuespedCPHA);
        tel = findViewById(R.id.etTelHuespedCPHA);
        nac = findViewById(R.id.sNacHuespedCPHA);
        sexo = findViewById(R.id.sSexoHuespedCPHA);
        fotoPerfil = findViewById(R.id.vFotoPerfilCPHA);
        galeria = findViewById(R.id.btGaleriaCPHA);
        tomarFoto = findViewById(R.id.btTomarFotoCPHA);

        Intent bA = getIntent();
        Bundle b = bA.getExtras();
        correo = b.getString("correo");
        clave = b.getString("clave");
        tipoUsuario = b.getString("tipoUsuario");

        countries.add("Seleccione un país");
        nacionalityREST();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, countries);
        nac.setAdapter(adapter);

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission((Activity) v.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE, "Se necesita acceder a la galería", REQUEST_GALLERY);
                loadGalerryImage();
            }
        });

        tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission((Activity) v.getContext(), Manifest.permission.CAMERA, "Se necesita acceder a la camara", REQUEST_CAMERA);
                takePicture();
            }
        });

        crearPerfilHuesped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isETEmpty(nombre) && isETEmpty(edad) && isETEmpty(tel) ){
                    nProgressDialog.setMessage("Creando el usuario...");
                    nProgressDialog.show();
                    register(correo,clave);
                }else{
                    nombre.setError("Complete el campo");
                    edad.setError("Complete el campo");
                    tel.setError("Complete el campo");
                }
            }
        });
    }

    private void register(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){ //Update user Info
                                UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                                upcrb.setDisplayName(nombre.getText().toString());
                                user.updateProfile(upcrb.build());
                                saveUser();
                            }
                            else{
                                nProgressDialog.dismiss();
                            }
                        }
                        if (!task.isSuccessful()) {
                            nProgressDialog.dismiss();
                            Toast.makeText(CrearPerfilActivity.this, "Fallo autenticación"+ task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.e("HomeNet", task.getException().getMessage());
                        }
                    }
                });
    }

    private void saveUser(){
        final FirebaseUser user = mAuth.getCurrentUser();
        final StorageReference sR = storage.getReference(PATH_STORAGE.concat("/")
                +user.getUid());
        Bitmap bitmap = ((BitmapDrawable) fotoPerfil.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        tomarFoto.setEnabled(false);
        galeria.setEnabled(false);
        StorageMetadata sM = new StorageMetadata.Builder()
                .setCustomMetadata("text","foto-perfil")
                .build();
        UploadTask uT = sR.putBytes(imageInByte,sM);
        uT.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sR.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        nProgressDialog.dismiss();
                        urlImage = uri;
                        Usuario nU = new Usuario(nombre.getText().toString(), urlImage.toString(),
                                Integer.parseInt(edad.getText().toString()),
                                tipoUsuario, correo, nac.getSelectedItem().toString().toString(),
                                sexo.getSelectedItem().toString());

                        if (tipoUsuario.equals("Guía")){
                            myRef = database.getReference(PATH_USERS_GUIAS+user.getUid());
                            myRef.setValue(nU);
                            Intent intent = new Intent(CrearPerfilActivity.this, GuiaPrincipalActivity.class);
                            startActivity(intent);
                            finish();
                        }else if (tipoUsuario.equals("Anfitrión")){
                            myRef = database.getReference(PATH_USERS+user.getUid());
                            myRef.setValue(nU);
                            Intent intent = new Intent(CrearPerfilActivity.this, AnfitrionMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            myRef = database.getReference(PATH_USERS+user.getUid());
                            myRef.setValue(nU);
                            Intent intent = new Intent(CrearPerfilActivity.this, MenuHuespedActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
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
                        fotoPerfil.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    fotoPerfil.setImageBitmap(imageBitmap);
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

    private void nacionalityREST () {
        RequestQueue queue = Volley.newRequestQueue(this);
        String query = "https://restcountries.eu/rest/v2/all?fields=name";
        StringRequest req = new StringRequest(Request.Method.GET, query,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        String data = (String)response;
                        try {
                            JSONArray nacionalities = new JSONArray(data);
                            for(int i=0; i<nacionalities.length(); i++)
                            {
                                JSONObject jo = (JSONObject) nacionalities.get(i);
                                String n = (String)jo.get("name");
                                countries.add(n);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case  REQUEST_GALLERY: {
                loadGalerryImage();
                break;
            }
            case REQUEST_CAMERA: {
                takePicture();
                break;
            }
        }
    }

    private void takePicture() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        else {

        }
    }

    public boolean isETEmpty(EditText et) {
        if (et.getText().toString().equals("") || et.getText().toString().equals(" ")){
            return false;
        }
        return true;
    }
}
