package javeriana.edu.co.homenet.activities.anfitrion;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.adapters.AnfAlojamientosAdapter;
import javeriana.edu.co.homenet.adapters.AnfPubElementosAlojAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Ubicacion;

public class AnfitrionMenuActivity extends AppCompatActivity {

    ArrayList<Alojamiento> alojamientos;

    ImageButton nuevap;
    RecyclerView rvAlojamientos;

    AnfAlojamientosAdapter adapter;
    RecyclerView.LayoutManager managerAlojamientos;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_menu);

        alojamientos = new ArrayList<Alojamiento>();

        mAuth = FirebaseAuth.getInstance();

        nuevap = findViewById(R.id.nuevap);
        rvAlojamientos = findViewById(R.id.rvAlojamientosAM);

        datosPrueba();
        crearRecycler();
        accionBotones();
    }

    public void crearRecycler(){
        managerAlojamientos = new LinearLayoutManager(this);
        adapter = new AnfAlojamientosAdapter(alojamientos);
        rvAlojamientos.setLayoutManager(managerAlojamientos);
        rvAlojamientos.setAdapter(adapter);

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
        u.setDireccion("direccion Alojamiento");
        a.setUbicacion(u);
        a.setNombre("nombre Prueba");
        a.setTipo("Casa prueba");
        a.setDescripcion("la descripcion del alojamiento");
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

    public void accionBotones() {
        nuevap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfitrionPublicarAlojamientoActivity.class);
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
