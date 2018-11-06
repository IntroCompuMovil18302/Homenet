package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;

public class GuiaEditarAnuncioParadaActivity extends AppCompatActivity {

    Button buttonGuardarRecorrido;
    Button detalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_editar_anuncio_parada);

        buttonGuardarRecorrido = (Button) findViewById(R.id.buttonGuardarRecorrido);
        detalle = findViewById(R.id.btDetalleParadaGCAPA);

        buttonGuardarRecorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GuiaDetalleTourActivity.class));
            }
        });

        detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), GuiaEditarAnuncioDetalleActivity.class));
            }
        });
    }
}
