package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.anfitrion.AnfitrionPublicarAlojamientoImgActivity;
import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;
import javeriana.edu.co.homenet.adapters.ImagenAnfitrionAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.OpinionAlojamiento;
import javeriana.edu.co.homenet.models.Usuario;

public class HuespedCalificarAlojamientoActivity extends AppCompatActivity {
    Button publicar;
    EditText comentario;
    RatingBar rating ;
    ViewPager viewPager;
    ImagenAnfitrionAdapter imgAnfAdapter;

    List<String> listaImagenes = new ArrayList<String>();
    Alojamiento alojamiento;

    private FirebaseAuth mAuth;
    private ProgressDialog nProgressDialog;
    DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_calificar_alojamiento);

        mAuth = FirebaseAuth.getInstance();
        nProgressDialog = new ProgressDialog(HuespedCalificarAlojamientoActivity.this);
        mDataBase = FirebaseDatabase.getInstance().getReference("Alojamientos/");

        publicar=(Button)findViewById(R.id.btPubComentarioHCA);
        comentario = (EditText)findViewById(R.id.etComentarioHCA);
        rating = (RatingBar)findViewById(R.id.rbRatingHCA);
        viewPager = (ViewPager)findViewById(R.id.vpImagenesAlojHCA);
        findAloj(getIntent().getStringExtra("idAloj"));
        publicar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String comentarioStr = comentario.getText().toString();
                float numEstrellas = rating.getRating();
                System.out.println("El numero de estrellas seleccionadas es -----------------------------------> "+numEstrellas);
                if(!comentarioStr.equals("") && comentarioStr !=null && numEstrellas>0){
                    nProgressDialog.setMessage("Publicando la calificación...");
                    subirCalificacion();
                }
                else{
                    Toast.makeText(HuespedCalificarAlojamientoActivity.this, "Ingrese un comentario y seleccione una calificacion en estrella", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void cargarImagenes(){
        listaImagenes = alojamiento.getUrlImgs();
        imgAnfAdapter = new ImagenAnfitrionAdapter(HuespedCalificarAlojamientoActivity.this,this.listaImagenes);
        viewPager.setAdapter(imgAnfAdapter);
    }
    private void findAloj (final String idAloj) {
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Alojamiento ialojamiento = singleSnapshot.getValue(Alojamiento.class);
                    if (ialojamiento.getId().equals(idAloj)) {
                        alojamiento = ialojamiento;
                    }
                }
                cargarImagenes();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }
    public void subirCalificacion(){
        FirebaseUser user = mAuth.getCurrentUser();
        Random random = new Random();
        int numRandom = random.nextInt(1000)+1;
        final String idOpinionAlojamiento = "OpinionAlojamiento "+String.valueOf(numRandom)+String.valueOf(System.currentTimeMillis());
        String uid = user.getUid();
        OpinionAlojamiento opinion;
        opinion = new OpinionAlojamiento(idOpinionAlojamiento,getIntent().getStringExtra("idAloj"),rating.getRating(),comentario.getText().toString(),uid);
        FirebaseDatabase.getInstance().getReference("OpinionesAlojamiento").child(idOpinionAlojamiento).setValue(opinion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //progressbar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    actualizarUsuario(idOpinionAlojamiento);
                }
                else{
                    nProgressDialog.dismiss();
                    Toast.makeText( HuespedCalificarAlojamientoActivity.this,"Hubo un error al crear un servicio", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
    public void actualizarUsuario(final String idOpinion){
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        mDataBase = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    Usuario usr = dataSnapshot.getValue(Usuario.class);
                    usr.agregarOpinionAlojamiento(idOpinion,true);
                    mDataBase.child(mAuth.getCurrentUser().getUid()).setValue(usr);
                    actualizarAlojamiento(idOpinion);

                }
                else{
                    nProgressDialog.dismiss();
                    Toast.makeText(HuespedCalificarAlojamientoActivity.this, "No se encontro un usuario para asociar la opinion", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void actualizarAlojamiento(final String idOpinion){
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        mDataBase = FirebaseDatabase.getInstance().getReference("Alojamientos");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Alojamientos").child(alojamiento.getId());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    Alojamiento aloja = dataSnapshot.getValue(Alojamiento.class);
                    aloja.agregarOpinion(idOpinion,true);
                    mDataBase.child(alojamiento.getId()).setValue(aloja);
                    nProgressDialog.dismiss();
                    Toast.makeText(HuespedCalificarAlojamientoActivity.this, "Se ha publicado la calificación del alojamiento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HuespedCalificarAlojamientoActivity.this,MenuHuespedActivity.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    Toast.makeText(HuespedCalificarAlojamientoActivity.this, "No se encontro un alojamiento para asociar la opinion", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
