package javeriana.edu.co.homenet.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
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

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.anfitrion.AnfitrionMenuActivity;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Reserva;
import javeriana.edu.co.homenet.models.Usuario;

public class ReservasService extends IntentService {
    private static final String CHANNEL_ID = "notification_test";
    private static final int notification_id = 100;
    private FirebaseAuth mAuth;
    DatabaseReference mDataBase;
    Usuario usuario;
    Map<String,String> listaAlojamientos = new HashMap<>();

    public ReservasService() {
        super("ReservasService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        usuarioActual();
    }

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
                        Toast.makeText(ReservasService.this, "No se encontro un usuario para asociar el alojamiento", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void listenerReserva() {
        mDataBase = FirebaseDatabase.getInstance().getReference("Reservas/");
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (mAuth.getCurrentUser() != null) {
                        Reserva reserva = dataSnapshot.getValue(Reserva.class);
                        Random random = new Random();
                        int numRandom = random.nextInt(10000)+1;
                        String CHANNEL_ID="";
                        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent repeating_intent = new Intent(ReservasService.this,AnfitrionMenuActivity.class);
                        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if (usuario.getAlojamientos().get(reserva.getAlojamiento()) != null) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                numRandom = random.nextInt(10000)+1;
                                CHANNEL_ID = "Reserva "+numRandom;
                                System.out.println("Esta en una version mayor a Android 8");
                                System.out.println("Entro ahora si al sistema OREO");
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

                            }
                        }
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
}
