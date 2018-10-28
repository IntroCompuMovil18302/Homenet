package javeriana.edu.co.homenet.activities.huesped;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.RegisterActivity;

public class CrearPerfilHuespedActivity extends AppCompatActivity {

    ImageButton volver;
    Button crearPerfilHuesped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_crear_perfil);

        volver = findViewById(R.id.volver);
        crearPerfilHuesped = findViewById(R.id.crearPerfilHuesped);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        crearPerfilHuesped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MenuHuespedActivity.class);
                startActivity(intent);
            }
        });
    }
}
