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
    List<DateData> disponibles= new ArrayList<>();
    List<DateData> resultadosProximosDias = new ArrayList<>();
    Alojamiento alojamiento;
    Reserva reserva;
    int indiceReserva = 1;
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

        //findAloj(bundle.getString("idAloj"));
        findAloj(getIntent().getStringExtra("idAloj"));
        ver_ruta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedReservarAlojamientoActivity.this,HuespedVerRutaDestinoActivity.class);
                startActivity(intent);
            }
        });
        reservar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!desde.getText().toString().equals("") && !hasta.getText().toString().equals("")){
                    nProgressDialog.setMessage("Realizando la reserva...");
                    nProgressDialog.show();
                    indiceReserva();
                    //guardarReserva();
                }else{
                    Toast.makeText(HuespedReservarAlojamientoActivity.this, "Debe seleccionar una fecha de inicio y fin para realizar la reserva", Toast.LENGTH_SHORT).show();
                }

            }
        });
        calendario.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                String fecha ="";
                System.out.println("Esto es fechaInicial ------------------------------> "+fechaInicial);
                System.out.println("Esto es fechaFinal ---------------------------------> "+fechaFinal);
                /*System.out.println("Tam de disponibles ------------------------------> "+disponibles.size());
                System.out.println("Cliquee este "+date.getDayString()+"/"+date.getMonthString()+"/"+String.valueOf(date.getYear()));
                for(int i=0;i<disponibles.size();i++){
                    System.out.println("En la lista estoy en ----------------------------------> "+disponibles.get(i).getDayString()+"/"+disponibles.get(i).getMonthString()+"/"+String.valueOf(disponibles.get(i).getYear()));
                    if(disponibles.get(i).equals(date)){
                        System.out.println("Hay uno igual <------------------>");
                    }
                }
                System.out.println("Mirar el contains -------------------> "+disponibles.contains(date));
                */

                if(disponibles.contains(date)){
                    if(fechaInicial && fechaFinal){
                        System.out.println("Entro fecha inicial y final son true ------------------------------> ");
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
                        System.out.println("Entro fechaInicial es false------------------------------------> ");
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
                        proximoDiaDisponible(fechaDesde);
                        calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.DOT,Color.GREEN));
                        precio.setText("");

                    }else if(!fechaFinal){
                        System.out.println("Entro fechaFinal es false---------------------------------------->");
                        int diaDesde=fechaDesde.getDay();
                        int mesDesde=fechaDesde.getMonth();
                        int anioDesde=fechaDesde.getYear();
                        int diaSelec=date.getDay();
                        int mesSelec=date.getMonth();
                        int anioSelec=date.getYear();
                        System.out.println("Dia selec--------------> "+date.getDayString()+"/"+date.getMonthString()+"/"+String.valueOf(date.getYear()));
                        System.out.println("Dia desde --------------->  "+diaDesde+"/"+mesDesde+"/"+anioDesde);
                        if(anioDesde<anioSelec){
                            if(resultadosProximosDias.contains(date)){
                                System.out.println("Entro cuando el seleccionado es mayor al anterior  --------------> ");
                                fechaFinal=true;
                                fecha = date.getDayString()+"/"+date.getMonthString()+"/"+String.valueOf(date.getYear());
                                hasta.setText(fecha);
                                fechaHasta=date;
                                calendario.unMarkDate(fechaHasta);
                                calendario.markDate(fechaHasta.setMarkStyle(MarkStyle.DOT,Color.GREEN));
                                long precioTotal = calcularPrecio(calcularDias(diaDesde,fechaHasta.getDay(),mesDesde,fechaHasta.getMonth(),anioDesde,fechaHasta.getYear()));
                                precio.setText(String.valueOf(precioTotal));
                            }else{
                                fecha = date.getDayString()+"/"+date.getMonthString()+"/"+String.valueOf(date.getYear());
                                desde.setText(fecha);
                                fechaFinal=false;
                                fechaInicial=true;
                                calendario.unMarkDate(fechaDesde);
                                calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.BACKGROUND,Color.GREEN));
                                fechaDesde=date;
                                calendario.unMarkDate(fechaDesde);
                                calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.DOT,Color.GREEN));
                                proximoDiaDisponible(fechaDesde);

                                //Toast.makeText(HuespedReservarAlojamientoActivity.this, "Seleccione la ultima fecha dentro del rango aceptable", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            if((diaDesde<diaSelec) && (mesDesde<=mesSelec)){
                                if(resultadosProximosDias.contains(date)){
                                    System.out.println("Entro cuando el seleccionado es mayor al anterior  --------------> ");
                                    fechaFinal=true;
                                    fecha = date.getDayString()+"/"+date.getMonthString()+"/"+String.valueOf(date.getYear());
                                    hasta.setText(fecha);
                                    fechaHasta=date;
                                    calendario.unMarkDate(fechaHasta);
                                    calendario.markDate(fechaHasta.setMarkStyle(MarkStyle.DOT,Color.GREEN));
                                    long precioTotal = calcularPrecio(calcularDias(diaDesde,fechaHasta.getDay(),mesDesde,fechaHasta.getMonth(),anioDesde,fechaHasta.getYear()));
                                    precio.setText(String.valueOf(precioTotal));
                                }else{
                                    fechaFinal=false;
                                    fechaInicial=true;
                                    fecha = date.getDayString()+"/"+date.getMonthString()+"/"+String.valueOf(date.getYear());
                                    desde.setText(fecha);
                                    calendario.unMarkDate(fechaDesde);
                                    calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.BACKGROUND,Color.GREEN));
                                    fechaDesde=date;
                                    calendario.unMarkDate(fechaDesde);
                                    calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.DOT,Color.GREEN));
                                    proximoDiaDisponible(fechaDesde);
                                    //Toast.makeText(HuespedReservarAlojamientoActivity.this, "Seleccione la ultima fecha dentro del rango aceptable", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                System.out.println("Entro cuando el dia seleccionado es menor al anterior--------------->");
                                fecha = date.getDayString()+"/"+date.getMonthString()+"/"+String.valueOf(date.getYear());
                                desde.setText(fecha);
                                calendario.unMarkDate(fechaDesde);
                                calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.BACKGROUND,Color.GREEN));
                                fechaDesde=date;
                                calendario.unMarkDate(fechaDesde);
                                calendario.markDate(fechaDesde.setMarkStyle(MarkStyle.DOT,Color.GREEN));
                                precio.setText("");
                                fechaInicial=true;
                                fechaFinal=false;
                                proximoDiaDisponible(fechaDesde);
                            }
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
        mDataBase = FirebaseDatabase.getInstance().getReference("Alojamientos/");
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
        System.out.println("FECHA DE INICIO ----------------------------------> "+fechaInicio[0]+"/"+fechaInicio[1]+"/"+fechaInicio[2]);
        System.out.println("FECHA DE FIN ----------------------------------> "+fechaFin[0]+"/"+fechaFin[1]+"/"+fechaFin[2]);
        DateData dateData;
        if(mesFinal>mesInicio){ //CASO CUANDO EL MES FINAL ES MAYOR QUE EL INICIAL
            System.out.println(" MES MAYOR QUE INICIAL----------------------------------> ");
            Calendar mycal = new GregorianCalendar(anioInicio,mesInicio,1);
            int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int diferenciaMes = mesFinal-mesInicio;
            for(int i=diaInicio;i<=diasMes;i++){ //LLENO PRIMERO LOS DIAS DEL MES INICIAL
                dateData = new DateData(anioInicio,mesInicio,i);
                if(verificarMayorDiaActual(dateData)){
                    disponibles.add(dateData);
                    calendario.unMarkDate(dateData);
                    calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.GREEN));
                }
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
                System.out.println(" ANIO FINAL MAYOR QUE INICIAL----------------------------------> ");
                Calendar mycal = new GregorianCalendar(anioInicio,mesInicio,1);
                int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                //Recorro primero los meses desde mesInicio hasta el mes 12
                for(int i=diaInicio;i<=diasMes;i++){ //LLENO PRIMERO LOS DIAS DE MES INICIAL
                    dateData = new DateData(anioInicio,mesInicio,i);
                    if(verificarMayorDiaActual(dateData)){
                        disponibles.add(dateData);
                        calendario.unMarkDate(dateData);
                        calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.GREEN));
                    }
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
                System.out.println(" MES INICIO Y FINAL SON LOS MISMOS ----------------------------------> ");
                for(int i = diaInicio;i<=diaFinal;i++){
                    System.out.println("Esto es i ------------------------------> "+i);
                    dateData = new DateData(anioInicio,mesInicio,i);
                    if(verificarMayorDiaActual(dateData)){
                        System.out.println("Agrego ------------------------> "+dateData.getDay()+"/"+dateData.getMonth()+"/"+dateData.getYear());
                        disponibles.add(dateData);
                        calendario.unMarkDate(dateData);
                        calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.GREEN));
                    }

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
                if(verificarMayorDiaActual(dateData)){
                    if(disponibles.contains(dateData)){
                        disponibles.remove(dateData);
                    }
                    calendario.unMarkDate(dateData);
                    calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.RED));
                }
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
                    if(verificarMayorDiaActual(dateData)){
                        if(disponibles.contains(dateData)){
                            disponibles.remove(dateData);
                        }
                        calendario.unMarkDate(dateData);
                        calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.RED));
                    }
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
                    if(verificarMayorDiaActual(dateData)){
                        if(disponibles.contains(dateData)){
                            disponibles.remove(dateData);
                        }
                        calendario.unMarkDate(dateData);
                        calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, Color.RED));
                    }

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
                if(verificarMayorDiaActual(dateData)){
                    disponibles.add(dateData);
                    calendario.unMarkDate(dateData);
                    calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
                }
            }
        }
        else{
            Calendar mycal = new GregorianCalendar(anioFin,mesFin,1);
            int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for(int i=1;i<=diasMes;i++){
                dateData = new DateData(anioFin,mesFin,i);
                if(verificarMayorDiaActual(dateData)){
                    disponibles.add(dateData);
                    calendario.unMarkDate(dateData);
                    calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
                }

            }
        }
    }
    public void marcarxMesReservados(int diaFin,int mesFin,int anioFin, boolean esFinal, int color){
        DateData dateData;
        if(esFinal){
            for(int i=1;i<=diaFin;i++){
                dateData = new DateData(anioFin,mesFin,i);
                if(verificarMayorDiaActual(dateData)){
                    if(disponibles.contains(dateData)){
                        disponibles.remove(dateData);
                    }
                    calendario.unMarkDate(dateData);
                    calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
                }

            }
        }
        else{
            Calendar mycal = new GregorianCalendar(anioFin,mesFin,1);
            int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for(int i=1;i<=diasMes;i++){ //LLENA TODOS LOS DIAS DE UN MES DETERMINADO
                dateData = new DateData(anioFin,mesFin,i);
                if(verificarMayorDiaActual(dateData)){
                    if(disponibles.contains(dateData)){
                        disponibles.remove(dateData);
                    }
                    calendario.unMarkDate(dateData);
                    calendario.markDate(dateData.setMarkStyle(MarkStyle.BACKGROUND, color));
                }

            }
        }
    }
    public void indiceReserva(){
        mDataBase = FirebaseDatabase.getInstance().getReference("Reservas/");
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Reserva ireserva = singleSnapshot.getValue(Reserva.class);
                    indiceReserva++;
                }
                guardarReserva();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }
    public void guardarReserva(){
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        Random random = new Random();
        int numRandom = random.nextInt(10000)+1;
        final String idReserva = "Reserva "+String.valueOf(indiceReserva);
        reserva = new Reserva();
        reserva.setId(idReserva);
        reserva.setAlojamiento(alojamiento.getId());
        reserva.setFechaInicio(desde.getText().toString());
        reserva.setFechaFin(hasta.getText().toString());
        reserva.setHuesped(uid);
        reserva.setEstado("ACTIVO");
        int anio = Calendar.getInstance().get(Calendar.YEAR); // Variable que contiene el aÃ±o actual
        int mes = (Calendar.getInstance().get(Calendar.MONTH))+1; // Variable que contiene el mes actual
        int dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        reserva.setFechaOperacion(String.valueOf(dia)+"/"+String.valueOf(mes)+"/"+String.valueOf(anio));
        FirebaseDatabase.getInstance().getReference("Reservas").child(idReserva).setValue(reserva).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    usr.agregarReserva(idReserva,true);
                    mDataBase.child(mAuth.getCurrentUser().getUid()).setValue(usr);
                    agregarDatosAlojamientoReserva(idReserva);
                }
                else{
                    nProgressDialog.dismiss();
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
                    aloj.agregarReserva(reserva.getId(),true);
                    mDataBase.child(alojamiento.getId()).setValue(aloj);
                    nProgressDialog.dismiss();
                    Toast.makeText(HuespedReservarAlojamientoActivity.this, "Reserva realizada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HuespedReservarAlojamientoActivity.this,HuespedDetallesHistorialReservaActivity.class);
                    intent.putExtra("idReserva",idReserva);
                    startActivity(intent);
                    finish();

                }
                else{
                    nProgressDialog.dismiss();
                    Toast.makeText(HuespedReservarAlojamientoActivity.this, "No se encontro un alojamiento para asociar la reserva", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public int calcularDias(int diaInicio, int diaFinal, int mesInicio, int mesFinal, int anioInicio, int anioFinal){
        int dias = 1;
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
                dias= (diaFinal-diaInicio)+1;
            }
        }
        return dias;
    }
    public long calcularPrecio(int dias){
        long precioAloj = alojamiento.getPrecio();
        return dias*precioAloj;
    }
    //Comparar el dia actual a lo que se va a marcar
    public boolean verificarMayorDiaActual(DateData fechaVerif){
        int anioActual = Calendar.getInstance().get(Calendar.YEAR); // Variable que contiene el anio actual
        int mesActual = (Calendar.getInstance().get(Calendar.MONTH))+1; // Variable que contiene el mes actual
        int diaActual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        //System.out.println("Fecha actual ------------------> "+diaActual+"/"+mesActual+"/"+anioActual);

        if((anioActual<fechaVerif.getYear())){ //Anio actual es menor que el que llega
            return true;
        }else if(anioActual==fechaVerif.getYear()){
            if(mesActual<fechaVerif.getMonth()){
                return true;
            }else if(mesActual==fechaVerif.getMonth()){
                if(diaActual<=fechaVerif.getDay()){
                    return true;
                }
            }
        }
        return false;
    }
    public List<DateData> proximoDiaDisponible(DateData fechaIn){
        DateData diaIntermedio;
        resultadosProximosDias = new ArrayList<>();
        //Ciclo de años que va a llegar hasta el año 2100
        for(int i=fechaIn.getYear();i<=2100;i++){
            if(i==fechaIn.getYear()){
                //Ciclo de meses, cuando se busca desde el primer anio
                for(int j=fechaIn.getMonth();j<=12;j++){
                    Calendar mycal = new GregorianCalendar(i,j-1,1);
                    int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if(j==fechaIn.getMonth()){
                        for(int k=fechaIn.getDay();k<=diasMes;k++){
                            diaIntermedio= new DateData(i,j,k);
                            if(disponibles.contains(diaIntermedio)){
                                //System.out.println("Dia intermedio entre los disponibles -----------> "+diaIntermedio.getDay()+"/"+diaIntermedio.getMonth()+"/"+diaIntermedio.getYear());
                                resultadosProximosDias.add(diaIntermedio);
                            }else{
                                //System.out.println("11111111111111111111No esta el ----------> Dia intermedio entre los disponibles -----------> "+diaIntermedio.getDay()+"/"+diaIntermedio.getMonth()+"/"+diaIntermedio.getYear());
                                return resultadosProximosDias;
                            }
                        }
                    }else{
                        for (int k=1;k<=diasMes;k++){
                            diaIntermedio= new DateData(i,j,k);
                            if(disponibles.contains(diaIntermedio)){
                                //System.out.println("Dia intermedio entre los disponibles -----------> "+diaIntermedio.getDay()+"/"+diaIntermedio.getMonth()+"/"+diaIntermedio.getYear());
                                resultadosProximosDias.add(diaIntermedio);
                            }else{
                                //System.out.println("2222222222222222222No esta el ----------> Dia intermedio entre los disponibles -----------> "+diaIntermedio.getDay()+"/"+diaIntermedio.getMonth()+"/"+diaIntermedio.getYear());
                                return resultadosProximosDias;
                            }
                        }
                    }
                }
            }else{
                //Ciclo de meses para los demas anios
                for(int j=1;j<=12;j++)  {
                    Calendar mycal = new GregorianCalendar(i,j-1,1);
                    int diasMes = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for(int k=1;k<=diasMes;k++){
                        diaIntermedio= new DateData(i,j,k);
                        if(disponibles.contains(diaIntermedio)){
                            System.out.println("Dia intermedio entre los disponibles -----------> "+diaIntermedio.getDay()+"/"+diaIntermedio.getMonth()+"/"+diaIntermedio.getYear());
                            resultadosProximosDias.add(diaIntermedio);
                        }else{
                            System.out.println("3333333333333333333333No esta el ----------> Dia intermedio entre los disponibles -----------> "+diaIntermedio.getDay()+"/"+diaIntermedio.getMonth()+"/"+diaIntermedio.getYear());
                            return resultadosProximosDias;
                        }
                    }
                }
            }
        }
        return resultadosProximosDias;
    }

}
