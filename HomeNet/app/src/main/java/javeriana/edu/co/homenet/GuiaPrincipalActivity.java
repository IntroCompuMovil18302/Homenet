package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuiaPrincipalActivity extends AppCompatActivity {

    Button buttonAddAnuncioGuia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_principal);

        buttonAddAnuncioGuia = (Button) findViewById(R.id.buttonAddAnuncioGuia);
        buttonAddAnuncioGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GuiaCrearAnuncioActivity.class));
            }
        });
    }
}
