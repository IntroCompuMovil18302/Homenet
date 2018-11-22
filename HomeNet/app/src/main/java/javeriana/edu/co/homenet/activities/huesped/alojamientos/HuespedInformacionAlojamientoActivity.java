package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;
import javeriana.edu.co.homenet.adapters.ImagenAnfitrionAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;

public class HuespedInformacionAlojamientoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    DatabaseReference mDataBase;

    TextView nombreAloj;
    TextView descAloj;
    TextView precioAloj;
    TextView tipoAloj;
    Button ver_comentarios;
    Button disponibilidad;
    Button ver_ruta;
    Button home;
    Button reservar;
    Button calificar;
    ViewPager slider;
    ImagenAnfitrionAdapter imgAnfAdapter;

    String idAloj;
    Alojamiento alojamiento;

    List<String> listaImagenes = new ArrayList<String>();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_informacion_alojamiento);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference("Alojamientos/");
        mStorage = FirebaseStorage.getInstance().getReference("ImagenesAlojamientos/");

        nombreAloj = findViewById(R.id.tvNombreHotelHIAA);
        descAloj = findViewById(R.id.tvDescripcionAlojHIAA);
        precioAloj = findViewById(R.id.tvPrecioAlojHIAA);
        tipoAloj = findViewById(R.id.tvTipoAlojHIAA);
        ver_comentarios =(Button)findViewById(R.id.ver_comentarios);
        disponibilidad=(Button)findViewById(R.id.btConsultarDispHIAA);
        ver_ruta=(Button)findViewById(R.id.ver_ruta3);
        home =(Button)findViewById(R.id.btHomeHIAA);
        slider = findViewById(R.id.vpSliderHIAA);
        reservar = findViewById(R.id.btReservarHIA);
        calificar = findViewById(R.id.btCalificarHIA2);
        Intent bA = getIntent();
        Bundle b = bA.getExtras();
        idAloj = b.getString("idAloj");

        findAloj(idAloj);

        ver_comentarios.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedInformacionAlojamientoActivity.this,HuespedVerComentariosActivity.class);
                startActivity(intent);
            }
        });
        disponibilidad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedInformacionAlojamientoActivity.this,HuespedConsultarDisponibilidadActivity.class);
                intent.putExtra("idAloj",idAloj);
                startActivity(intent);
            }
        });
        ver_ruta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedInformacionAlojamientoActivity.this,HuespedVerRutaDestinoActivity.class);
                intent.putExtra("Aloj",alojamiento);
                startActivity(intent);
            }
        });
        reservar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedInformacionAlojamientoActivity.this,HuespedReservarAlojamientoActivity.class);
                intent.putExtra("idAloj",idAloj);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedInformacionAlojamientoActivity.this,MenuHuespedActivity.class);
                startActivity(intent);
            }
        });
        calificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HuespedInformacionAlojamientoActivity.this,HuespedCalificarAlojamientoActivity.class);
                intent.putExtra("idAloj",idAloj);
                startActivity(intent);
            }
        });
    }

    private void findAloj (final String idAloj) {
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Alojamiento ialojamiento = singleSnapshot.getValue(Alojamiento.class);
                    if (ialojamiento.getId().equals(idAloj)) {
                        alojamiento = ialojamiento;
                    }
                }
                llenarDatosVista();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }

    private void llenarDatosVista (){
        nombreAloj.setText(alojamiento.getNombre());
        descAloj.setText(alojamiento.getDescripcion());
        precioAloj.setText(String.valueOf(alojamiento.getPrecio()));
        tipoAloj.setText(alojamiento.getTipo());
        listaImagenes = alojamiento.getUrlImgs();
        imgAnfAdapter = new ImagenAnfitrionAdapter(HuespedInformacionAlojamientoActivity.this,this.listaImagenes);
        slider.setAdapter(imgAnfAdapter);
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
            Intent intent = new Intent(HuespedInformacionAlojamientoActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
