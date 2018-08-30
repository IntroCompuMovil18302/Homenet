package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class VerInfoGuiaActivity extends AppCompatActivity {

    Button verMasTour;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_guia);

        verMasTour = findViewById(R.id.verMasTour);
        back = findViewById(R.id.back);

        verMasTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),VerMasTourActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),GuiasDisponiblesActivity.class);
                startActivity(intent);
            }
        });
    }
}
