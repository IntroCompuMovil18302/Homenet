package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.activities.huesped.guias.HuespedVerInfoGuiaActivity;
import javeriana.edu.co.homenet.adapters.ComentarioAdapter;
import javeriana.edu.co.homenet.models.OpinionGuia;
import javeriana.edu.co.homenet.models.Tour;
import javeriana.edu.co.homenet.models.Usuario;

public class GuiaCalificacionesActivity extends AppCompatActivity {

    public static final String PATH_OP_GUIA="OpinionesGuia/";
    public static final String PATH_GUIAS = "usersGuias";

    TextView nombreUsuario;
    ImageView imgPerfil;
    RatingBar calificacion;
    ListView comentarios;

    BottomNavigationView navigation;
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tours:
                    item.setIntent(new Intent(GuiaCalificacionesActivity.this, GuiaPrincipalActivity.class));
                    return true;
                case R.id.navigation_new_tour:
                    item.setIntent(new Intent(GuiaCalificacionesActivity.this, GuiaCrearTourActivity.class));
                    return true;
                case R.id.navigation_history_tours:
                    item.setIntent(new Intent(GuiaCalificacionesActivity.this, GuiaHistorialToures.class));
                    return true;
                case R.id.navigation_config:
                    return true;
            }
            return false;
        }
    };

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    ArrayList<OpinionGuia> listComentarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_calificaciones);


        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_config);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        nombreUsuario = findViewById(R.id.tvUserGC);
        imgPerfil = findViewById(R.id.imgPerfilGC);
        calificacion = findViewById(R.id.rbCalificacionGC);
        comentarios = findViewById(R.id.lvComentariosGC);

        obtenerComentarios();
        obtenerUsuario();
    }

    private void obtenerComentarios () {
        final FirebaseUser user = mAuth.getCurrentUser();
        myRef = database.getReference(PATH_OP_GUIA);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    OpinionGuia oG = singleSnapshot.getValue(OpinionGuia.class);
                    if (oG.getGuia().equals(user.getUid())){
                        listComentarios.add(oG);
                    }
                }
                ComentarioAdapter cA = new ComentarioAdapter(GuiaCalificacionesActivity.this,
                        listComentarios);
                comentarios.setAdapter(cA);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void obtenerUsuario(){
        final FirebaseUser user = mAuth.getCurrentUser();
        myRef = database.getReference(PATH_GUIAS).child(user.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario u = dataSnapshot.getValue(Usuario.class);
                nombreUsuario.setText(u.getNombre());
                try{
                    Picasso.get()
                            .load(u.getUrlImg())
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(imgPerfil);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuLogOut){
            mAuth.signOut();
            Intent intent = new Intent(GuiaCalificacionesActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
