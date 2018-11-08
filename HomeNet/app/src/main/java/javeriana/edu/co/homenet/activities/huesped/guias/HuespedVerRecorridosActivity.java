package javeriana.edu.co.homenet.activities.huesped.guias;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.guia.GuiaCrearAnuncioDetalleActivity;
import javeriana.edu.co.homenet.activities.guia.GuiaPrincipalActivity;

public class HuespedVerRecorridosActivity extends AppCompatActivity {

    Button detalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_ver_recorridos);


        detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), HuespedVerRecorridoDetalleActivity.class));
            }
        });
    }
}
