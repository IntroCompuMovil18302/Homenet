package javeriana.edu.co.homenet.activities.anfitrion;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Reserva;

public class AnfMenuEditarActivity extends AppCompatActivity {

    Alojamiento alojamiento;

    Button datosEspecificos;
    Button datosGenerales;
    Button mueblesElectrodomesticos;
    Button disponibilidad;
    Button imagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_menu_editar);

        alojamiento = (Alojamiento) getIntent().getSerializableExtra("Data");

        datosEspecificos = findViewById(R.id.btDatosEspecificosAME);
        datosGenerales = findViewById(R.id.btDatosgeneralesAME);
        mueblesElectrodomesticos = findViewById(R.id.btMueblesAME);
        disponibilidad = findViewById(R.id.btDisponibilidadAME);
        imagenes =  findViewById(R.id.btImagenesAME);

        accionBotones();
    }

    private  void accionBotones(){

        datosGenerales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AnfitrionPublicarAlojamientoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Data", alojamiento);
                bundle.putInt("modo",2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        datosEspecificos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AnfitrionPublicarDetalleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Data", alojamiento);
                bundle.putInt("modo",2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        mueblesElectrodomesticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AnfitrionPublicarListasActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Data", alojamiento);
                bundle.putInt("modo",2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        disponibilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AnfitrionPublicarDisponibilidadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Data", alojamiento);
                bundle.putInt("modo",2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        imagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AnfitrionPublicarAlojamientoImgActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Data", alojamiento);
                bundle.putInt("modo",2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
