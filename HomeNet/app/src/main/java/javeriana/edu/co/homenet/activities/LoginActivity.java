package javeriana.edu.co.homenet.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.anfitrion.AnfitrionMenuActivity;
import javeriana.edu.co.homenet.activities.guia.GuiaPrincipalActivity;
import javeriana.edu.co.homenet.activities.huesped.MenuHuespedActivity;
import javeriana.edu.co.homenet.models.Usuario;

public class LoginActivity extends AppCompatActivity {

    public static final String PATH_USERS="users/";
    public static final String PATH_USERS_GUIAS="usersGuias/";

    Button registrarse;
    Button iniciarSesion;
    EditText correo;
    EditText clave;

    private ProgressDialog nProgressDialog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    String uid;
    String uid2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_login);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        registrarse = findViewById(R.id.btRegistrarseLA);
        iniciarSesion = findViewById(R.id.btLogInLA);
        correo = findViewById(R.id.etCorreoLA);
        clave = findViewById(R.id.etContrasenaLA);

        nProgressDialog = new ProgressDialog(LoginActivity.this);

            mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    myRef = database.getReference(PATH_USERS);
                    myRef.orderByKey().endAt(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                Usuario u = singleSnapshot.getValue(Usuario.class);
                                uid = singleSnapshot.getKey();
                                if (uid.equals(user.getUid())){
                                    if (u.getTipoUsuario().equals("Huésped")) {
                                        startActivity(new Intent(LoginActivity.this, MenuHuespedActivity.class));
                                    }else {
                                        startActivity(new Intent(LoginActivity.this,AnfitrionMenuActivity.class));
                                    }
                                }
                            }
                            myRef2 = database.getReference(PATH_USERS_GUIAS);
                            myRef2.orderByKey().endAt(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                        Usuario u = singleSnapshot.getValue(Usuario.class);
                                        uid2 = singleSnapshot.getKey();
                                        Log.i("GUIA",user.getUid());
                                        if (uid2.equals(user.getUid())){
                                            startActivity(new Intent(LoginActivity.this,GuiaPrincipalActivity.class));
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.w("Firebase database", "error en la consulta", databaseError.toException());
                                }
                            });

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("Firebase database", "error en la consulta", databaseError.toException());
                        }
                    });
                } else {
                    // User is signed out
                }
            }
        };

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nProgressDialog.setMessage("Iniciando sesión...");
                nProgressDialog.show();
                signInUser();
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    protected void signInUser(){
        if(validateForm()){
            String email = correo.getText().toString();
            String password = clave.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                nProgressDialog.dismiss();
                                correo.setError("Correo incorrecto");
                                correo.setText("");
                                clave.setText("");
                                clave.setError("Contraseña incorrecta");
                            }else{
                                nProgressDialog.dismiss();
                            }

                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private boolean validateForm() {
        boolean valid = true;
        String email = correo.getText().toString();
        valid = isEmailValid(email);
        if (TextUtils.isEmpty(email)) {
            valid = false;
            correo.setError("Complete el campo");
        }
        String password = clave.getText().toString();
        if (TextUtils.isEmpty(password)) {
            valid = false;
            clave.setError("Complete el campo");
        }
        return valid;
    }

    private boolean isEmailValid(String email) {
        boolean isValid = true;
        if (!email.contains("@") || !email.contains(".") || email.length() < 5){
            isValid = false;
        }
        return isValid;
    }
}