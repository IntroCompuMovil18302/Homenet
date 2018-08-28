package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReservarAlojamientoActivity extends AppCompatActivity {
    Button ver_ruta;
    Button reservar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar_alojamiento);
        ver_ruta=(Button)findViewById(R.id.ver_ruta2);
        reservar=(Button)findViewById(R.id.reservar2);
        ver_ruta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ReservarAlojamientoActivity.this,CalificarUsuario.class);
                startActivity(intent);
            }
        });
        reservar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ReservarAlojamientoActivity.this,HistorialReservaActivity.class);
                startActivity(intent);
            }
        });
    }
}
