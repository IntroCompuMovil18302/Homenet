package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;

public class GuiaPrincipalActivity extends AppCompatActivity {

    Button buttonTourSample;
    Button buttonAddAnuncioGuia;
    Button buttonSolServGuia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_principal);

        buttonAddAnuncioGuia = (Button) findViewById(R.id.buttonAddAnuncioGuia);
        buttonAddAnuncioGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GuiaCrearAnuncioParadaActivity.class));
            }
        });

        buttonTourSample = (Button) findViewById(R.id.buttonTourSample);
        buttonTourSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),GuiaDetalleTourActivity.class));
            }
        });

        buttonSolServGuia = (Button) findViewById(R.id.buttonSolServGuia);
        buttonSolServGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),GuiaSolicitudesServiciosActivity.class));
            }
        });
    }
}
