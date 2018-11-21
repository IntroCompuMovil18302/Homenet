package javeriana.edu.co.homenet.activities.huesped.guias;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.adapters.ComentarioAdapter;
import javeriana.edu.co.homenet.models.OpinionGuia;
import javeriana.edu.co.homenet.models.Usuario;

public class HuespedVerInfoGuiaActivity extends AppCompatActivity {

    public static final String PATH_OP_GUIA="OpinionesGuia/";
    public static final String PATH_GUIAS="usersGuias/";

    ImageView fotoGuia;
    TextView nombreGuia;
    TextView correoGuia;
    TextView telGuia;
    RatingBar calificacion;
    ListView comentariosGuia;
    ArrayList<OpinionGuia> comentarios = new ArrayList<>();
    Usuario guia;
    Bundle b;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_ver_info_guia);

        fotoGuia = findViewById(R.id.ivFotoGuiaHVIGA);
        nombreGuia = findViewById(R.id.tvNombreGuiaHVIGA);
        correoGuia = findViewById(R.id.tvCorreoGuiaHVIGA);
        telGuia = findViewById(R.id.tvTelGuiaHVIGA);
        calificacion = findViewById(R.id.rbCalGuiaHVIGA);
        comentariosGuia = findViewById(R.id.lvComentariosGuiaHVIGA);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        b = getIntent().getExtras();

        obtenerGuia(b.getString("idGuia"));
        obtenerComentarios(b.getString("idGuia"));

    }

    private void obtenerGuia (final String idGuia) {
        myRef = database.getReference(PATH_GUIAS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.getKey().equals(idGuia)){
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

    private void obtenerComentarios (final String idGuia) {
        myRef = database.getReference(PATH_OP_GUIA);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    OpinionGuia oG = singleSnapshot.getValue(OpinionGuia.class);
                    if (oG.getGuia().equals(b.getString("idGuia"))){
                        comentarios.add(oG);
                    }
                }
                ComentarioAdapter cA = new ComentarioAdapter(HuespedVerInfoGuiaActivity.this,
                        comentarios);
                comentariosGuia.setAdapter(cA);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setValues (){
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
        correoGuia.setText(guia.getCorreo());
        telGuia.setText(Integer.toString(guia.getTelefono()));
        calificacion.setRating((float) guia.getCalificacion());
    }
}
