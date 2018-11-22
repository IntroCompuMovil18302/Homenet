package javeriana.edu.co.homenet.activities.huesped.guias;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.adapters.TourGuiaAdapter;
import javeriana.edu.co.homenet.models.HistoricoTour;
import javeriana.edu.co.homenet.models.Tour;
import javeriana.edu.co.homenet.utils.DateFormater;

public class HuespedHistorialRecorridosActivity extends AppCompatActivity {

    public static final String PATH_TOUR_HISTO="HistorialToures/";
    public static final String PATH_TOUR="Tours/";

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;

    private ArrayList<HistoricoTour> listToures = new ArrayList<>();
    private ListView historial;
    private ArrayList<Tour> tt = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_historial_recorridos);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(PATH_TOUR_HISTO);

        historial = findViewById(R.id.lvHistorialRecorridosHHRA);

        buscarHistorial();
    }

    private void buscarHistorial(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    HistoricoTour hT = singleSnapshot.getValue(HistoricoTour.class);
                    if (hT.getUsuarios().get(mAuth.getUid()) != null){
                        listToures.add(hT);
                    }
                }
                myRef2 = database.getReference(PATH_TOUR);
                for (final HistoricoTour h: listToures){
                    myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                Tour t = singleSnapshot.getValue(Tour.class);
                                if (singleSnapshot.getKey().equals(h.getTour())){
                                    tt.add(t);
                                }
                            }
                            TourGuiaAdapter tga = new TourGuiaAdapter(HuespedHistorialRecorridosActivity.this
                            , tt);
                            historial.setAdapter(tga);
                            historial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Bundle x = new Bundle();
                                    x.putString("idGuia", h.getIdGuia());
                                    startActivity(new Intent(HuespedHistorialRecorridosActivity.this,
                                            HuespedCalificarGuiaActivity.class).putExtras(x));
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean matchTour (Tour t) {
        boolean match = true;
        Date d = DateFormater.today();
        Date d2 = DateFormater.stringToDate(t.getFecha());
        Date h = DateFormater.today();
        Date h2 = DateFormater.stringToHour(t.getHora());
        if(d.before(d2) && h.after(h2)){
            return match;
        }
        return false;
    }
}
