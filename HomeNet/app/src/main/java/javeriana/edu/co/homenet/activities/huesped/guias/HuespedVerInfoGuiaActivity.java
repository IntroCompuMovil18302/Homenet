package javeriana.edu.co.homenet.activities.huesped.guias;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.adapters.TourGuiaAdapter;
import javeriana.edu.co.homenet.models.Tour;

public class HuespedVerInfoGuiaActivity extends AppCompatActivity {

    ImageView fotoGuia;
    TextView nombreGuia;
    TextView correoGuia;
    TextView telGuia;
    ListView listaToures;
    List<Tour> toures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_ver_info_guia);

        fotoGuia = findViewById(R.id.ivFotoGuiaHVIGA);
        nombreGuia = findViewById(R.id.tvNombreGuiaHVIGA);
        correoGuia = findViewById(R.id.tvCorreoGuiaHVIGA);
        telGuia = findViewById(R.id.tvTelGuiaHVIGA);
        listaToures = findViewById(R.id.lvListaTouresHVIGA);
        toures = new ArrayList<>();

        obtenerTouresGuia();

        /*verMasTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedVerMasTourActivity.class);
                startActivity(intent);
            }
        });*/

    }

    private void obtenerTouresGuia () {

    }
}
