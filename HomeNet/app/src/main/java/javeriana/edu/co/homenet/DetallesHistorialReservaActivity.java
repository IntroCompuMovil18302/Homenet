package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DetallesHistorialReservaActivity extends AppCompatActivity {
    Button informacion;
    Button calificacion;
    Button ver_ruta;
    Button home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_historial_reserva);
        informacion =(Button)findViewById(R.id.info_alojamiento);
        calificacion=(Button)findViewById(R.id.calificacion);
        ver_ruta=(Button)findViewById(R.id.ver_ruta);
        home = (Button)findViewById(R.id.home_icon3);
        informacion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(DetallesHistorialReservaActivity.this,InformacionAlojamientoActivity.class);
                startActivity(intent);
            }
        });
        calificacion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(DetallesHistorialReservaActivity.this,CalificarAlojamientoActivity.class);
                startActivity(intent);
            }
        });
        ver_ruta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(DetallesHistorialReservaActivity.this,VerComentariosActivity.class);
                startActivity(intent);
            }
        });
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(DetallesHistorialReservaActivity.this,MenuHuespedActivity.class);
                startActivity(intent);
            }
        });
    }
}
