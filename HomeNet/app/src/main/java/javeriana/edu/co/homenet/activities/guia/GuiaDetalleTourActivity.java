package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.activities.huesped.guias.HuespedVerRecorridoTourActivity;
import javeriana.edu.co.homenet.models.Tour;
import javeriana.edu.co.homenet.models.Ubicacion;
import javeriana.edu.co.homenet.utils.Punto;
import javeriana.edu.co.homenet.utils.UtilidadesMapa;

public class GuiaDetalleTourActivity extends AppCompatActivity {

    public static final String PATH_TOURS="Tours/";

    BottomNavigationView navigation;
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tours:
                    item.setIntent(new Intent(GuiaDetalleTourActivity.this, GuiaPrincipalActivity.class));
                    return true;
                case R.id.navigation_new_tour:
                    item.setIntent(new Intent(GuiaDetalleTourActivity.this, GuiaCrearTourActivity.class));
                    return true;
                case R.id.navigation_history_tours:
                    item.setIntent(new Intent(GuiaDetalleTourActivity.this, GuiaHistorialToures.class));
                    return true;
                case R.id.navigation_config:
                    item.setIntent(new Intent(GuiaDetalleTourActivity.this, GuiaCalificacionesActivity.class));
                    return true;
            }
            return false;
        }
    };

    TextView precio;
    TextView descripcion;
    TextView nombre;
    TextView fecha;
    TextView hora;
    TextView duracion;
    TextView capacidad;
    ImageView poster;
    Button recorrido;
    Button renovar;
    Button editar;
    Button eliminar;
    Button usuarios;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    private String idTour;
    private Tour tour;

    ArrayList<String> paradasLat = new ArrayList<>();
    ArrayList<String> paradasLong = new ArrayList<>();
    ArrayList<String> titulos = new ArrayList<>();
    ArrayList<String> descripciones = new ArrayList<>();

    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_detalle_tour);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        precio = findViewById(R.id.tvPrecioGDT);
        descripcion = findViewById(R.id.tvDescripcionITGP2);
        nombre = findViewById(R.id.tvNombreGDT);
        fecha = findViewById(R.id.tvDateGDT);
        hora = findViewById(R.id.tvHourGDT);
        duracion = findViewById(R.id.tvDurationGDT);
        capacidad = findViewById(R.id.tvCapacidadGDT);
        poster = findViewById(R.id.imgTourGDT);
        recorrido = findViewById(R.id.btRecorridoGDT);
        renovar = findViewById(R.id.btRenovarGDT);
        editar = findViewById(R.id.btEditarGDT);
        eliminar = findViewById(R.id.btEliminarGDT);
        usuarios = findViewById(R.id.btVerGDT);

        Intent bA = getIntent();
        Bundle b = bA.getExtras();
        idTour = b.getString("idTour");
        findTour();

        renovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = GuiaRenovarTourActivity.newInstance(idTour);
                newFragment.show(getSupportFragmentManager(), "missiles");
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = GuiaEliminarTourActivity.newInstance(idTour);
                newFragment.show(getSupportFragmentManager(), "missiles");
            }
        });
        usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("idTour", idTour);
                startActivity(new Intent(v.getContext(),GuiaUsuariosTourActivity.class).putExtras(b));
            }
        });

        request= Volley.newRequestQueue(getApplicationContext());

        recorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarRutas ();

                Intent intent = new Intent(v.getContext(),HuespedVerRecorridoTourActivity.class);
                Bundle b = new Bundle();
                b.putStringArrayList("latitudes", paradasLat);
                b.putStringArrayList("longitudes", paradasLong);
                b.putStringArrayList("titulos", titulos);
                b.putStringArrayList("descripciones", descripciones);
                startActivity(intent.putExtras(b));
            }
        });

    }

    private void findTour() {
        myRef = database.getReference(PATH_TOURS/*+"/"+idTour*/);
        myRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Tour itour = singleSnapshot.getValue(Tour.class);
                    if (singleSnapshot.getKey().equals(idTour)) {
                        tour = itour;
                    }
                }
                for (Ubicacion u : tour.getRecorrido()){
                    paradasLat.add(Double.toString(u.getLatitude()));
                    paradasLong.add(Double.toString(u.getLongitude()));
                    titulos.add(u.getTitulo());
                    descripciones.add(u.getDescripcion());
                }
                llenarDatosVista();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }

    private void llenarDatosVista(){
        precio.setText(String.valueOf(tour.getPrecio()).concat(" "+tour.getMoneda()));
        descripcion.setText(tour.getDescripcion());
        nombre.setText(tour.getTitulo());
        fecha.setText(tour.getFecha());
        hora.setText(tour.getHora());
        duracion.setText(String.valueOf(tour.getDuracion()).concat(" horas"));
        capacidad.setText(String.valueOf(tour.getInscritos()).concat(" de "+tour.getCapacidad()));
        try{
            Picasso.get()
                    .load(tour.getUrlImg())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(poster);
        }catch(Exception ex){
            ex.printStackTrace();
        }
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
            Intent intent = new Intent(GuiaDetalleTourActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void buscarRutas (){
        if (titulos.size() > 1){
            UtilidadesMapa.routes.clear();
            for (int i = 0; i < titulos.size()-1 ;i++) {
                Punto p1 = new Punto();
                Punto p2 = new Punto();
                p1.setLatitudInicial(Double.valueOf(paradasLat.get(i)));
                p1.setLatitudInicial(Double.valueOf(paradasLong.get(i)));
                UtilidadesMapa.coordenadas.add(p1);
                p2.setLatitudFinal(Double.valueOf(paradasLat.get(i+1)));
                p2.setLongitudFinal(Double.valueOf(paradasLong.get(i+1)));
                UtilidadesMapa.coordenadas.add(p2);
                webServiceObtenerRuta(paradasLat.get(i), paradasLong.get(i),
                        paradasLat.get(i + 1), paradasLong.get(i + 1));
            }
        }
    }

    private void webServiceObtenerRuta(String latitudInicial, String longitudInicial, String latitudFinal, String longitudFinal) {

        String url="https://maps.googleapis.com/maps/api/directions/json?origin="+latitudInicial+","+longitudInicial
                +"&destination="+latitudFinal+","+longitudFinal+"&key=AIzaSyAchBP3BNVvqSHTbu02DA0PQur2d_RKE_M";

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Este método PARSEA el JSONObject que retorna del API de Rutas de Google devolviendo
                //una lista del lista de HashMap Strings con el listado de Coordenadas de Lat y Long,
                //con la cual se podrá dibujar pollinas que describan la ruta entre 2 puntos.
                JSONArray jRoutes = null;
                JSONArray jLegs = null;
                JSONArray jSteps = null;

                try {

                    jRoutes = response.getJSONArray("routes");

                    /** Traversing all routes */
                    for(int i=0;i<jRoutes.length();i++){
                        jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                        List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                        /** Traversing all legs */
                        for(int j=0;j<jLegs.length();j++){
                            jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                            /** Traversing all steps */
                            for(int k=0;k<jSteps.length();k++){
                                String polyline = "";
                                polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                                List<LatLng> list = decodePoly(polyline);

                                /** Traversing all points */
                                for(int l=0;l<list.size();l++){
                                    HashMap<String, String> hm = new HashMap<String, String>();
                                    hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                                    hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                                    path.add(hm);
                                }
                            }
                            UtilidadesMapa.routes.add(path);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                Log.d("ERROR: ", error.toString());
            }
        }
        );

        request.add(jsonObjectRequest);
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
