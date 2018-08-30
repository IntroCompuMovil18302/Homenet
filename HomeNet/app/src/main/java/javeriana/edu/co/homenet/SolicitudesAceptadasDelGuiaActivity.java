package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SolicitudesAceptadasDelGuiaActivity extends AppCompatActivity {

    Button verTour;
    ImageButton volverHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_aceptadas_del_guia);

        verTour = findViewById(R.id.verMasTour);
        volverHome = findViewById(R.id.volverHome);

        verTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),VerTourActivity.class);
                startActivity(intent);
            }
        });

        volverHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MenuHuespedActivity.class);
                startActivity(intent);
            }
        });
    }
}
