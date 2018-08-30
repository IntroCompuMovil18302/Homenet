package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuAnfitrionActivity extends AppCompatActivity {

    ImageButton nuevap;
    Button alojamiento;
    Button alojamiento2;
    Button alojamiento3;
    Button alojamiento4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_anfitrion);

        nuevap = findViewById(R.id.nuevap);

        nuevap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),PublicarAlojamientoAnfActivity.class);
                startActivity(intent);
            }
        });

        alojamiento = findViewById(R.id.btnalo1);

        alojamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetalleAlojamientoAnfActivity.class);
                startActivity(intent);
            }
        });
        alojamiento2 = findViewById(R.id.btnalo2);

        alojamiento2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetalleAlojamientoAnfActivity.class);
                startActivity(intent);
            }
        });
        alojamiento3 = findViewById(R.id.btnalo3);

        alojamiento3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetalleAlojamientoAnfActivity.class);
                startActivity(intent);
            }
        });
        alojamiento4 = findViewById(R.id.btnres1);

        alojamiento4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetalleAlojamientoAnfActivity.class);
                startActivity(intent);
            }
        });


    }
}
