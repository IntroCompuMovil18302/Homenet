package javeriana.edu.co.homenet.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import javeriana.edu.co.homenet.R;

public class AlarmReceiverService extends IntentService {

    private static final String CHANNEL_ID = "notification_test";
    private static final int notification_id = 100;

    public AlarmReceiverService() {
        super("AlarmReceiverService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        /*try{
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
        }*/
    }
}
