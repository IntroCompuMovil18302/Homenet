package javeriana.edu.co.homenet.activities.guia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.activities.huesped.alojamientos.HuespedInformacionAlojamientoActivity;
import javeriana.edu.co.homenet.activities.huesped.alojamientos.HuespedResultadosListActivity;
import javeriana.edu.co.homenet.adapters.AlojamientoAdapter;
import javeriana.edu.co.homenet.adapters.TourGuiaAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Tour;
import javeriana.edu.co.homenet.utils.DateFormater;

public class GuiaPrincipalActivity extends AppCompatActivity {

    public static final String PATH_TOURS="Tours/";

    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tours:
                    return true;
                case R.id.navigation_new_tour:
                    item.setIntent(new Intent(GuiaPrincipalActivity.this, GuiaCrearTourActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    return true;
                case R.id.navigation_history_tours:
                    item.setIntent(new Intent(GuiaPrincipalActivity.this, GuiaHistorialToures.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    return true;
                case R.id.navigation_config:
                    item.setIntent(new Intent(GuiaPrincipalActivity.this, GuiaCalificacionesActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    return true;
            }
            return false;
        }
    };

    ListView toures;
    EditText keyword;
    Spinner orden;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    private ArrayList<Tour> listToures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_principal);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        toures = findViewById(R.id.lvTouresGP);
        keyword = findViewById(R.id.etKeywordGP);
        orden = findViewById(R.id.spOrdenGP);

        /*
        Intent intentReserva = new Intent(GuiaPrincipalActivity.this,ReservaTourService.class);
        startService(intentReserva);

        Intent intentCalificacion = new Intent(GuiaPrincipalActivity.this,CalificacionGuiaService.class);
        startService(intentCalificacion);
*/
        toures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putString("idTour", listToures
                        .get(position).getId());
                startActivity(new Intent(view.getContext(),GuiaDetalleTourActivity.class).putExtras(b));
            }
        });

        orden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = String.valueOf(adapterView.getItemAtPosition(i));
                if(selected.toUpperCase().contains("Titulo".toUpperCase())){
                    Collections.sort(listToures, new Comparator<Tour>() {
                        @Override
                        public int compare(Tour a1, Tour a2) {
                            return a1.getTitulo().compareToIgnoreCase(a2.getTitulo());

                        }
                    });
                    TourGuiaAdapter adapter = new TourGuiaAdapter(GuiaPrincipalActivity.this, listToures);
                    updateView(adapter);
                }else if(selected.toUpperCase().contains("Fecha".toUpperCase())){
                    Collections.sort(listToures, new Comparator<Tour>() {
                        @Override
                        public int compare(Tour a1, Tour a2) {
                            Date d1 = DateFormater.stringToDate(a1.getFecha());
                            Date d2 = DateFormater.stringToDate(a2.getFecha());
                            return (d1.getTime() > d2.getTime() ? -1 : 1);
                        }
                    });
                    TourGuiaAdapter adapter = new TourGuiaAdapter(GuiaPrincipalActivity.this, listToures);
                    updateView(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //NOTHING
            }
        });

        keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE){
                    String keysearch = keyword.getText().toString();
                    String keysearch_spaces = keysearch.replaceAll(" ", "");
                    if (keysearch_spaces == "") {
                        updateView(new TourGuiaAdapter(GuiaPrincipalActivity.this, listToures));
                    }else{
                        consultarTouresPorTitulo(keyword.getText().toString());
                    }
                }
                return false;
            }
        });

        consultarTouresGuia();
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
            /*
            Intent intentStop = new Intent(GuiaPrincipalActivity.this,ReservaTourService.class);
            stopService(intentStop);
            Intent intentOpinionStop =  new Intent (GuiaPrincipalActivity.this,CalificacionGuiaService.class);
            stopService(intentOpinionStop);*/
            Intent intent = new Intent(GuiaPrincipalActivity.this, LoginActivity.class);
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

    private void consultarTouresGuia(){
        final FirebaseUser user = mAuth.getCurrentUser();
        myRef = database.getReference(PATH_TOURS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listToures.clear();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Tour itour = singleSnapshot.getValue(Tour.class);
                    itour.setId(singleSnapshot.getKey());
                    if (itour.getIdGuia().equals(user.getUid())){
                        listToures.add(itour);
                    }
                }
                Collections.sort(listToures, new Comparator<Tour>() {
                    @Override
                    public int compare(Tour a1, Tour a2) {
                        return a1.getTitulo().compareToIgnoreCase(a2.getTitulo());

                    }
                });
                TourGuiaAdapter adapter = new TourGuiaAdapter(GuiaPrincipalActivity.this, listToures);
                updateView(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }


    /**
     * Get tours with specific title
     * @param titulo
     */
    private void consultarTouresPorTitulo(String titulo){
        ArrayList<Tour> listTouresTemp = new ArrayList<>();
        for(Tour tour:listToures){
            if(tour.getTitulo().toLowerCase().contains(titulo.toLowerCase())){
                listTouresTemp.add(tour);
            }
        }
        TourGuiaAdapter adapter = new TourGuiaAdapter(GuiaPrincipalActivity.this, listTouresTemp);
        updateView(adapter);
    }

    /**
     * Update list view, using custom adapter.
     * @param adapter
     */
    private void updateView(TourGuiaAdapter adapter){
        toures.setAdapter(adapter);
    }


}
