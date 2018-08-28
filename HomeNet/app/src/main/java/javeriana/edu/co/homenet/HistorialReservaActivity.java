package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HistorialReservaActivity extends AppCompatActivity {
    Button detalles;
    Button detalles2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_reserva);
        detalles=(Button)findViewById(R.id.detalles);
        detalles2=(Button)findViewById(R.id.detalles2);
        detalles.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HistorialReservaActivity.this,DetallesHistorialReservaActivity.class);
                startActivity(intent);
            }
        });
    }
}
