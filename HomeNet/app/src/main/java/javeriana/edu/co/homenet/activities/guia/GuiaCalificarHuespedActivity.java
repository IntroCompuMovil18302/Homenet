package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;

public class GuiaCalificarHuespedActivity extends AppCompatActivity {

    Button buttonSaveCalUserGuia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_calificar_huesped);

        buttonSaveCalUserGuia = (Button) findViewById(R.id.buttonSaveCalUserGuia);
        buttonSaveCalUserGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),GuiaHistorialServiciosActivity.class));
            }
        });
    }
}
