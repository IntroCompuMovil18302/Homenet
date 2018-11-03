package javeriana.edu.co.homenet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.huesped.CrearPerfilHuespedActivity;
import javeriana.edu.co.homenet.models.Usuario;

public class RegisterActivity extends AppCompatActivity {

    Spinner selectUsuario;
    Button siguienteRegistro;
    String tipoUsuario;
    ImageButton cancelarReg;
    EditText nombreUsuario;
    EditText correo;
    EditText clave;
    EditText confirmarClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_register);

        selectUsuario = findViewById(R.id.sTipoUsuarioRA);
        siguienteRegistro = findViewById(R.id.btSiguienteRegistroRA);
        cancelarReg = findViewById(R.id.btCancelarRegRA);
        nombreUsuario = findViewById(R.id.etUserNameRA);
        correo  = findViewById(R.id.etCorreoRA);
        clave = findViewById(R.id.etContraRA);
        confirmarClave = findViewById(R.id.etConfirmarContraRA);


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
                if (isETEmpty(nombreUsuario) && isETEmpty(correo) && isETEmpty(clave)
                        && isETEmpty(confirmarClave)){
                    if (isEmailValid(correo.getText().toString())) {
                        if (clave.getText().toString().equals(confirmarClave.getText().toString())) {

                            Bundle b = new Bundle();
                            b.putString("nombreUsuario", nombreUsuario.getText().toString());
                            b.putString("correo", correo.getText().toString());
                            b.putString("clave", clave.getText().toString());
                            b.putString("tipoUsuario",tipoUsuario);
                            if (!tipoUsuario.equals("Huésped")) {
                                Intent intent = new Intent(v.getContext(), CrearPerfilGuiaAnfActivity.class);
                                intent.putExtras(b);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(v.getContext(), CrearPerfilHuespedActivity.class);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(v.getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(v.getContext(), "Correo incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(v.getContext(), "Complete los campos", Toast.LENGTH_SHORT).show();
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

    public boolean isETEmpty(EditText et) {
        if (et.getText().toString().equals("") || et.getText().toString().equals(" ")){
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        boolean isValid = true;
        if (!email.contains("@") || !email.contains(".") || email.length() < 5){
            isValid = false;
        }
        return isValid;
    }
}