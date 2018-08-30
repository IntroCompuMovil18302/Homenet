package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class CrearPerfilGuiaAnfActivity extends AppCompatActivity {

    ImageButton volver;
    Button crearPerfilGuiaAnf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_perfil_guia_anf);

        volver = findViewById(R.id.volver);
        crearPerfilGuiaAnf = findViewById(R.id.crearPerfilGuiaAnf);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RegistroGeneralActivity.class);
                startActivity(intent);
            }
        });

        crearPerfilGuiaAnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MenuAnfitrionActivity.class);
                startActivity(intent);
            }
        });

    }

}