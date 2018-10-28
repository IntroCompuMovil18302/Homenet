package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;

public class GuiaCrearAnuncioActivity extends AppCompatActivity {

    Button buttonGuardarRecorrido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_crear_anuncio);

        buttonGuardarRecorrido = (Button) findViewById(R.id.buttonGuardarRecorrido);

        buttonGuardarRecorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GuiaPrincipalActivity.class));
            }
        });

    }
}
