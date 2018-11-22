package javeriana.edu.co.homenet.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.anfitrion.AnfitrionMenuActivity;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.OpinionAlojamiento;
import javeriana.edu.co.homenet.models.Usuario;

public class CalificacionAlojamientoService extends Service {
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
                        Toast.makeText(CalificacionAlojamientoService.this, "No se encontro un usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void listenerOpinionesAloj() {
        mDataBase = FirebaseDatabase.getInstance().getReference().child("OpinionesAlojamiento/");
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (mAuth.getCurrentUser() != null) {
                        OpinionAlojamiento opAloj= new OpinionAlojamiento();
                        int i=0;
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            opAloj = singleSnapshot.getValue(OpinionAlojamiento.class);
                            i++;
                            System.out.println("El id de la opinion del alojamiento "+opAloj.getId());
                        }
                        Random random = new Random();
                        int numRandom = random.nextInt(10000)+1;
                        String CHANNEL_ID="";
                        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent repeating_intent = new Intent(CalificacionAlojamientoService.this,AnfitrionMenuActivity.class);
                        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        System.out.println("Se creooooooooooooooooooooooooooooooooooooo una opinion del alojamiento -------------------------->");
                        if (usuario.getAlojamientos().get(opAloj.getAlojamiento()) != null && primero) {
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
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(CalificacionAlojamientoService.this,CHANNEL_ID);
                                mBuilder.setSmallIcon(R.drawable.ic_stars);
                                mBuilder.setContentTitle("Nueva calificacion en un alojamiento");
                                mBuilder.setContentText("Realizaron una nueva calificacion en tu alojamiento "+listaAlojamientos.get(opAloj.getAlojamiento()));
                                mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(""));
                                mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                                numRandom = random.nextInt(10000)+1;
                                notificationManager.notify(numRandom,mBuilder.build());
                            }else{ //Android con version menor a la 8
                                System.out.println("Entro android version menor a 8");
                                numRandom = random.nextInt(10000)+1;
                                PendingIntent pendingIntent = PendingIntent.getActivity(CalificacionAlojamientoService.this,numRandom,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(CalificacionAlojamientoService.this)
                                        .setContentIntent(pendingIntent)
                                        .setSmallIcon(R.drawable.ic_stars)
                                        .setContentTitle("Nueva calificacion en un alojamiento")
                                        .setContentText("Realizaron una nueva calificacion en tu alojamiento "+listaAlojamientos.get(opAloj.getAlojamiento()))
                                        .setAutoCancel(true)
                                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                        .setStyle(new NotificationCompat.BigTextStyle().
                                                bigText("Realizaron una nueva calificacion en tu alojamiento "+listaAlojamientos.get(opAloj.getAlojamiento())))
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                numRandom = random.nextInt(10000)+1;
                                notificationManager.notify(numRandom,builder.build());
                            }
                        }
                        primero=true;
                    }
                } else {
                    Toast.makeText(CalificacionAlojamientoService.this, "No se encontro un usuario para asociar el alojamiento", Toast.LENGTH_SHORT).show();
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
                listenerOpinionesAloj();

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
