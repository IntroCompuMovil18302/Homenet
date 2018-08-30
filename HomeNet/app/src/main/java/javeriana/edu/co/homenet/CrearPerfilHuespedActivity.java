package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CrearPerfilHuespedActivity extends AppCompatActivity {

    ImageButton volver;
    Button crearPerfilHuesped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_perfil_huesped);

        volver = findViewById(R.id.volver);
        crearPerfilHuesped = findViewById(R.id.crearPerfilHuesped);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RegistroGeneralActivity.class);
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
