package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ConsultarAlojamientoActivity extends AppCompatActivity {
    ImageButton image1;
    ImageButton image4;
    Button porFecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_alojamiento);
        image1=(ImageButton) findViewById(R.id.imageButton);
        image4=(ImageButton)findViewById(R.id.imageButton4);
        porFecha=(Button)findViewById(R.id.consultar_por_fecha1);
        image1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ConsultarAlojamientoActivity.this,InformacionAlojamientoActivity.class);
                startActivity(intent);
            }
        });
        image4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ConsultarAlojamientoActivity.this,InformacionAlojamientoActivity.class);
                startActivity(intent);
            }
        });
        porFecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ConsultarAlojamientoActivity.this,ConsultarPorFechaActivity.class);
                startActivity(intent);
            }
        });
    }
}
