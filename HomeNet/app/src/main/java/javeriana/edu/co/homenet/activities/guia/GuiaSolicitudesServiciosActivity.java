package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;

public class GuiaSolicitudesServiciosActivity extends AppCompatActivity {

    Button buttonVolverSolServGuia;
    Button buttonImageUsuarioGuia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_solicitudes_servicios);

        buttonVolverSolServGuia = (Button) findViewById(R.id.buttonVolverSolServGuia);
        buttonVolverSolServGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),GuiaPrincipalActivity.class));
            }
        });

        buttonImageUsuarioGuia = (Button) findViewById(R.id.buttonImageUsuarioGuia);
        buttonImageUsuarioGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GuiaVerHuespedActivity.class));
            }
        });
    }
}
