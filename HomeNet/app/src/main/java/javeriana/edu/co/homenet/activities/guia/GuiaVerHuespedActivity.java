package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;

public class GuiaVerHuespedActivity extends AppCompatActivity {

    Button buttonVolverVerUserGuia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_ver_huesped);

        buttonVolverVerUserGuia = (Button) findViewById(R.id.buttonVolverVerUserGuia);

        buttonVolverVerUserGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GuiaSolicitudesServiciosActivity.class));
            }
        });
    }
}
