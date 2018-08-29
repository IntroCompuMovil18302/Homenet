package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuiasDisponiblesActivity extends AppCompatActivity {

    Button solAceptadas;
    Button verMasGuia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guias_disponibles);

        solAceptadas = findViewById(R.id.solAceptadas);
        verMasGuia = findViewById(R.id.verMasGuia);

        solAceptadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),SolicitudesAceptadasDelGuiaActivity.class);
                startActivity(intent);
            }
        });

        verMasGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),VerInfoGuiaActivity.class);
                startActivity(intent);
            }
        });
    }
}
