package javeriana.edu.co.homenet.activities.anfitrion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.services.CalificacionAlojamientoService;
import javeriana.edu.co.homenet.services.ReservasService;

public class AnfitrionMenuActivity extends AppCompatActivity {

    ImageButton nuevap;
    Button alojamiento;
    Button alojamiento2;
    Button alojamiento3;
    Button alojamiento4;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_menu);

        mAuth = FirebaseAuth.getInstance();

        nuevap = findViewById(R.id.nuevap);

        nuevap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfitrionPublicarAlojamientoActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = new Intent(AnfitrionMenuActivity.this,ReservasService.class);
        startService(intent);
        Intent intentOpinion = new Intent(AnfitrionMenuActivity.this,CalificacionAlojamientoService.class);
        startService(intentOpinion);

        alojamiento = findViewById(R.id.btnalo1);

        alojamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfitrionDetalleAlojamientoActivity.class);
                startActivity(intent);
            }
        });
        alojamiento2 = findViewById(R.id.btnalo2);

        alojamiento2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfitrionDetalleAlojamientoActivity.class);
                startActivity(intent);
            }
        });
        alojamiento3 = findViewById(R.id.btnalo3);

        alojamiento3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfitrionDetalleAlojamientoActivity.class);
                startActivity(intent);
            }
        });
        alojamiento4 = findViewById(R.id.btnres1);

        alojamiento4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfitrionDetalleAlojamientoActivity.class);
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
            Intent intentStop = new Intent(AnfitrionMenuActivity.this,ReservasService.class);
            stopService(intentStop);
            Intent intentOpinionStop =  new Intent (AnfitrionMenuActivity.this,CalificacionAlojamientoService.class);
            stopService(intentOpinionStop);
            Intent intent = new Intent(AnfitrionMenuActivity.this, LoginActivity.class);
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
