package javeriana.edu.co.homenet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class GuiaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_principal);

        getActionBar().setTitle("Menú Principal");
        getSupportActionBar().setTitle("Menú Principal");

    }
}
