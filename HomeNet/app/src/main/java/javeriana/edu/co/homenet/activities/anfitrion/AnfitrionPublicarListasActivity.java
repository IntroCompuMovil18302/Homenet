package javeriana.edu.co.homenet.activities.anfitrion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.adapters.AnfPubElementosAlojAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;

public class AnfitrionPublicarListasActivity extends AppCompatActivity {

    Alojamiento alojamiento;
    ArrayList<String> muebles;
    ArrayList<String> electrodomesticos;

    RecyclerView rvmuebles;
    RecyclerView rvelectrodomesticos;

    AnfPubElementosAlojAdapter adapterMuebles;
    AnfPubElementosAlojAdapter adapterElectrodomesticos;

    RecyclerView.LayoutManager managerMuebles;
    RecyclerView.LayoutManager managerElectrodomesticos;

    Button siguiente;
    Button addElectrodomestico;
    Button addMueble;

    EditText electrodomestico;
    EditText mueble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_pub_listas);
        alojamiento = (Alojamiento) getIntent().getSerializableExtra("Data");

        muebles = new ArrayList<String>();
        electrodomesticos = new ArrayList<String>();
        datosPrueba();

        // inicializar
        siguiente = findViewById(R.id.btSiguienteAPL);
        addElectrodomestico = findViewById(R.id.btElectrodomesticosAPL);
        addMueble = findViewById(R.id.btMueblesAPL);

        electrodomestico = findViewById(R.id.tvElectrodomesticosAPL);
        mueble = findViewById(R.id.tvMueblesAPL);

        rvmuebles = findViewById(R.id.rvMueblesAPL);
        rvmuebles.setHasFixedSize(true);
        rvelectrodomesticos = findViewById(R.id.rvElectrodomesticosAPL);
        rvelectrodomesticos.setHasFixedSize(true);


        crearRecycler();
        accionBotones();
    }


    public void accionBotones() {

        siguiente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("XXXXXXXXXXXXXXXXXXXX");
                alojamiento.setElectrodomesticos(electrodomesticos);
                alojamiento.setMuebles(muebles);
                Intent intent = new Intent(view.getContext(), AnfitrionPublicarDisponibilidadActivity.class);
                intent.putExtra("Data", alojamiento);
                startActivity(intent);
            }
        });

        addElectrodomestico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertItem(2,electrodomestico.getText().toString());
                electrodomestico.setText("");
            }
        });

        addMueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertItem(1,mueble.getText().toString());
                mueble.setText("");
            }
        });

    }

    public void insertItem(int n, String text){
        if(n == 1)
        {
            muebles.add(text);
            adapterMuebles.notifyItemInserted(adapterMuebles.getItemCount());
        }
        else {
            electrodomesticos.add(text);
            adapterElectrodomesticos.notifyItemInserted(adapterElectrodomesticos.getItemCount());
        }
    }

    public void deleteItem(int n, int position){
        // 1 = mueble / 2 = electrodomestico
        if(n == 1)
        {
            muebles.remove(position);
            adapterMuebles.notifyItemRemoved(position);
        }
        else {
            electrodomesticos.remove(position);
            adapterElectrodomesticos.notifyItemRemoved(position);
        }
    }

    public void changeItem(int n,int position){
        // 1 = mueble / 2 = electrodomestico
        if(n == 1) {
            String s = muebles.get(position);
            adapterMuebles.notifyItemChanged(position);
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Seleccione una ubicacion" + s, Toast.LENGTH_SHORT);

            toast1.show();
        }
        else {
            String s = electrodomesticos.get(position);
            adapterElectrodomesticos.notifyItemChanged(position);
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Seleccione una ubicacion" + s, Toast.LENGTH_SHORT);

            toast1.show();
        }


    }

    public void crearRecycler(){
        managerMuebles = new LinearLayoutManager(this);
        managerElectrodomesticos = new LinearLayoutManager(this);

        adapterMuebles = new AnfPubElementosAlojAdapter(muebles);
        adapterElectrodomesticos = new AnfPubElementosAlojAdapter(electrodomesticos);

        rvmuebles.setLayoutManager(managerMuebles);
        rvelectrodomesticos.setLayoutManager(managerElectrodomesticos);

        rvmuebles.setAdapter(adapterMuebles);
        rvelectrodomesticos.setAdapter(adapterElectrodomesticos);

        adapterMuebles.setOnItemClickListener(new AnfPubElementosAlojAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
              changeItem(1,position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteItem(1,position);
            }
        });

        adapterElectrodomesticos.setOnItemClickListener(new AnfPubElementosAlojAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(2,position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteItem(2, position);
            }
        });
    }

    public void datosPrueba()
    {
        for(int i =0; i < 10 ; i++)
        {
            muebles.add("mueble"+i+1);
            electrodomesticos.add("electrodomestico"+i+1);
        }
    }


}
