package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;

public class GuiaHistorialServiciosActivity extends AppCompatActivity {

    Button buttonVolverHistServGuia;
    Button buttonHomeHistServGuia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_historial_servicios);


        buttonVolverHistServGuia = (Button) findViewById(R.id.buttonVolverHistServGuia);
        buttonVolverHistServGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GuiaDetalleTourActivity.class));
            }
        });

        buttonHomeHistServGuia = (Button) findViewById(R.id.buttonHomeHistServGuia);
        buttonHomeHistServGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GuiaPrincipalActivity.class));
            }
        });

    }
}
