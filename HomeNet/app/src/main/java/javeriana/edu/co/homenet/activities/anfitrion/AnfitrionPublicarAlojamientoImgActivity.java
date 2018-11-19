package javeriana.edu.co.homenet.activities.anfitrion;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.adapters.ImagenAnfitrionAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Usuario;

public class AnfitrionPublicarAlojamientoImgActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    final static int REQUEST_GALLERY = 2;

    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private ProgressDialog nProgressDialog;
    DatabaseReference mDataBase;

    Alojamiento alojamiento;
    String referenciaUrl = "";
    List<String> imgUri = new ArrayList<String>();
    int subidos;
    Usuario usr;
    List<String> listaImagenes = new ArrayList<String>();;
    Button agregarImg;
    Button siguiente;
    ViewPager viewPager;
    ImagenAnfitrionAdapter imgAnfAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //FirebaseApp.initializeApp(AnfitrionPublicarAlojamientoImgActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anfitrion_publicar_alojamiento_img);

        mAuth = FirebaseAuth.getInstance();
        nProgressDialog = new ProgressDialog(AnfitrionPublicarAlojamientoImgActivity.this);
        mDataBase = FirebaseDatabase.getInstance().getReference("Alojamientos");
        mStorage =FirebaseStorage.getInstance().getReference("ImagenesAlojamientos");
        alojamiento = (Alojamiento) getIntent().getSerializableExtra("Data");

        System.out.println("************"+alojamiento.getNombre());
        System.out.println("************"+alojamiento.getTipo());
        System.out.println("************"+alojamiento.getPrecio());
        System.out.println("************"+alojamiento.getDescripcion());



        agregarImg = findViewById(R.id.btSubirImgAPA);
        siguiente = findViewById(R.id.btSiguienteImgAPAI);

        //viewPager
        viewPager = (ViewPager)findViewById(R.id.vpImagenesAlojAPA);

        accionBotones();
    }
    public void onActivityResult(int requestCode, int ResultCode, Intent data){
        if(requestCode == RESULT_LOAD_IMAGE && ResultCode== RESULT_OK){ // TODO Probar si funciona sin verificar el GALLERY INTENT
            if(data.getClipData()!=null){
                ClipData imagenes = data.getClipData();
                int totalItemsSelected = data.getClipData().getItemCount();

                for(int i=0 ; i<totalItemsSelected;i++){
                    ClipData.Item item = imagenes.getItemAt(i);
                    Uri uri = item.getUri();
                    listaImagenes.add(uri.toString());
                }
                imgAnfAdapter = new ImagenAnfitrionAdapter(AnfitrionPublicarAlojamientoImgActivity.this,this.listaImagenes);
                viewPager.setAdapter(imgAnfAdapter);
                //Toast.makeText(this, "Se subieron los archivos", Toast.LENGTH_SHORT).show();
            }else if(data.getData()!=null){
                Uri uri = data.getData();
                listaImagenes.add(uri.toString());
                imgAnfAdapter = new ImagenAnfitrionAdapter(AnfitrionPublicarAlojamientoImgActivity.this,this.listaImagenes);
                viewPager.setAdapter(imgAnfAdapter);

            }
        }
    }
    public void accionBotones(){
        agregarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission((Activity) view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE, "Se necesita acceder a la galería", REQUEST_GALLERY);
                cargarImagen();
            }
        });
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarDatos()){
                    //Bundle bundle = getIntent().getExtras();
                    nProgressDialog.setMessage("Publicando el alojamiento...");
                    nProgressDialog.show();
                    subirImagenesStorage();
                }
                else{
                    Toast.makeText(AnfitrionPublicarAlojamientoImgActivity.this, "Tiene que seleccionar al menos cuatro imagenes de su galeria", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void cargarImagen(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("ENTRO acccion boton");
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Seleccionar imagenes"), RESULT_LOAD_IMAGE);
            //startActivityForResult(intent,RESULT_LOAD_IMAGE);
        }
    }
    public boolean validarDatos(){
        if(listaImagenes.size()>=4){
            return true;
        }else{

            return false;
        }
    }
    private void requestPermission(Activity context, String permiso, String explanation, int requestId ){
        if (ContextCompat.checkSelfPermission(context,permiso)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?  
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,permiso)) {
                Toast.makeText(context, explanation, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permiso}, requestId);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case  REQUEST_GALLERY: {
                cargarImagen();
                break;
            }
        }
    }
    private String extensionImagen(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    public void subirImagenesStorage(){
        Uri uri;
        subidos=0;
        for(int i=0;i<listaImagenes.size();i++) {
            uri = Uri.parse(listaImagenes.get(i));
            StorageReference fileReference = mStorage.child(System.currentTimeMillis() + "." + extensionImagen(uri));
            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    referenciaUrl = taskSnapshot.getUploadSessionUri().toString();
                    imgUri.add(referenciaUrl);
                    subidos++;
                    if(subidos==listaImagenes.size()){
                        subirAlojamiento();
                    }

                    System.out.println("ESTO ES TASK SNAPSHOT DONWLOAD URL ANTESSSSSSSSSSSS-------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + referenciaUrl);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    referenciaUrl = "";
                    Toast.makeText(AnfitrionPublicarAlojamientoImgActivity.this, "Falló la subida de una imagen", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void subirAlojamiento(){
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        alojamiento.setIdUsuario(uid);
        alojamiento.setUrlImgs(imgUri);
        //FirebaseUser user = mAuth.getCurrentUser();
        Random random = new Random();
        int numRandom = random.nextInt(10000)+1;
        final String idAlojamiento = "Alojamiento "+String.valueOf(numRandom)+String.valueOf(System.currentTimeMillis());
        alojamiento.setId(idAlojamiento);
        FirebaseDatabase.getInstance().getReference("Alojamientos").child(idAlojamiento).setValue(alojamiento).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //progressbar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    nProgressDialog.dismiss();
                    Toast.makeText(AnfitrionPublicarAlojamientoImgActivity.this, "Se ha publicado el alojamiento", Toast.LENGTH_SHORT).show();
                    //infoActualUsuario(idServicio,ususerv);
                    agregarDatosUsuarioServicio(alojamiento.getId());

                }
                else{
                    nProgressDialog.dismiss();
                    Toast.makeText( AnfitrionPublicarAlojamientoImgActivity.this,"Hubo un error al crear un servicio", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
    public void agregarDatosUsuarioServicio(final String idAloja){
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        mDataBase = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    usr = dataSnapshot.getValue(Usuario.class);
                    Usuario usuario = new Usuario(usr.getNombre(),usr.getUrlImg(),usr.getEdad(),usr.getTipoUsuario(),usr.getCorreo(),usr.getNacionalidad(),usr.getSexo());
                    if(usr.getAlojamientos().size()>0)
                        usuario.setAlojamientos(usr.getAlojamientos());
                    usuario.agregarElemento(idAloja,true);
                    mDataBase.child(mAuth.getCurrentUser().getUid()).setValue(usuario);
                    Intent intent = new Intent(AnfitrionPublicarAlojamientoImgActivity.this,AnfitrionMenuActivity.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    Toast.makeText(AnfitrionPublicarAlojamientoImgActivity.this, "No se encontro un usuario para asociar el alojamiento", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
            Intent intent = new Intent(AnfitrionPublicarAlojamientoImgActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
