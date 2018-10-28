package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;

public class HuespedConsultarPorFechaActivity extends AppCompatActivity {
    Button seleccionar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_consultar_por_fecha);
        seleccionar=(Button)findViewById(R.id.button_seleccionar1);
        seleccionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedConsultarPorFechaActivity.this,HuespedConsultarAlojamientoActivity.class);
                startActivity(intent);
            }
        });
    }
}
