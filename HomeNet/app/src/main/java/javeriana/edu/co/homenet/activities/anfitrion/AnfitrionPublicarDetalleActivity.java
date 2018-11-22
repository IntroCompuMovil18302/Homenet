package javeriana.edu.co.homenet.activities.anfitrion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.models.Alojamiento;

public class AnfitrionPublicarDetalleActivity extends AppCompatActivity{

    private static final String TAG = "YYY" ;
    int modo;
    Alojamiento alojamiento;

    // spinner
    Spinner area;
    Spinner banios;
    Spinner camas;
    Spinner dormitorios;
    Spinner huespedes;
    Spinner parqueaderos;

    // radio group
    CheckBox calefaccion;
    CheckBox internet;
    CheckBox television;
    CheckBox mascotas;

    // radio buttons
    RadioButton radiobutton;
    // buttons
    Button siguiente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_pub_detalle);


        //inicializar variables
        area = findViewById(R.id.spAreaAFD);
        banios = findViewById(R.id.spBaniosAPD);
        camas = findViewById(R.id.spCamasAPD);
        dormitorios = findViewById(R.id.spDormitoriosAPD);
        huespedes = findViewById(R.id.spHuespedesAPD);
        parqueaderos = findViewById(R.id.spParqueaderosAPD);

        calefaccion = findViewById(R.id.cbCalefaccionAPD);
        internet = findViewById(R.id.cbInternetAPD);
        television = findViewById(R.id.cbTelevisionAPD);
        mascotas = findViewById(R.id.cbMascotasAPD);

        siguiente =  findViewById(R.id.btSiguienteAPD);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.area, R.layout.item_anf_spinner);
        area.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.cantidades, R.layout.item_anf_spinner);
        banios.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.cantidades, R.layout.item_anf_spinner);
        camas.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.cantidades, R.layout.item_anf_spinner);
        dormitorios.setAdapter(adapter3);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.cantidades, R.layout.item_anf_spinner);
        huespedes.setAdapter(adapter4);

        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this, R.array.cantidades, R.layout.item_anf_spinner);
        parqueaderos.setAdapter(adapter5);


        accionBotones();
        Bundle bundle = getIntent().getExtras();
        modo = bundle.getInt("modo");
        alojamiento = (Alojamiento) bundle.getSerializable("Data");
        if(modo == 2)
        {
            llenarDatos();
        }
    }

    public void llenarDatos(){
        siguiente.setText("Guardar");
        // TODO get datos forebase
        area.setSelection((alojamiento.getArea()/10)-1);
        banios.setSelection(alojamiento.getBanios()-1);
        camas.setSelection(alojamiento.getCamas()-1);
        dormitorios.setSelection(alojamiento.getDormitorios()-1);
        huespedes.setSelection(alojamiento.getHuespedes()-1);
        parqueaderos.setSelection(alojamiento.getParqueaderos()-1);


        calefaccion.setChecked(alojamiento.isCalefaccion());
        internet.setChecked(alojamiento.isInternet());
        television.setChecked(alojamiento.isTelevision());
        mascotas.setChecked(alojamiento.isMascotas());

    }

    public int buscarValor(SpinnerAdapter adapter,int valor){
        ArrayList<Integer> area = new ArrayList<Integer>();
        ArrayList<Integer> valores = new ArrayList<Integer>();

        for(int i =0; i < adapter.getCount(); i++)
        {
            System.out.println("Valor1111:"+adapter.getItemId(i)+"----"+valor);
            if(adapter.getItemId(i) == valor);
                return i;
        }
        return 1;
    }

    public void checkBox(View v){

        if(calefaccion.isChecked()){
            alojamiento.setCalefaccion(true);
        }
        else{
            alojamiento.setCalefaccion(false);
        }
        if(internet.isChecked()){
            alojamiento.setInternet(true);
        }else{
            alojamiento.setInternet(false);
        }
        if(television.isChecked()){
            alojamiento.setTelevision(true);
        }else{
            alojamiento.setTelevision(false);
        }
        if(mascotas.isChecked()){
            alojamiento.setMascotas(true);
        }else{
            alojamiento.setMascotas(false);
        }


    }

    public void manejoSpinner(){
        alojamiento.setArea(Integer.parseInt(area.getSelectedItem().toString()));
        alojamiento.setBanios(Integer.parseInt(banios.getSelectedItem().toString()));
        alojamiento.setCamas(Integer.parseInt(banios.getSelectedItem().toString()));
        alojamiento.setDormitorios(Integer.parseInt(banios.getSelectedItem().toString()));
        alojamiento.setHuespedes(Integer.parseInt(banios.getSelectedItem().toString()));
        alojamiento.setParqueaderos(Integer.parseInt(banios.getSelectedItem().toString()));
    }

    public void accionBotones() {

        siguiente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(modo == 1) {
                    checkBox(view);
                    manejoSpinner();
                    Intent intent = new Intent(view.getContext(), AnfitrionPublicarListasActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Data", alojamiento);
                    bundle.putInt("modo",1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else
                {

                    checkBox(view);
                    manejoSpinner();

                    // TODO guardar datos
                    Intent intent = new Intent(view.getContext(), AnfMenuEditarActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

}
