package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConsultarPorFechaActivity extends AppCompatActivity {
    Button seleccionar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_por_fecha);
        seleccionar=(Button)findViewById(R.id.button_seleccionar1);
        seleccionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ConsultarPorFechaActivity.this,ConsultarAlojamientoActivity.class);
                startActivity(intent);
            }
        });
    }
}
