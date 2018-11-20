package javeriana.edu.co.homenet.activities.huesped;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.activities.guia.GuiasDisponiblesActivity;
import javeriana.edu.co.homenet.activities.huesped.alojamientos.HuespedConsultarAlojamientoActivity;
import javeriana.edu.co.homenet.activities.huesped.alojamientos.HuespedDetallesHistorialReservaActivity;
import javeriana.edu.co.homenet.activities.huesped.alojamientos.HuespedHistorialReservaActivity;
import javeriana.edu.co.homenet.activities.huesped.guias.HuespedHistorialRecorridosActivity;
import javeriana.edu.co.homenet.services.AlarmReceiverService;

public class MenuHuespedActivity extends AppCompatActivity {

    Button consultarAlojamientos;
    Button verHistorialReservas;
    Button verGuiasCercanos;
    Button verHistorialRecorridos;

    private FirebaseAuth mAuth;

    public static final int CODE_INTENT = 103;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_menu);

        mAuth = FirebaseAuth.getInstance();

        consultarAlojamientos = findViewById(R.id.consultarAlojamientos);
        verHistorialReservas = findViewById(R.id.verHistorialReservas);
        verGuiasCercanos = findViewById(R.id.verGuiasCercanos);
        verHistorialRecorridos = findViewById(R.id.verHistorialRecorridos);


        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.SECOND,3);
        calendar.set(Calendar.HOUR_OF_DAY,7);
        calendar.set(Calendar.MINUTE,25);
        Intent intent = new Intent(MenuHuespedActivity.this,AlarmReceiverService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),CODE_INTENT, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        startService(intent);



        consultarAlojamientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedConsultarAlojamientoActivity.class);
                startActivity(intent);
            }
        });

        verHistorialReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedHistorialReservaActivity.class);
                startActivity(intent);
            }
        });

        verGuiasCercanos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),GuiasDisponiblesActivity.class);
                startActivity(intent);
            }
        });

        verHistorialRecorridos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedHistorialRecorridosActivity.class);
                startActivity(intent);
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
            Intent intent = new Intent(MenuHuespedActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed()
    {
        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }
}
