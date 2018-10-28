package javeriana.edu.co.homenet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.huesped.CrearPerfilHuespedActivity;

public class RegisterActivity extends AppCompatActivity {

    Spinner selectUsuario;
    Button siguienteRegistro;
    String tipoUsuario;
    ImageButton cancelarReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_register);

        selectUsuario = findViewById(R.id.selectUsuario);
        siguienteRegistro = findViewById(R.id.siguienteRegistro);
        cancelarReg = findViewById(R.id.cancelarReg);

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
                    Intent intent = new Intent(v.getContext(),CrearPerfilGuiaAnfActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(v.getContext(),CrearPerfilHuespedActivity.class);
                    startActivity(intent);
                }

            }
        });

        cancelarReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}