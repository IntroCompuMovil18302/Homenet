package javeriana.edu.co.homenet.activities.huesped.guias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import javeriana.edu.co.homenet.R;

public class HuespedVerMasTourActivity extends AppCompatActivity {

    Button verRecorridos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_ver_mas_tour);

        verRecorridos = findViewById(R.id.btVerRecorridosHVTA);

        verRecorridos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedVerRecorridoTourActivity.class);
                startActivity(intent);
            }
        });
    }
}
