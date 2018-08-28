package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CalificarUsuarioActivity extends AppCompatActivity {
    Button publicar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_usuario);
        publicar=(Button)findViewById(R.id.publicar);
        publicar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(CalificarUsuarioActivity.this,HistorialReservaActivity.class);
                startActivity(intent);
            }
        });
    }
}
