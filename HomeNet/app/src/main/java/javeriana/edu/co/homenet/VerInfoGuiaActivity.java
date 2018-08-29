package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VerInfoGuiaActivity extends AppCompatActivity {

    Button verMasTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_guia);

        verMasTour = findViewById(R.id.verMasTour);

        verMasTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),VerMasTourActivity.class);
                startActivity(intent);
            }
        });
    }
}
