package javeriana.edu.co.homenet.activities;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import javeriana.edu.co.homenet.activities.anfitrion.AnfitrionMenuActivity;
import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.guia.GuiaPrincipalActivity;
import javeriana.edu.co.homenet.activities.huesped.CrearPerfilHuespedActivity;
import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;
import javeriana.edu.co.homenet.models.Usuario;

public class CrearPerfilGuiaAnfActivity extends AppCompatActivity {


    public static final String PATH_USERS="users/";
    public static final String PATH_STORAGE="perfilPhotos/";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    private FirebaseAuth mAuth;

    final static int REQUEST_GALLERY = 1;
    final static int IMAGE_PICKER_REQUEST = 2;

    final static int REQUEST_CAMERA = 3;
    static final int REQUEST_IMAGE_CAPTURE = 4;

    ImageButton volver;
    Button crearPerfilGuiaAnf;
    EditText nombre;
    EditText edad;
    EditText tel;
    ImageView fotoPerfil;
    Button galeria;
    Button tomarFoto;
    Uri urlImage;
    String correo;
    String clave;
    String tipoUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_crear_perfil_guia_anf);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        volver = findViewById(R.id.ibVolverCPGA);
        crearPerfilGuiaAnf = findViewById(R.id.btCrearPerfilGuiaAnfCPGA);
        nombre = findViewById(R.id.etNombreGuiaAnfCPGA);
        edad = findViewById(R.id.etEdadCPGA);
        tel = findViewById(R.id.etTelCPGA);
        fotoPerfil = findViewById(R.id.ivFotoGuiaAnfCPGA);
        galeria = findViewById(R.id.btGaleriaCPGA);
        tomarFoto = findViewById(R.id.btTomarFotoCPGA);

        Intent bA = getIntent();
        Bundle b = bA.getExtras();
        correo = b.getString("correo");
        clave = b.getString("clave");
        tipoUsuario = b.getString("tipoUsuario");

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

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        crearPerfilGuiaAnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isETEmpty(nombre) && isETEmpty(edad) && isETEmpty(tel) ){
                    register(correo,clave);
                }else{
                    Toast.makeText(v.getContext(), "Complete los campos", Toast.LENGTH_SHORT).show();
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
                            }
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(CrearPerfilGuiaAnfActivity.this, "Fallo autenticación"+ task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.e("HomeNet", task.getException().getMessage());
                        }
                        else{
                            final FirebaseUser user = mAuth.getCurrentUser();
                            final StorageReference sR = storage.getReference(PATH_STORAGE+
                                    UUID.randomUUID());
                            Bitmap bitmap = ((BitmapDrawable) fotoPerfil.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageInByte = baos.toByteArray();
                            tomarFoto.setEnabled(false);
                            galeria.setEnabled(false);
                            StorageMetadata sM = new StorageMetadata.Builder()
                                    .setCustomMetadata("text","Perfil"+nombre.getText().toString())
                                    .build();
                            UploadTask uT = sR.putBytes(imageInByte,sM);
                            uT.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    sR.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            urlImage = uri;
                                        }
                                    });
                                }
                            });
                            Usuario nU = new Usuario(nombre.getText().toString(), urlImage.toString(), Integer.parseInt(edad.getText().toString()),
                                    tipoUsuario, correo, "", "");
                            myRef=database.getReference(PATH_USERS+user.getUid());
                            myRef.setValue(nU);
                            if (tipoUsuario.equals("Guía")){
                                Intent intent = new Intent(CrearPerfilGuiaAnfActivity.this, GuiaPrincipalActivity.class);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(CrearPerfilGuiaAnfActivity.this, AnfitrionMenuActivity.class);
                                startActivity(intent);
                            }
                            //}
                        }
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
                        urlImage = imageUri; //PENdiente
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