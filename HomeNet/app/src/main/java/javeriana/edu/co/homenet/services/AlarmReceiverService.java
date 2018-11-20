package javeriana.edu.co.homenet.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.Nullable;
import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;
import javeriana.edu.co.homenet.models.Reserva;

public class AlarmReceiverService extends BroadcastReceiver {

    public static final int CODE_INTENT = 103;
    private FirebaseAuth mAuth;
    DatabaseReference mDataBase;

    List<Reserva>reservas= new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference("Reservas/");
        encontrarReservas(context,intent);
    }
    private void encontrarReservas (final Context context, final Intent intent) {
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Reserva ireserva = singleSnapshot.getValue(Reserva.class);
                    if (ireserva.getHuesped().equals(mAuth.getCurrentUser().getUid())) {
                        reservas.add(ireserva);
                    }
                }
                mostrarNotificaciones(context,intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }
    public void mostrarNotificaciones(Context context, Intent intent){
        Calendar calendar = Calendar.getInstance();
        Random random = new Random();
        int numRandom = random.nextInt(10000)+1;
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeating_intent = new Intent(context,MenuHuespedActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Avisar 5 dias antes de que sea el momento de la reserva
        int diaAct;
        int mesAct;
        int anioAct;
        for(int i=0;i<reservas.size();i++){
            System.out.println("Entro a el for de reservas ------------------------------------>");
            diaAct = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            mesAct = (Calendar.getInstance().get(Calendar.MONTH))+1;
            anioAct = Calendar.getInstance().get(Calendar.YEAR);
            String [] fechaInicio = reservas.get(i).getFechaInicio().split("/");
            System.out.println("DIA ACTUAL -----------------------------> "+diaAct+"/"+mesAct+"/"+anioAct);
            System.out.println("INICIO DE RESERVA -----------------------------> "+Integer.parseInt(fechaInicio[0])+"/"+Integer.parseInt(fechaInicio[1])+"/"+Integer.parseInt(fechaInicio[2]));
            int diferenciaDias = calcularDias(diaAct,Integer.parseInt(fechaInicio[0]), mesAct,Integer.parseInt(fechaInicio[1]), anioAct,Integer.parseInt(fechaInicio[2]));
            System.out.println("Esta es la diferencia de dias ---------> "+diferenciaDias);
            if(diferenciaDias<=5){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    numRandom = random.nextInt(10000)+1;
                    CharSequence name= "Reserva"+String.valueOf(numRandom);
                    String description = "Reserva descripcion "+String.valueOf(numRandom);
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(String.valueOf(numRandom),name,importance);
                    channel.setDescription(description);
                    Notification notification = new Notification.Builder(context)
                            .setContentTitle("Reserva de alojamiento cercana")
                            .setContentText("Faltan "+ diferenciaDias+" dias para la reserva del alojamiento +"+reservas.get(i).getAlojamiento())
                            .setSmallIcon(R.drawable.boy)
                            .setChannelId("CHANNEL "+String.valueOf(numRandom))
                            .build();
                }else{
                    numRandom = random.nextInt(10000)+1;
                    PendingIntent pendingIntent = PendingIntent.getActivity(context,numRandom,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.drawable.boy)
                            .setContentTitle("Reserva de alojamiento cercana")
                            .setContentText("Faltan "+ diferenciaDias+" dias para la reserva del alojamiento +"+reservas.get(i).getAlojamiento())
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle().
                                    bigText("Faltan "+ diferenciaDias+" dias para la reserva del alojamiento +"+reservas.get(i).getAlojamiento()))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    numRandom = random.nextInt(10000)+1;
                    notificationManager.notify(numRandom,builder.build());
                }

            }
        }
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

    /*
    private static final String CHANNEL_ID = "notification_test";
    private static final int notification_id = 100;


    public AlarmReceiverService() {
        super("AlarmReceiverService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try{
            Thread.sleep(5000);
            Log.i("NOTIFICACION","Jojojojo sirvioo");
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.bathtub)
                    .setContentTitle("Notificacion pruebaaaaaaaaaa")
                    .setContentText("Contenido bien chevere")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notification_id,mBuilder.build());
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
    */
}
