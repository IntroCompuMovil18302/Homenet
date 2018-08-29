package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
