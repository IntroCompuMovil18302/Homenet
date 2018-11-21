package javeriana.edu.co.homenet.activities.huesped.guias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;

public class HuespedVerInfoGuiaActivity extends AppCompatActivity {

    Button verMasTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_ver_info_guia);

        verMasTour = findViewById(R.id.verMasTour);

        verMasTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedVerMasTourActivity.class);
                startActivity(intent);
            }
        });

    }
}
