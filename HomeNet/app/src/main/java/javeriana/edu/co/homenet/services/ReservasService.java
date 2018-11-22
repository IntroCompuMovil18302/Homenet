package javeriana.edu.co.homenet.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.Nullable;
import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.anfitrion.AnfitrionMenuActivity;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Reserva;
import javeriana.edu.co.homenet.models.Usuario;

public class ReservasService extends Service {
    private static final String CHANNEL_ID = "notification_test";
    private static final int notification_id = 100;
    private FirebaseAuth mAuth;
    DatabaseReference mDataBase;
    Usuario usuario;
    Map<String,String> listaAlojamientos = new HashMap<>();
    public static boolean isServiceRunning = false;
    boolean primero =false;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        usuarioActual();
    }

    @Override
    public void onDestroy() {
        isServiceRunning = false;
        super.onDestroy();
    }

    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    @Override
    protected void onHandleIntent(Intent intent) {
        mAuth = FirebaseAuth.getInstance();
        usuarioActual();
    }*/

    public void usuarioActual() {
        if (mAuth.getCurrentUser() != null) {
            mDataBase = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
            mDataBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        usuario = dataSnapshot.getValue(Usuario.class);
                        encontrarAlojamientos();
                        //listenerReserva();
                    } else {
                        Toast.makeText(ReservasService.this, "No se encontro un usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void listenerReserva() {
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Reservas/");
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (mAuth.getCurrentUser() != null) {
                        Reserva reserva= new Reserva();
                        int i=0;
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            reserva = singleSnapshot.getValue(Reserva.class);
                            i++;
                            System.out.println("El id de la reserva "+reserva.getId());
                        }
                        System.out.println("Reserva fecha de Inicio------> "+reserva.getFechaFin());
                        System.out.println("Reserva fecha de Fin------> "+reserva.getFechaInicio());
                        Random random = new Random();
                        int numRandom = random.nextInt(10000)+1;
                        String CHANNEL_ID="";
                        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent repeating_intent = new Intent(ReservasService.this,AnfitrionMenuActivity.class);
                        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        System.out.println("Se creooooooooooooooooooooooooooooooooooooo una reservaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa -------------------------->");
                        if (usuario.getAlojamientos().get(reserva.getAlojamiento()) != null && primero) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                System.out.println("Entro android version mayor  a 8------------------------->");
                                numRandom = random.nextInt(10000)+1;
                                CHANNEL_ID = "Reserva "+numRandom;
                                CharSequence name = getString(R.string.desde);
                                String description = getString(R.string.agregar);
                                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
                                channel.setDescription(description);
                                notificationManager.createNotificationChannel(channel);
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ReservasService.this,CHANNEL_ID);
                                mBuilder.setSmallIcon(R.drawable.boy);
                                mBuilder.setContentTitle("Nueva reserva en un alojamiento");
                                mBuilder.setContentText("Realizaron una nueva reserva en tu alojamiento "+listaAlojamientos.get(reserva.getAlojamiento()));
                                mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(""));
                                mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                                numRandom = random.nextInt(10000)+1;
                                notificationManager.notify(numRandom,mBuilder.build());
                            }else{ //Android con version menor a la 8
                                System.out.println("Entro android version menor a 8");
                                numRandom = random.nextInt(10000)+1;
                                PendingIntent pendingIntent = PendingIntent.getActivity(ReservasService.this,numRandom,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(ReservasService.this)
                                        .setContentIntent(pendingIntent)
                                        .setSmallIcon(R.drawable.boy)
                                        .setContentTitle("Nueva reserva en un alojamiento")
                                        .setContentText("Realizaron una nueva reserva en tu alojamiento "+listaAlojamientos.get(reserva.getAlojamiento()))
                                        .setAutoCancel(true)
                                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                        .setStyle(new NotificationCompat.BigTextStyle().
                                                bigText("Realizaron una nueva reserva en tu alojamiento "+listaAlojamientos.get(reserva.getAlojamiento())))
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                numRandom = random.nextInt(10000)+1;
                                notificationManager.notify(numRandom,builder.build());
                            }
                        }
                        primero=true;
                    }
                } else {
                    Toast.makeText(ReservasService.this, "No se encontro un usuario para asociar el alojamiento", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void encontrarAlojamientos(){
        mDataBase = FirebaseDatabase.getInstance().getReference("Alojamientos/");
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Alojamiento ialojamiento = singleSnapshot.getValue(Alojamiento.class);
                    listaAlojamientos.put(ialojamiento.getId(),ialojamiento.getNombre());
                    System.out.println("El nombre del alojamiento "+ialojamiento.getNombre());
                }
                listenerReserva();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }

    public void stopMyService(){
        stopForeground(true);
        stopSelf();
        isServiceRunning=false;
    }
}
