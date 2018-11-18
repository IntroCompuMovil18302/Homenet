package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Disponibilidad;
import javeriana.edu.co.homenet.models.Reserva;
import javeriana.edu.co.homenet.models.Usuario;
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
    Reserva reserva;
    private FirebaseAuth mAuth;
    DatabaseReference mDataBase;
    private ProgressDialog nProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_reservar_alojamiento);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference("Alojamientos/");
        nProgressDialog = new ProgressDialog(HuespedReservarAlojamientoActivity.this);

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
                    nProgressDialog.setMessage("Realizando la reserva...");
                    nProgressDialog.show();
                    guardarReserva();
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
                        precio.setText("");
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
                        precio.setText("");

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
                            long precioTotal = calcularPrecio(calcularDias(diaDesde,fechaHasta.getDay(),mesDesde,fechaHasta.getMonth(),anioDesde,fechaHasta.getYear()));
                            precio.setText(String.valueOf(precioTotal));
                        }else{
                            fecha = date.getDayString()+"/"+date.getMonthString()+"/"+String.valueOf(date.getYear());
                            desde.setText(fecha);
                            calendario.unMarkDate(fechaDesde);
                            calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.BACKGROUND,Color.GREEN));
                            fechaDesde=date;
                            calendario.unMarkDate(fechaDesde);
                            calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.DOT,Color.GREEN));
                            precio.setText("");
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
                encontrarReservas(idAloj);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
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
        if(mesFinal>mesInicio){ //CASO CUANDO EL MES FINAL ES MAYOR QUE EL INICIAL
            Calendar mycal = new GregorianCalendar(anioInicio,mesInicio,1);
            int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int diferenciaMes = mesFinal-mesInicio;
            for(int i=diaInicio;i<=diasMes;i++){ //LLENO PRIMERO LOS DIAS DEL MES INICIAL
                dateData = new DateData(anioInicio,mesInicio,i);
                disponibles.add(dateData);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.GREEN));
            }
            //LLENO LOS DIAS DE LOS DEMAS MESES
            for(int i=1;i<=diferenciaMes;i++){
                if(i==diferenciaMes){
                    marcarxMesDisponibles(diaFinal,mesFinal+i,anioFinal, true,Color.GREEN);
                }else{
                    marcarxMesDisponibles(diaFinal,mesFinal+i,anioFinal, false,Color.GREEN);
                }
            }
        }
        else{
            if(anioFinal>anioInicio){ // CASO CUANDO EL ANIO FINAL ES MAYOR QUE EL INICIAL
                Calendar mycal = new GregorianCalendar(anioInicio,mesInicio,1);
                int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                //Recorro primero los meses desde mesInicio hasta el mes 12
                for(int i=diaInicio;i<=diasMes;i++){ //LLENO PRIMERO LOS DIAS DE MES INICIAL
                    dateData = new DateData(anioInicio,mesInicio,i);
                    disponibles.add(dateData);
                    calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.GREEN));
                }
                //LLENO LOS MESES RESTANTES DEL ANIO INICIAL
                for(int i=mesInicio+1;i<=12;i++){
                    marcarxMesDisponibles(diaInicio,i,anioInicio,false,Color.GREEN);
                }
                //Despues recorro los meses desde el primero hasta el mesFinal
                for(int i=1;i<=mesFinal;i++){
                    if(i==mesFinal){
                        marcarxMesDisponibles(diaFinal,i,anioFinal,true,Color.GREEN);
                    }else{
                        marcarxMesDisponibles(diaFinal,i,anioFinal,false,Color.GREEN);
                    }

                }
            }else{ // CASO CUANDO EL MES INICIO Y EL FINAL SON LOS MISMOS
                for(int i = diaInicio;i<=diaFinal;i++){
                    dateData = new DateData(anioInicio,mesInicio,i);
                    disponibles.add(dateData);
                    calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.GREEN));
                }
            }
        }

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
            Calendar mycal = new GregorianCalendar(anioInicio,mesInicio,1);
            int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
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
            if(anioFinal>anioInicio){ // CASO CUANDO EL ANIO FINAL ES MAYOR QUE EL INICIAL
                Calendar mycal = new GregorianCalendar(anioInicio,mesInicio,1);
                int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                //Recorro primero los meses desde mesInicio hasta el mes 12
                for(int i=diaInicio;i<diasMes;i++){ //LLENO PRIMERO LOS DIAS DE MES INICIAL
                    dateData = new DateData(anioInicio,mesInicio,i);
                    disponibles.add(dateData);
                    calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.RED));
                }
                //LLENO LOS MESES RESTANTES DEL ANIO INICIAL
                for(int i=mesInicio+1;i<=12;i++){
                    marcarxMesReservados(diaInicio,i,anioInicio,false,Color.RED);
                }
                //Despues recorro los meses desde el primero hasta el mesFinal
                for(int i=1;i<=mesFinal;i++){
                    if(i==mesFinal){
                        marcarxMesReservados(diaFinal,i,anioFinal,true,Color.RED);
                    }else{
                        marcarxMesReservados(diaFinal,i,anioFinal,false,Color.RED);
                    }

                }
            }else{ // CASO CUANDO EL MES INICIO Y EL FINAL SON LOS MISMOS
                for(int i = diaInicio;i<=diaFinal;i++){
                    dateData = new DateData(anioInicio,mesInicio,i);
                    disponibles.add(dateData);
                    calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.RED));
                }
            }
        }
    }
    //Marca la disponibilidad de dias cuando el mes de la fecha final se encuentra en un diferente mes que la inicial
    public void marcarxMesDisponibles(int diaFin,int mesFin,int anioFin, boolean esFinal, int color){
        DateData dateData;
        if(esFinal){
            for(int i=1;i<=diaFin;i++){
                dateData = new DateData(anioFin,mesFin,i);
                disponibles.add(dateData);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
            }
        }
        else{
            Calendar mycal = new GregorianCalendar(anioFin,mesFin,1);
            int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for(int i=1;i<=diasMes;i++){
                dateData = new DateData(anioFin,mesFin,i);
                disponibles.add(dateData);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
            }
        }
    }
    public void marcarxMesReservados(int diaFin,int mesFin,int anioFin, boolean esFinal, int color){
        DateData dateData;
        if(esFinal){
            for(int i=1;i<=diaFin;i++){
                dateData = new DateData(anioFin,mesFin,i);
                disponibles.remove(dateData);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
            }
        }
        else{
            Calendar mycal = new GregorianCalendar(anioFin,mesFin,1);
            int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for(int i=1;i<=diasMes;i++){ //LLENA TODOS LOS DIAS DE UN MES DETERMINADO
                dateData = new DateData(anioFin,mesFin,i);
                disponibles.remove(dateData);
                calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
            }
        }
    }
    public void guardarReserva(){
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        Random random = new Random();
        int numRandom = random.nextInt(10000)+1;
        final String idReserva = "Reserva "+String.valueOf(numRandom)+String.valueOf(System.currentTimeMillis());
        reserva = new Reserva();
        reserva.setId(idReserva);
        reserva.setAlojamiento(alojamiento.getId());
        reserva.setFechaInicio(desde.getText().toString());
        reserva.setFechaFin(hasta.getText().toString());
        reserva.setHuesped(uid);
        int anio = Calendar.getInstance().get(Calendar.YEAR); // Variable que contiene el aÃ±o actual
        int mes = (Calendar.getInstance().get(Calendar.MONTH))+1; // Variable que contiene el mes actual
        int dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        reserva.setFechaOperacion(String.valueOf(dia)+"/"+String.valueOf(mes)+"/"+String.valueOf(anio));
        FirebaseDatabase.getInstance().getReference("Reservas").child(idReserva).setValue(alojamiento).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //progressbar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    agregarDatosUsuarioReserva(idReserva);
                }
                else{
                    nProgressDialog.dismiss();
                    Toast.makeText(HuespedReservarAlojamientoActivity.this,"Hubo un error al crear un servicio", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
    public void agregarDatosUsuarioReserva(final String idReserva){
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        mDataBase = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    Usuario usr = dataSnapshot.getValue(Usuario.class);
                    usr.agregarReserva(idReserva);
                    mDataBase.child(mAuth.getCurrentUser().getUid()).setValue(usr);
                    agregarDatosAlojamientoReserva(idReserva);
                }
                else{
                    Toast.makeText(HuespedReservarAlojamientoActivity.this, "No se encontro un usuario para asociar la reserva", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void agregarDatosAlojamientoReserva(final String idReserva){
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        mDataBase = FirebaseDatabase.getInstance().getReference("Alojamientos");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Alojamientos").child(alojamiento.getId());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    Alojamiento  aloj= dataSnapshot.getValue(Alojamiento.class);
                    aloj.agregarReserva(reserva);
                    mDataBase.child(alojamiento.getId()).setValue(aloj);
                    Toast.makeText(HuespedReservarAlojamientoActivity.this, "Reserva realizada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HuespedReservarAlojamientoActivity.this,HuespedDetallesHistorialReservaActivity.class);
                    intent.putExtra("idReserva",idReserva);
                    startActivity(intent);
                    finish();

                }
                else{
                    Toast.makeText(HuespedReservarAlojamientoActivity.this, "No se encontro un alojamiento para asociar la reserva", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public int calcularDias(int diaInicio, int diaFinal, int mesInicio, int mesFinal, int anioInicio, int anioFinal){
        int dias = 0;
        Calendar mycal;
        int diasMes=0;
        if(anioFinal>anioInicio){
            //Primer el mes actual
            mycal = new GregorianCalendar(anioInicio,mesInicio,1);
            diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for(int i=diaInicio;i<=diasMes;i++)
                dias++;
            //Los meses faltantes del anio
            for(int i=mesInicio+1;i<=12;i++){
                mycal = new GregorianCalendar(anioInicio,i,1);
                diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                for(int j=1;j<diasMes;j++){
                    dias++;
                }
            }
            //Meses del año siguiente
            for(int i=1;i<=mesFinal;i++){
                if(i==mesFinal){
                    for(int j=1;j<diaFinal;j++){
                        dias++;
                    }
                }
                else{
                    mycal = new GregorianCalendar(anioFinal,i,1);
                    diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for(int j=1;j<diasMes;j++){
                        dias++;
                    }
                }
            }
        }else{
            if(mesFinal>mesInicio){
                mycal = new GregorianCalendar(anioInicio,mesInicio,1);
                diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                for(int i=diaInicio;i<=diasMes;i++){
                    dias++;
                }
                for(int i=mesInicio+1;i<=mesFinal;i++){
                    if(i==mesFinal){
                        for(int j=1;j<=diaFinal;j++){
                            dias++;
                        }
                    }else{
                        mycal = new GregorianCalendar(anioInicio,i,1);
                        diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        for(int j=1;j<=diasMes;j++){
                            dias++;
                        }
                    }
                }
            }else{
                dias= diaFinal-diaInicio;
            }
        }
        return dias;
    }
    public long calcularPrecio(int dias){
        long precioAloj = alojamiento.getPrecio();
        return dias*precioAloj;
    }
}
