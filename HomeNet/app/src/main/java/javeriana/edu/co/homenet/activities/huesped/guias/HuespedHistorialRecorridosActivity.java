package javeriana.edu.co.homenet.activities.huesped.guias;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.Tour;
import javeriana.edu.co.homenet.utils.DateFormater;

public class HuespedHistorialRecorridosActivity extends AppCompatActivity {

    public static final String PATH_TOUR="Tours/";

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ArrayList<Tour> listToures = new ArrayList<>();
    private ListView historial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_historial_recorridos);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(PATH_TOUR);

        historial = findViewById(R.id.lvHistorialRecorridosHHRA);
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
