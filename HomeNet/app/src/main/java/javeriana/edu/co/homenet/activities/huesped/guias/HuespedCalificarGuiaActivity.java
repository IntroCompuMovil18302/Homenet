package javeriana.edu.co.homenet.activities.huesped.guias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import javeriana.edu.co.homenet.R;

public class HuespedCalificarGuiaActivity extends AppCompatActivity {

    Button enviarCalGuia;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_calificar_guia);

        enviarCalGuia = findViewById(R.id.enviarCalGuia);
        back = findViewById(R.id.back);

        enviarCalGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedHistorialRecorridosActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedHistorialRecorridosActivity.class);
                startActivity(intent);
            }
        });
    }
}
