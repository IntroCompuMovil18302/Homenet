package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HistorialReservasAnfActivity extends AppCompatActivity {

    Button res1;
    Button res2;
    Button res3;
    Button res4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_reservas_anf);

        res1 = findViewById(R.id.btnres1);

        res1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetalleReservaAnfActivity.class);
                startActivity(intent);
            }
        });

        res2 = findViewById(R.id.btnres2);

        res2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetalleReservaAnfActivity.class);
                startActivity(intent);
            }
        });

        res3 = findViewById(R.id.btnres3);

        res3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetalleReservaAnfActivity.class);
                startActivity(intent);
            }
        });

        res4 = findViewById(R.id.btnres4);

        res4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetalleReservaAnfActivity.class);
                startActivity(intent);
            }
        });
    }
}
