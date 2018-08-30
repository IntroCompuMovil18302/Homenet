package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InformacionAlojamientoActivity extends AppCompatActivity {
    Button ver_comentarios;
    Button disponibilidad;
    Button ver_ruta;
    Button home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_alojamiento);
        ver_comentarios =(Button)findViewById(R.id.ver_comentarios);
        disponibilidad=(Button)findViewById(R.id.consultar_dispo);
        ver_ruta=(Button)findViewById(R.id.ver_ruta3);
        home =(Button)findViewById(R.id.home_icon1);
        ver_comentarios.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(InformacionAlojamientoActivity.this,VerComentariosActivity.class);
                startActivity(intent);
            }
        });
        disponibilidad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(InformacionAlojamientoActivity.this,ConsultarDisponibilidadActivity.class);
                startActivity(intent);
            }
        });
        ver_ruta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(InformacionAlojamientoActivity.this,VerRutaActivity.class);
                startActivity(intent);
            }
        });
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(InformacionAlojamientoActivity.this,MenuHuespedActivity.class);
                startActivity(intent);
            }
        });
    }
}
