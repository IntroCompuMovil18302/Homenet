package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

public class RegistroGeneral extends AppCompatActivity {

    Spinner selectUsuario;
    Button siguienteRegistro;
    String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_general);

        selectUsuario = findViewById(R.id.selectUsuario);
        siguienteRegistro = findViewById(R.id.siguienteRegistro);

        selectUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoUsuario = (String) selectUsuario.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        siguienteRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tipoUsuario.equals("Huésped")){
                    Intent intent = new Intent(v.getContext(),CrearPerfilGuiaAnf.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(v.getContext(),CrearPerfilHuesped.class);
                    startActivity(intent);
                }

            }
        });
    }
}