package javeriana.edu.co.homenet.activities.anfitrion;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.adapters.ImagenAnfitrionAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;
import me.relex.circleindicator.CircleIndicator;

public class AnfitrionPublicarAlojamientoImgActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    final static int REQUEST_GALLERY = 2;

    Alojamiento alojamiento;

    List<String> listaImagenes = new ArrayList<String>();;
    Button agregarImg;
    Button siguiente;
    ViewPager viewPager;
    ImagenAnfitrionAdapter imgAnfAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alojamiento = (Alojamiento) getIntent().getSerializableExtra("Data");

        System.out.println("************"+alojamiento.getNombre());
        System.out.println("************"+alojamiento.getTipo());
        System.out.println("************"+alojamiento.getPrecio());
        System.out.println("************"+alojamiento.getDescripcion());


        setContentView(R.layout.activity_anfitrion_publicar_alojamiento_img);
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
                CircleIndicator indicator = (CircleIndicator)findViewById(R.id.ciImagenesAlojAPA);
                indicator.setViewPager(viewPager);
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
                }
                else{
                    Toast.makeText(AnfitrionPublicarAlojamientoImgActivity.this, "Tiene que seleccionar al menos una imagen de su galeria", Toast.LENGTH_SHORT).show();
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
        if(listaImagenes.size()>0){
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
}
