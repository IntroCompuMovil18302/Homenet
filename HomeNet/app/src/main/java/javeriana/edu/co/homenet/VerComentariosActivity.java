package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VerComentariosActivity extends AppCompatActivity {
    Button home ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_comentarios);
        home =(Button)findViewById(R.id.home_icon2);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(VerComentariosActivity.this,MenuHuespedActivity.class);
                startActivity(intent);
            }
        });
    }
}
