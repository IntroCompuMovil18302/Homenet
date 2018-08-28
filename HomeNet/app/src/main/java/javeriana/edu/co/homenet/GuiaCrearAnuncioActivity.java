package javeriana.edu.co.homenet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GuiaCrearAnuncioActivity extends AppCompatActivity {

    ListView listRecorridos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_crear_anuncio);

        listRecorridos = (ListView) findViewById(R.id.listRecorridos);
        //ArrayAdapter<String> recorridosAdapater = new ArrayAdapter<String>(this, android.R.layout.act, dataset);
    }
}
