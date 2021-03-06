package javeriana.edu.co.homenet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import javeriana.edu.co.homenet.R;

public class RegisterActivity extends AppCompatActivity {

    Spinner selectUsuario;
    Button siguienteRegistro;
    String tipoUsuario;
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
                            Intent intent = new Intent(v.getContext(), CrearPerfilActivity.class);
                            intent.putExtras(b);
                            startActivity(intent);
                        } else {
                            confirmarClave.setText("");
                            confirmarClave.setError("Contraseñas no coinciden");
                        }
                    }else{
                        correo.setError("Correo inválido");
                    }
                }else{
                    correo.setError("Complete el campo");
                    nombreUsuario.setError("Complete el campo");
                    clave.setError("Complete el campo");
                    confirmarClave.setError("Complete el campo");
                }
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