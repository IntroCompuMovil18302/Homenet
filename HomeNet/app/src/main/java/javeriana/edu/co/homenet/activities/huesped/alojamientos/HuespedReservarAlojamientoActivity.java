package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Disponibilidad;
import javeriana.edu.co.homenet.models.Reserva;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class HuespedReservarAlojamientoActivity extends AppCompatActivity {
    Button ver_ruta;
    Button reservar;
    TextView desde;
    TextView hasta;
    TextView precio;
    MCalendarView calendario;
    boolean fechaInicial=false;
    boolean fechaFinal=false;
    DateData fechaDesde ;
    DateData fechaHasta;
    Bundle bundle;
    List<DateData> disponibles= new ArrayList<>();
    Alojamiento alojamiento;

    private FirebaseAuth mAuth;
    DatabaseReference mDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_reservar_alojamiento);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference("Alojamientos/");

        desde = (TextView)findViewById(R.id.tvDesdeHRA);
        hasta = (TextView)findViewById(R.id.tvHastaHRA);
        precio = (TextView)findViewById(R.id.tvPrecioHRA);
        ver_ruta=(Button)findViewById(R.id.btRutaHRA);
        calendario = (MCalendarView)findViewById(R.id.mcvReservarAlojHRA);
        reservar=(Button)findViewById(R.id.btReservarHRA);


        bundle= getIntent().getExtras();
        findAloj(bundle.getString("idAloj"));
        ver_ruta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedReservarAlojamientoActivity.this,HuespedVerRutaActivity.class);
                startActivity(intent);
            }
        });
        reservar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!desde.getText().toString().equals("") && !hasta.getText().toString().equals("")){
                    Toast.makeText(HuespedReservarAlojamientoActivity.this, "Reserva realizada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HuespedReservarAlojamientoActivity.this,HuespedDetallesHistorialReservaActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(HuespedReservarAlojamientoActivity.this, "Debe seleccionar una fecha de inicio y fin para realizar la reserva", Toast.LENGTH_SHORT).show();
                }

            }
        });
        calendario.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                String fecha ="";
                if(!disponibles.contains(date)){
                    if(fechaInicial && fechaFinal){
                        fechaInicial=false;
                        fechaFinal=false;
                        desde.setText("");
                        hasta.setText("");
                        calendario.unMarkDate(fechaDesde);
                        calendario.unMarkDate(fechaHasta);
                        calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.BACKGROUND,Color.GREEN));
                        calendario.markDate(fechaHasta.setMarkStyle(MarkStyle.BACKGROUND,Color.GREEN));
                    }
                    if(!fechaInicial){
                        fechaInicial=true;
                        fecha = date.getDayString()+"/"+date.getMonthString()+"/"+String.valueOf(date.getYear());
                        desde.setText(fecha);
                        calendario.unMarkDate(date);
                        if(fechaDesde!=null){
                            calendario.unMarkDate(fechaDesde);
                            calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.BACKGROUND,Color.GREEN));
                            if(fechaHasta!=null){
                                calendario.unMarkDate(fechaHasta);
                                calendario.markDate(fechaHasta.setMarkStyle(MarkStyle.BACKGROUND,Color.GREEN));
                            }
                        }
                        fechaDesde=date;
                        calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.DOT,Color.GREEN));

                    }else if(!fechaFinal){
                        int diaDesde=fechaDesde.getDay();
                        int mesDesde=fechaDesde.getMonth();
                        int anioDesde=fechaDesde.getYear();
                        int diaSelec=date.getDay();
                        int mesSelec=date.getMonth();
                        int anioSelec=date.getYear();
                        if((diaDesde<diaSelec) && (mesDesde<mesSelec) && (anioDesde<anioSelec)){
                            fechaFinal=true;
                            fecha = date.getDayString()+"/"+date.getMonthString()+"/"+String.valueOf(date.getYear());
                            hasta.setText(fecha);
                            fechaHasta=date;
                            calendario.markDate(fechaHasta.setMarkStyle(MarkStyle.DOT,Color.GREEN));
                        }else{
                            fecha = date.getDayString()+"/"+date.getMonthString()+"/"+String.valueOf(date.getYear());
                            desde.setText(fecha);
                            calendario.unMarkDate(fechaDesde);
                            calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.BACKGROUND,Color.GREEN));
                            fechaDesde=date;
                            calendario.unMarkDate(fechaDesde);
                            calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.DOT,Color.GREEN));
                        }
                    }
                }
                else{
                    Toast.makeText(HuespedReservarAlojamientoActivity.this, "Seleccione una fecha que este resaltada con verde", Toast.LENGTH_SHORT).show();
                }
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
            Intent intent = new Intent(HuespedReservarAlojamientoActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
                llenarDisponibles();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }
    public void llenarDisponibles(){
        calendario.getMarkedDates().getAll().clear();
        List<Disponibilidad> ld = alojamiento.getDisponibilidades();
        for (Disponibilidad disp : ld ) {
            String[] fechaInicio = disp.getFechaInicio().split("/");
            String[] fechaFin = disp.getFechaFin().split("/");
            marcarDias(fechaInicio,fechaFin);
        }
    }
    //Marca los dias disponibles del alojamiento de color verde en el calendario, cuando las fechas estan en el mismo mes
    public void marcarDias(String[] fechaInicio, String[] fechaFin){
        int diaInicio = Integer.parseInt(fechaInicio[0]);
        int mesInicio = Integer.parseInt(fechaInicio[1]);
        int anioInicio = Integer.parseInt(fechaInicio[2]);
        int diaFinal = Integer.parseInt(fechaFin[0]);
        int mesFinal = Integer.parseInt(fechaFin[1]);
        int anioFinal = Integer.parseInt(fechaFin[2]);
        DateData dateData;
        if(mesFinal>mesInicio){
            int diasMes=0; //TODO Hallar los dias de mes del que se esta analizando
            int diferenciaMes = mesFinal-mesInicio;
            for(int i=diaInicio;i<diasMes;i++){
                dateData = new DateData(anioInicio,mesInicio,i);
                disponibles.add(dateData);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.GREEN));
            }
            for(int i=1;i<=diferenciaMes;i++){
                if(i==diferenciaMes){
                    marcarxMesDisponibles(diaFinal,mesFinal+i,anioFinal, true,Color.GREEN);
                }else{
                    marcarxMesDisponibles(diaFinal,mesFinal+i,anioFinal, false,Color.GREEN);
                }


            }

        }
        else{

            for(int i = diaInicio;i<=diaFinal;i++){
                dateData = new DateData(anioInicio,mesInicio,i);
                disponibles.add(dateData);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.GREEN));
            }
        }

    }
    //Marca la disponibilidad de dias cuando el mes de la fecha final se encuentra en un diferente mes que la inicial
    public void marcarxMesDisponibles(int diaFin,int mesFin,int anioFin, boolean esFinal, int color){
        DateData dateData;
        if(esFinal){
            for(int i=1;i<diaFin;i++){
                dateData = new DateData(anioFin,mesFin,i);
                disponibles.add(dateData);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
            }
        }
        else{
            int diasMes=0; //TODO Hallar los dias de mes del que se esta analizando
            for(int i=1;i<diasMes;i++){
                dateData = new DateData(anioFin,mesFin,i);
                disponibles.add(dateData);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
            }
        }
    }
    public void marcarxMesReservados(int diaFin,int mesFin,int anioFin, boolean esFinal, int color){
        DateData dateData;
        if(esFinal){
            for(int i=1;i<diaFin;i++){
                dateData = new DateData(anioFin,mesFin,i);
                disponibles.remove(dateData);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
            }
        }
        else{
            int diasMes=0; //TODO Hallar los dias de mes del que se esta analizando
            for(int i=1;i<diasMes;i++){
                dateData = new DateData(anioFin,mesFin,i);
                disponibles.remove(dateData);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
            }
        }
    }
    public void encontrarReservas(final String idAloj){
        mDataBase = FirebaseDatabase.getInstance().getReference("Reservas/");
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Reserva ireserva = singleSnapshot.getValue(Reserva.class);
                    if (ireserva.getAlojamiento().equals(idAloj)) {
                        String[] fechaInicio = ireserva.getFechaInicio().split("/");
                        String[] fechaFin = ireserva.getFechaFin().split("/");
                        llenarReservadas(fechaInicio,fechaFin);
                    }
                }
                llenarDisponibles();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }
    public void llenarReservadas(String[] fechaInicio, String[] fechaFin){
        int diaInicio = Integer.parseInt(fechaInicio[0]);
        int mesInicio = Integer.parseInt(fechaInicio[1]);
        int anioInicio = Integer.parseInt(fechaInicio[2]);
        int diaFinal = Integer.parseInt(fechaFin[0]);
        int mesFinal = Integer.parseInt(fechaFin[1]);
        int anioFinal = Integer.parseInt(fechaFin[2]);
        DateData dateData;
        if(mesFinal>mesInicio){
            int diasMes=0; //TODO Hallar los dias de mes del que se esta analizando
            int diferenciaMes = mesFinal-mesInicio;
            for(int i=diaInicio;i<diasMes;i++){
                dateData = new DateData(anioInicio,mesInicio,i);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.RED));
            }
            for(int i=1;i<=diferenciaMes;i++){
                if(i==diferenciaMes){
                    marcarxMesReservados(diaFinal,mesFinal+i,anioFinal, true, Color.RED);
                }
                marcarxMesReservados(diaFinal,mesFinal+i,anioFinal, false,Color.RED);
            }

        }
        else{

            for(int i = diaInicio;i<=diaFinal;i++){
                dateData = new DateData(anioInicio,mesInicio,i);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.RED));
            }
        }
    }
}
