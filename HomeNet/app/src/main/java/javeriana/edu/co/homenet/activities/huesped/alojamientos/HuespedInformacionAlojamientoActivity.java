package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;

public class HuespedInformacionAlojamientoActivity extends AppCompatActivity {
    Button ver_comentarios;
    Button disponibilidad;
    Button ver_ruta;
    Button home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_informacion_alojamiento);
        ver_comentarios =(Button)findViewById(R.id.ver_comentarios);
        disponibilidad=(Button)findViewById(R.id.consultar_dispo);
        ver_ruta=(Button)findViewById(R.id.ver_ruta3);
        home =(Button)findViewById(R.id.home_icon1);
        ver_comentarios.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedInformacionAlojamientoActivity.this,HuespedVerComentariosActivity.class);
                startActivity(intent);
            }
        });
        disponibilidad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedInformacionAlojamientoActivity.this,HuespedConsultarDisponibilidadActivity.class);
                startActivity(intent);
            }
        });
        ver_ruta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedInformacionAlojamientoActivity.this,HuespedVerRutaActivity.class);
                startActivity(intent);
            }
        });
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedInformacionAlojamientoActivity.this,MenuHuespedActivity.class);
                startActivity(intent);
            }
        });
    }
}
