package javeriana.edu.co.homenet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import javeriana.edu.co.homenet.activities.anfitrion.AnfitrionMenuActivity;
import javeriana.edu.co.homenet.R;

public class CrearPerfilGuiaAnfActivity extends AppCompatActivity {

    ImageButton volver;
    Button crearPerfilGuiaAnf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_crear_perfil_guia_anf);

        volver = findViewById(R.id.volver);
        crearPerfilGuiaAnf = findViewById(R.id.crearPerfilGuiaAnf);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        crearPerfilGuiaAnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfitrionMenuActivity.class);
                startActivity(intent);
            }
        });

    }

}