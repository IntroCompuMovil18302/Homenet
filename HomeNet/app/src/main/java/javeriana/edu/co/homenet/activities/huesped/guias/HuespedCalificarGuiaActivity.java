package javeriana.edu.co.homenet.activities.huesped.guias;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.OpinionGuia;
import javeriana.edu.co.homenet.models.Usuario;

public class HuespedCalificarGuiaActivity extends AppCompatActivity {

    public static final String PATH_GUIA="usersGuias/";
    public static final String PATH_OPINIONES="OpinionesGuia/";

    Button enviarCalGuia;
    ImageView fotoGuia;
    TextView nombreGuia;
    RatingBar calGuia;
    EditText comentario;

    Bundle b;
    private String idGuia;
    private Usuario guia;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_calificar_guia);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(PATH_GUIA);
        myRef2 = database.getReference(PATH_OPINIONES);

        enviarCalGuia = findViewById(R.id.btEnviarComenHCGA);
        fotoGuia = findViewById(R.id.ivFotoGuiaHCGA);
        nombreGuia = findViewById(R.id.tvNombreGuiaHCGA);
        calGuia = findViewById(R.id.rbCalGuiaHCGA);
        comentario = findViewById(R.id.etComentarioHCGA);

        b = getIntent().getExtras();
        idGuia = b.getString("idGuia");

        buscarGuia(idGuia);

        Log.i("COCU",comentario.getText().toString());

        enviarCalGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isETEmpty(comentario)){
                    guardarComentario();
                    Intent intent = new Intent(v.getContext(),HuespedHistorialRecorridosActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    comentario.setError("Debe llenar el campo");
                }
            }
        });
    }

    private void buscarGuia (final String id) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.getKey().equals(id)){
                        guia = singleSnapshot.getValue(Usuario.class);
                    }
                }
                setValues();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setValues (){
        try{
            Picasso.get()
                    .load(guia.getUrlImg())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(fotoGuia);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        nombreGuia.setText(guia.getNombre());
    }

    public void guardarComentario (){
        OpinionGuia oG = new OpinionGuia();
        oG.setCalificacion((int) calGuia.getRating());
        oG.setGuia(idGuia);
        oG.setUsuario(mAuth.getUid());
        oG.setDescripcion(comentario.getText().toString());
        String idOp = myRef2.push().getKey();
        myRef2 = database.getReference(PATH_OPINIONES+idOp);
        myRef2.setValue(oG);
        myRef = database.getReference(PATH_GUIA+idGuia+"/opinionesGuia/");
        Map<String,Object> updateOp = new HashMap<>();
        updateOp.put(idOp,true);
        myRef.updateChildren(updateOp);
    }

    public boolean isETEmpty(EditText et) {
        if (et.getText().toString().equals("") || et.getText().toString().equals(" ")){
            return false;
        }
        return true;
    }
}