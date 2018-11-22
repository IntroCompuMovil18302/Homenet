package javeriana.edu.co.homenet.activities.anfitrion;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.adapters.AnfAlojamientosAdapter;
import javeriana.edu.co.homenet.adapters.AnfComentariosAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.OpinionAlojamiento;

public class AnfComentariosActivity extends AppCompatActivity {

    Alojamiento alojamiento;
    ArrayList<OpinionAlojamiento> opiniones;
    RecyclerView rvOpniones;

    AnfComentariosAdapter adapter;
    RecyclerView.LayoutManager managerComenttarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_comentarios);

        opiniones = new ArrayList<OpinionAlojamiento>();
        alojamiento = (Alojamiento) getIntent().getSerializableExtra("Data");

        rvOpniones = findViewById(R.id.rvComentariosAC);


        getComentariosUser();
        crearRecycler();
    }

    public void crearRecycler(){
        managerComenttarios = new LinearLayoutManager(this);
        adapter = new AnfComentariosAdapter(opiniones);
        rvOpniones.setLayoutManager(managerComenttarios);
        rvOpniones.setAdapter(adapter);

        adapter.setOnItemClickListener(new AnfComentariosAdapter.OnItemClickListener() {
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

    public void deleteItem(int n, int position){
        opiniones.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void changeItem(int n,int position){

        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "Selecciono: " + position, Toast.LENGTH_SHORT);

        toast1.show();
    }

    public void getComentariosUser(){
        // datos
        System.out.println("//////////////////////////");

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("OpinionesAlojamiento/");
        db.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("///////----------------");
                    OpinionAlojamiento opinion = singleSnapshot.getValue(OpinionAlojamiento.class);
                    System.out.println("///////----------ALO-------"+opinion.getComentario());
                    if(opinion != null)
                    {
                        if(opinion.getAlojamiento().equals(alojamiento.getId()))
                        {
                            opiniones.add(opinion);
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
}
