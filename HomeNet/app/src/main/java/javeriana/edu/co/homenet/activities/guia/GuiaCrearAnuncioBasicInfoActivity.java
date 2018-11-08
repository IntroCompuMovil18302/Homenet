package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;

public class GuiaCrearAnuncioBasicInfoActivity extends AppCompatActivity {

    Button siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_crear_anuncio_basic_info);

        siguiente = findViewById(R.id.btSiguienteGCABA);

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),GuiaCrearAnuncioParadaActivity.class));
            }
        });
    }
}
