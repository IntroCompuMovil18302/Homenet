package javeriana.edu.co.homenet.activities.anfitrion;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.adapters.AnfAlojamientosAdapter;
import javeriana.edu.co.homenet.adapters.AnfPubElementosAlojAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Disponibilidad;
import javeriana.edu.co.homenet.models.Ubicacion;
import javeriana.edu.co.homenet.services.CalificacionAlojamientoService;
import javeriana.edu.co.homenet.services.ReservasService;

public class AnfitrionMenuActivity extends AppCompatActivity {

    ArrayList<Alojamiento> alojamientos;
    ArrayList<Alojamiento> alojamientosUser;
    ImageButton nuevap;
    RecyclerView rvAlojamientos;

    AnfAlojamientosAdapter adapter;
    RecyclerView.LayoutManager managerAlojamientos;

    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private ProgressDialog nProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_menu);

        alojamientos = new ArrayList<Alojamiento>();

        mAuth = FirebaseAuth.getInstance();

        nuevap = findViewById(R.id.nuevap);
        rvAlojamientos = findViewById(R.id.rvAlojamientosAM);

        //datosPrueba();
        getALojamientosUser();
        crearRecycler();
        accionBotones();
    }

    public void crearRecycler(){
        managerAlojamientos = new LinearLayoutManager(this);
        adapter = new AnfAlojamientosAdapter(alojamientos);
        rvAlojamientos.setLayoutManager(managerAlojamientos);
        rvAlojamientos.setAdapter(adapter);
        Intent intent = new Intent(AnfitrionMenuActivity.this,ReservasService.class);
        startService(intent);
        Intent intentOpinion = new Intent(AnfitrionMenuActivity.this,CalificacionAlojamientoService.class);
        startService(intentOpinion);
        adapter.setOnItemClickListener(new AnfAlojamientosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(1,position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteItem(1,position);
            }
        });

    }

    public void insertItem(String text){
        Alojamiento a = new Alojamiento();
        alojamientos.add(a);
        adapter.notifyItemInserted(adapter.getItemCount());

    }

    public void deleteItem(int n, int position){
            alojamientos.remove(position);
            adapter.notifyItemRemoved(position);
    }

    public void changeItem(int n,int position){
            Alojamiento s = alojamientos.get(position);
            adapter.notifyItemChanged(position);

        Intent intent = new Intent(this,AnfitrionDetalleAlojamientoActivity.class);
        intent.putExtra("Data", alojamientos.get(position));
        startActivity(intent);

        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "Selecciono: " + position, Toast.LENGTH_SHORT);

        toast1.show();
    }

    public void datosPrueba(){
        Alojamiento a = new Alojamiento();
        Ubicacion u = new Ubicacion();
        u.setDireccion("direccion Alojamiento funciona--");
        a.setUbicacion(u);
        a.setNombre("nombre Prueba");
        a.setTipo("Apartamento");
        a.setDescripcion("la descripcion del alojamiento");
        // especificos
        a.setArea(50);
        a.setBanios(2);
        a.setCamas(3);
        a.setCalefaccion(true);
        a.setInternet(true);
        /// muebles
        ArrayList<String> mue = new ArrayList<String>();
        mue.add("aaaaa");
        mue.add("bbbb");
        mue.add("ccccc");
        mue.add("ddddd");
        mue.add("eeeee");
        a.setMuebles(mue);
        a.setElectrodomesticos(mue);
        // Disponibilidad
        Disponibilidad d = new Disponibilidad();
        d.setFechaInicio("10/08/2018");
        d.setFechaFin("17/02/2018");
        ArrayList<Disponibilidad> dis = new ArrayList<Disponibilidad>();
        dis.add(d);
        dis.add(d);
        dis.add(d);
        dis.add(d);
        a.setDisponibilidades(dis);


        alojamientos.add(a);
        alojamientos.add(a);
        alojamientos.add(a);
        alojamientos.add(a);
        alojamientos.add(a);
        alojamientos.add(a);
        alojamientos.add(a);
        alojamientos.add(a);
        alojamientos.add(a);
    }

    public void getALojamientosUser(){
        System.out.println("//////////////////////////");
        alojamientosUser =  new ArrayList<Alojamiento>();
        FirebaseUser user = mAuth.getCurrentUser();
        final String uid = user.getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Alojamientos/");
        db.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("///////------------------"+uid);
                    Alojamiento ialojamiento = singleSnapshot.getValue(Alojamiento.class);
                    System.out.println("///////----------ALO-------"+ialojamiento.getId());
                    if(ialojamiento != null)
                    {
                        if(ialojamiento.getIdUsuario().equals(uid))
                        {
                            alojamientos.add(ialojamiento);
                            System.out.println("///////----ENTRO");
                            System.out.println("///////----ENTRO");
                        }
                    }
                    else
                    {
                        System.out.println("///////----EEEE-");
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void accionBotones() {
        nuevap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfitrionPublicarAlojamientoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("modo",1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    // barra superior
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
            Intent intentStop = new Intent(AnfitrionMenuActivity.this,ReservasService.class);
            stopService(intentStop);
            Intent intentOpinionStop =  new Intent (AnfitrionMenuActivity.this,CalificacionAlojamientoService.class);
            stopService(intentOpinionStop);
            Intent intent = new Intent(AnfitrionMenuActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }
}
