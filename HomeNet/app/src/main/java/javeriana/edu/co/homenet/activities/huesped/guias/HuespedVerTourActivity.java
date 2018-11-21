package javeriana.edu.co.homenet.activities.huesped.guias;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.Tour;
import javeriana.edu.co.homenet.models.Ubicacion;

public class HuespedVerTourActivity extends AppCompatActivity {

    public static final String PATH_TOUR="Tours/";

    TextView nombreAnuncio;
    ImageView posterAnuncio;
    TextView descripcionAnuncio;
    TextView precioAnuncio;
    TextView horaInicio;
    TextView duracion;
    TextView fecha;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    Button verRecorridos;
    Bundle b;
    Tour t;
    ArrayList<String> paradasLat = new ArrayList<>();
    ArrayList<String> paradasLong = new ArrayList<>();
    ArrayList<String> titulos = new ArrayList<>();
    ArrayList<String> descripciones = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_ver_tour);

        verRecorridos = findViewById(R.id.btVerRecorridosHVTA);
        nombreAnuncio = findViewById(R.id.tvNombreAnuncioHVTA);
        posterAnuncio = findViewById(R.id.ivAnuncioHVTA);
        descripcionAnuncio = findViewById(R.id.mtdescAnuncioHVMTA);
        precioAnuncio = findViewById(R.id.tvTarifaAnuncioHVTA);
        horaInicio = findViewById(R.id.tvHoraInicioHVTA);
        duracion = findViewById(R.id.tvDuracionHVTA);
        fecha = findViewById(R.id.tvFechaHVTA);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(PATH_TOUR);

        b = getIntent().getExtras();

        getTour(b.getString("idTour"));

        verRecorridos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedVerRecorridoTourActivity.class);
                Bundle b = new Bundle();
                b.putStringArrayList("latitudes", paradasLat);
                b.putStringArrayList("longitudes", paradasLong);
                b.putStringArrayList("titulos", titulos);
                b.putStringArrayList("descripciones", descripciones);
                startActivity(intent.putExtras(b));
            }
        });
    }

    private void getTour (final String id) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Tour tt = singleSnapshot.getValue(Tour.class);
                    if (singleSnapshot.getKey().equals(id)){
                        t = tt;
                    }
                }
                for (Ubicacion u : t.getRecorrido()){
                    paradasLat.add(Double.toString(u.getLatitude()));
                    paradasLong.add(Double.toString(u.getLongitude()));
                    titulos.add(u.getTitulo());
                    descripciones.add(u.getDescripcion());
                }
                setValues();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }

    private void setValues () {
        nombreAnuncio.setText(t.getTitulo());
        try{
            Picasso.get()
                    .load(t.getUrlImg())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(posterAnuncio);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        descripcionAnuncio.setText(t.getDescripcion());
        precioAnuncio.setText(Integer.toString(t.getPrecio()));
        horaInicio.setText(t.getHora());
        duracion.setText(Integer.toString(t.getDuracion()));
        fecha.setText(t.getFecha());
    }
}
