package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.adapters.TourGuiaAdapter;
import javeriana.edu.co.homenet.models.HistoricoTour;
import javeriana.edu.co.homenet.models.Tour;
import javeriana.edu.co.homenet.utils.DateFormater;

public class GuiaHistorialToures extends AppCompatActivity {

    public static final String PATH_TOURS="Tours/";
    public static final String PATH_HIST_TOURS="HistorialToures/";

    BottomNavigationView navigation;
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tours:
                    item.setIntent(new Intent(GuiaHistorialToures.this, GuiaPrincipalActivity.class));
                    return true;
                case R.id.navigation_new_tour:
                    item.setIntent(new Intent(GuiaHistorialToures.this, GuiaCrearTourActivity.class));
                    return true;
                case R.id.navigation_history_tours:
                    return true;
                case R.id.navigation_config:
                    item.setIntent(new Intent(GuiaHistorialToures.this, GuiaCalificacionesActivity.class));
                    return true;
            }
            return false;
        }
    };

    ListView historial;

    private ArrayList<Tour> listToures = new ArrayList<>();
    private ArrayList<HistoricoTour> listHistorico = new ArrayList<>();
    private Map<String, Tour> toures;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myRefHist;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_historial_toures);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_history_tours);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        historial = findViewById(R.id.lvHistorialGHT);
        consultarHistorialGuia();



    }

    private void consultarHistorialGuia(){
        final FirebaseUser user = mAuth.getCurrentUser();
        myRef = database.getReference(PATH_HIST_TOURS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listToures.clear();
                listHistorico.clear();

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d("Tour", singleSnapshot.getKey());
                    HistoricoTour ihtour = singleSnapshot.getValue(HistoricoTour.class);
                    Log.d("GUIAAAA", ihtour.getIdGuia());
                    if (ihtour.getIdGuia().equals(user.getUid()) &&
                            DateFormater.stringToDate(ihtour.getFecha()).before(DateFormater.today())){
                        consultarTour(ihtour.getTour(), ihtour.getHora(), ihtour.getFecha(), ihtour.getMoneda(),
                                ihtour.getPrecio());
                        listHistorico.add(ihtour);
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TourGuiaAdapter adapter = new TourGuiaAdapter(GuiaHistorialToures.this, listToures);
                historial.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }

    private void consultarTour(String ht, final String hora, final String fecha, final String moneda, final int precio){
        myRefHist = database.getReference(PATH_TOURS).child(ht);
        myRefHist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tour t = dataSnapshot.getValue(Tour.class);
                t.setHora(hora);
                t.setFecha(fecha);
                t.setPrecio(precio);
                t.setMoneda(moneda);
                listToures.add(t);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
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
            Intent intent = new Intent(GuiaHistorialToures.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
