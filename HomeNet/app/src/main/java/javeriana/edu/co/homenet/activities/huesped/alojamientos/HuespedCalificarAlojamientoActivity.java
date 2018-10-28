package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;

public class HuespedCalificarAlojamientoActivity extends AppCompatActivity {
    Button publicar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_calificar_alojamiento);
        publicar=(Button)findViewById(R.id.publicar);
        publicar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HuespedCalificarAlojamientoActivity.this,HuespedHistorialReservaActivity.class);
                startActivity(intent);
            }
        });
    }
}
