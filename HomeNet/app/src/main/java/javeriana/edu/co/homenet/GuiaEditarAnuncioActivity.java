package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuiaEditarAnuncioActivity extends AppCompatActivity {

    Button buttonGuardarRecorridoEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_editar_anuncio);

        buttonGuardarRecorridoEditar = (Button) findViewById(R.id.buttonGuardarRecorridoEditar);

        buttonGuardarRecorridoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GuiaDetalleTourActivity.class));
            }
        });
    }
}
