package javeriana.edu.co.homenet.activities.anfitrion;

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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.adapters.AnfAlojamientosAdapter;
import javeriana.edu.co.homenet.adapters.AnfReservasAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Reserva;
import javeriana.edu.co.homenet.models.Ubicacion;
import javeriana.edu.co.homenet.services.CalificacionAlojamientoService;
import javeriana.edu.co.homenet.services.ReservasService;

public class AnfitrionHistorialReservasActivity extends AppCompatActivity {
    int modo;
    Alojamiento alojamiento;
    ArrayList<Reserva> reservas;
    private FirebaseAuth mAuth;
    RecyclerView rvReservas;
    AnfReservasAdapter adapter;
    RecyclerView.LayoutManager managerAlojamientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_historial_reservas);


        mAuth = FirebaseAuth.getInstance();
        reservas = new ArrayList<Reserva>();
        rvReservas =  findViewById(R.id.rvReservasAlojAHR);

        //datosPrueba();
        Bundle bundle = getIntent().getExtras();
        modo = bundle.getInt("modo");
        alojamiento = (Alojamiento) bundle.getSerializable("Data");
        if(modo == 2)
        {
            getReservasAloj();
        }
        crearRecycler();
        accionBotones();
    }

    public void crearRecycler(){
        managerAlojamientos = new LinearLayoutManager(this);
        adapter = new AnfReservasAdapter(reservas);
        rvReservas.setLayoutManager(managerAlojamientos);
        rvReservas.setAdapter(adapter);

        adapter.setOnItemClickListener(new AnfReservasAdapter.OnItemClickListener() {
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
       /* Alojamiento a = new Alojamiento();
        alojamientos.add(a);
        adapter.notifyItemInserted(adapter.getItemCount());*/

    }

    public void deleteItem(int n, int position){
        /*alojamientos.remove(position);
        adapter.notifyItemRemoved(position);*/
    }

    public void changeItem(int n,int position){
        Reserva r = reservas.get(position);
        adapter.notifyItemChanged(position);

        Intent intent = new Intent(this,AnfitrionDetalleReservaActivity.class);
        intent.putExtra("idReserva", reservas.get(position).getId());
        startActivity(intent);

        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "Selecciono: " + position, Toast.LENGTH_SHORT);

        toast1.show();
    }

    public void datosPrueba(){
        Reserva r = new Reserva();
        r.setFechaOperacion("10/08/2018");
        r.setHuesped("Huesped117");

        reservas.add(r);
        reservas.add(r);
        reservas.add(r);
        reservas.add(r);
        reservas.add(r);
        reservas.add(r);
    }

    public void getReservasAloj(){
        System.out.println("//////////////////////////");
        FirebaseUser user = mAuth.getCurrentUser();
        final String uid = user.getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Reservas/");
        db.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("///////------------------"+uid);
                    Reserva reserva = singleSnapshot.getValue(Reserva.class);
                    System.out.println("///////----------ALO-------"+reserva.getId());
                    if(reserva != null)
                    {
                        if(reserva.getAlojamiento().equals(alojamiento.getId()))
                        {
                            reservas.add(reserva);
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
        /*nuevap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfitrionPublicarAlojamientoActivity.class);
                startActivity(intent);
            }
        });*/

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
            Intent intentStop = new Intent(AnfitrionHistorialReservasActivity.this,ReservasService.class);
            stopService(intentStop);
            Intent intentOpinionStop =  new Intent (AnfitrionHistorialReservasActivity.this,CalificacionAlojamientoService.class);
            stopService(intentOpinionStop);
            Intent intent = new Intent(AnfitrionHistorialReservasActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
