package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;
import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.huesped.guias.HuespedSolicitudesAceptadasTourActivity;
import javeriana.edu.co.homenet.activities.huesped.guias.HuespedVerInfoGuiaActivity;

public class GuiasDisponiblesActivity extends AppCompatActivity {

    Button solAceptadas;
    Button verMasGuia;
    ImageButton volverHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_guias_disponibles);

        solAceptadas = findViewById(R.id.solAceptadas);
        verMasGuia = findViewById(R.id.verMasGuia);
        volverHome = findViewById(R.id.volverHome);

        solAceptadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedSolicitudesAceptadasTourActivity.class);
                startActivity(intent);
            }
        });

        verMasGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedVerInfoGuiaActivity.class);
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
