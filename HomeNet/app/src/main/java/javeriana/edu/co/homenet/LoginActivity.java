package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button registrarse;
    Button buttonTestGuias;
    Button buttonTestHuesped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registrarse = findViewById(R.id.register);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RegistroGeneralActivity.class);
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
                startActivity(new Intent(view.getContext(),ConsultarAlojamientoActivity.class));
            }
        });

    }
}