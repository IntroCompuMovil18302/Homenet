package javeriana.edu.co.homenet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.anfitrion.AnfitrionMenuActivity;
import javeriana.edu.co.homenet.activities.guia.GuiaPrincipalActivity;
import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;

public class LoginActivity extends AppCompatActivity {

    Button registrarse;
    Button buttonTestGuias;
    Button buttonTestHuesped;
    Button buttonTestAnfitrion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_login);

        registrarse = findViewById(R.id.register);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonTestGuias = findViewById(R.id.buttonTestGuias);

        buttonTestGuias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),GuiaPrincipalActivity.class));
            }
        });

        buttonTestHuesped = (Button)findViewById(R.id.test_huesped);
        buttonTestHuesped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),MenuHuespedActivity.class));
            }
        });

        buttonTestAnfitrion = findViewById(R.id.test_anfitrion);

        buttonTestAnfitrion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),AnfitrionMenuActivity.class));
            }
        });

    }
}