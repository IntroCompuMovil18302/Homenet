package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConsultarDisponibilidadActivity extends AppCompatActivity {
    Button listo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_disponibilidad);
        listo=(Button)findViewById(R.id.button_listo);
        listo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ConsultarDisponibilidadActivity.this,ReservarAlojamientoActivity.class);
                startActivity(intent);
            }
        });
    }
}
