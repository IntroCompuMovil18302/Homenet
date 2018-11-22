package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.adapters.TourGuiaAdapter;
import javeriana.edu.co.homenet.adapters.UserAdapter;
import javeriana.edu.co.homenet.models.HistoricoTour;
import javeriana.edu.co.homenet.models.Tour;
import javeriana.edu.co.homenet.models.Usuario;
import javeriana.edu.co.homenet.utils.DateFormater;

public class GuiaUsuariosTourActivity extends AppCompatActivity {

    public static final String PATH_TOURS="Tours/";
    public static final String PATH_HIST_TOURS="HistorialToures/";
    public static final String PATH_USERS="users/";

    private String idTour;
    private Tour tour;
    private String lastTour;

    BottomNavigationView navigation;
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tours:
                    item.setIntent(new Intent(GuiaUsuariosTourActivity.this, GuiaPrincipalActivity.class));
                    return true;
                case R.id.navigation_new_tour:
                    item.setIntent(new Intent(GuiaUsuariosTourActivity.this, GuiaCrearTourActivity.class));
                    return true;
                case R.id.navigation_history_tours:
                    return true;
                case R.id.navigation_config:
                    return true;
            }
            return false;
        }
    };

    ListView usuarios;
    List<Usuario> listUsers = new ArrayList<Usuario>();

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_usuarios_tour);

        database = FirebaseDatabase.getInstance();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        usuarios = findViewById(R.id.lvUsuariosGUT);

        Intent bA = getIntent();
        Bundle b = bA.getExtras();
        idTour = b.getString("idTour");
        findTour();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        consultarUsuarios();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserAdapter adapter = new UserAdapter(GuiaUsuariosTourActivity.this, (ArrayList<Usuario>) listUsers);
        usuarios.setAdapter(adapter);

    }

    private void findTour() {
        myRef = database.getReference(PATH_TOURS/*+"/"+idTour*/);
        myRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Tour itour = singleSnapshot.getValue(Tour.class);
                    if (singleSnapshot.getKey().equals(idTour)) {
                        tour = itour;
                        lastTour = tour.getHistorialTour().get(tour.getHistorialTour().size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }


    private void consultarUsuarios(){
        myRef = database.getReference(PATH_HIST_TOURS).child(lastTour);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HistoricoTour ht = dataSnapshot.getValue(HistoricoTour.class);
                for (Map.Entry<String, Boolean> entry : ht.getUsuarios().entrySet())
                {
                    obtenerUsuario(entry.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }

    private void obtenerUsuario(String idUser){
        myRef = database.getReference(PATH_USERS).child(idUser);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario u = dataSnapshot.getValue(Usuario.class);
                listUsers.add(u);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }
}
