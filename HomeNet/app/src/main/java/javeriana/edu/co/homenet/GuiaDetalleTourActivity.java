package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuiaDetalleTourActivity extends AppCompatActivity {

    Button buttonEditDetalle;
    Button buttonHistServiciosGuia;
    Button buttonVolverDetalleGuia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_detalle_tour);

        buttonEditDetalle = (Button) findViewById(R.id.buttonEditDetalle);
        buttonEditDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),GuiaEditarAnuncioActivity.class));
            }
        });
        buttonHistServiciosGuia = (Button) findViewById(R.id.buttonHistServiciosGuia);
        buttonHistServiciosGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),GuiaHistorialServiciosActivity.class));
            }
        });
        buttonVolverDetalleGuia = (Button) findViewById(R.id.buttonVolverDetalleGuia);
        buttonVolverDetalleGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),GuiaPrincipalActivity.class));
            }
        });

    }
}
