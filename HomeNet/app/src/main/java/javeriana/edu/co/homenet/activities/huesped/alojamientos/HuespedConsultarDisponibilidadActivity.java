package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Disponibilidad;
import javeriana.edu.co.homenet.utils.DateFormater;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;

public class HuespedConsultarDisponibilidadActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference mDataBase;

    TextView nombreAloj;
    MCalendarView calendarView;
    Alojamiento alojamiento;

    Date date = new Date();
    Calendar cal = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_consultar_disponibilidad);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference("Alojamientos/");

        nombreAloj = findViewById(R.id.tvNombreHotelHIAA);
        calendarView = findViewById(R.id.calendar);

        cal.setTime(date);
        calendarView.setMarkedStyle(MarkStyle.BACKGROUND, Color.RED);
        calendarView.unMarkDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH+1)
                ,cal.get(Calendar.DAY_OF_MONTH));

        Intent i = getIntent();
        Bundle b = i.getExtras();

        findAloj(b.getString("idAloj"));
    }

    private void findAloj (final String idAloj) {
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Alojamiento ialojamiento = singleSnapshot.getValue(Alojamiento.class);
                    if (ialojamiento.getId().equals(idAloj)) {
                        alojamiento = ialojamiento;
                    }
                }
                nombreAloj.setText(alojamiento.getNombre());
                llenarCalendario();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }

    private void llenarCalendario (){
        calendarView.getMarkedDates().getAll().clear();
        List<Disponibilidad> ld = alojamiento.getDisponibilidades();
        for (Disponibilidad disp : ld ) {
            String[] fechaInicio = disp.getFechaInicio().split("/");
            String[] fechaFin = disp.getFechaFin().split("/");
            marcasDias(fechaInicio, fechaFin, disp.getFechaInicio());
        }
    }

    private void marcasDias (String[] fInicio, String[] fFin, String fI){
        int diaInicio = Integer.parseInt(fInicio[0]);
        int mesInicio = Integer.parseInt(fInicio[1]);
        int anioInicio = Integer.parseInt(fInicio[2]);
        int diaFin = Integer.parseInt(fFin[0]);
        int mesFin = Integer.parseInt(fFin[1]);
        int anioFin = Integer.parseInt(fFin[2]);
        if (DateFormater.stringToDate(fI).after(DateFormater.today())){
            if (mesFin == mesInicio){
                int difDias = diaFin - diaInicio;
                for (int i = 0; i <= difDias ; i++){
                    calendarView.markDate(anioInicio,mesInicio,diaInicio+i);
                }
            } else if (mesFin > mesInicio){
                if (mesInicio == 1 || mesInicio == 3 || mesInicio == 5 || mesInicio == 7 ||
                        mesInicio == 8 || mesInicio == 10 || mesInicio == 12) {
                    for (int i = 0; i <= 31-diaInicio; i++) {
                        calendarView.markDate(anioInicio, mesInicio, diaInicio+i);
                    }
                }else if(mesInicio == 4 || mesInicio == 6 || mesInicio == 9 || mesInicio == 11){
                    for (int i = 0; i <= 31-diaInicio; i++) {
                        calendarView.markDate(anioInicio, mesInicio, diaInicio+i);
                    }
                }else{
                    for (int i = 0; i <= 28-diaInicio; i++) {
                        calendarView.markDate(anioInicio, mesInicio, diaInicio+i);
                    }
                }
                for (int i = 0; i <= diaFin ; i++){
                    calendarView.markDate(anioInicio,mesFin,1+i);
                }
            }
        }else{
            if (mesFin == mesInicio){
                diaInicio = cal.get(Calendar.DAY_OF_MONTH);
                int difDias = diaFin - diaInicio;
                for (int i = 0; i <= difDias ; i++){
                    calendarView.markDate(anioInicio,mesInicio,diaInicio+i);
                }
            } else if (mesFin > mesInicio){
                if (mesInicio == 1 || mesInicio == 3 || mesInicio == 5 || mesInicio == 7 ||
                        mesInicio == 8 || mesInicio == 10 || mesInicio == 12) {
                    for (int i = 0; i <= 31-diaInicio; i++) {
                        calendarView.markDate(anioInicio, mesInicio, diaInicio+i);
                    }
                }else if(mesInicio == 4 || mesInicio == 6 || mesInicio == 9 || mesInicio == 11){
                    for (int i = 0; i <= 31-diaInicio; i++) {
                        calendarView.markDate(anioInicio, mesInicio, diaInicio+i);
                    }
                }else{
                    for (int i = 0; i <= 28-diaInicio; i++) {
                        calendarView.markDate(anioInicio, mesInicio, diaInicio+i);
                    }
                }
                for (int i = 0; i <= diaFin ; i++){
                    calendarView.markDate(anioInicio,mesFin,1+i);
                }
            }
        }
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
            Intent intent = new Intent(HuespedConsultarDisponibilidadActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
