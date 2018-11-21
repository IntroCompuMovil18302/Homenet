package javeriana.edu.co.homenet.activities.huesped.guias;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import javeriana.edu.co.homenet.models.Tour;
import javeriana.edu.co.homenet.models.Ubicacion;
import javeriana.edu.co.homenet.utils.Punto;
import javeriana.edu.co.homenet.utils.UtilidadesMapa;

public class HuespedVerTourActivity extends AppCompatActivity {

    public static final String PATH_TOUR="Tours/";

    TextView nombreAnuncio;
    ImageView posterAnuncio;
    TextView descripcionAnuncio;
    TextView precioAnuncio;
    TextView horaInicio;
    TextView duracion;
    TextView fecha;
    TextView capacidad;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    Button verRecorridos;
    Button verGuia;
    Button inscribirse;
    Bundle b;
    Tour t;
    String idGuia;
    ArrayList<String> paradasLat = new ArrayList<>();
    ArrayList<String> paradasLong = new ArrayList<>();
    ArrayList<String> titulos = new ArrayList<>();
    ArrayList<String> descripciones = new ArrayList<>();

    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_ver_tour);

        verRecorridos = findViewById(R.id.btVerRecorridosHVTA);
        verGuia = findViewById(R.id.btVerGuiaHVTA);
        inscribirse = findViewById(R.id.btInscribirseHVTA);
        nombreAnuncio = findViewById(R.id.tvNombreAnuncioHVTA);
        posterAnuncio = findViewById(R.id.ivAnuncioHVTA);
        descripcionAnuncio = findViewById(R.id.mtdescAnuncioHVMTA);
        precioAnuncio = findViewById(R.id.tvTarifaAnuncioHVTA);
        horaInicio = findViewById(R.id.tvHoraInicioHVTA);
        duracion = findViewById(R.id.tvDuracionHVTA);
        fecha = findViewById(R.id.tvFechaHVTA);
        capacidad = findViewById(R.id.tvCapacidadHVTA);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(PATH_TOUR);

        b = getIntent().getExtras();

        getTour(b.getString("idTour"));

        verRecorridos.setOnClickListener(new View.OnClickListener() {
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

        request= Volley.newRequestQueue(getApplicationContext());

        verGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), HuespedVerInfoGuiaActivity.class);
                Bundle b = new Bundle();
                b.putString("idGuia", t.getIdGuia());
                startActivity(i.putExtras(b));
            }
        });

        inscribirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Actualizar capacidad
                Toast.makeText(HuespedVerTourActivity.this,
                        "Se ha registrado en el tour "+t.getTitulo(), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void getTour (final String id) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Tour tt = singleSnapshot.getValue(Tour.class);
                    if (singleSnapshot.getKey().equals(id)){
                        t = tt;
                    }
                }
                for (Ubicacion u : t.getRecorrido()){
                    paradasLat.add(Double.toString(u.getLatitude()));
                    paradasLong.add(Double.toString(u.getLongitude()));
                    titulos.add(u.getTitulo());
                    descripciones.add(u.getDescripcion());
                }
                setValues();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }

    private void setValues () {
        nombreAnuncio.setText(t.getTitulo());
        try{
            Picasso.get()
                    .load(t.getUrlImg())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(posterAnuncio);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        descripcionAnuncio.setText(t.getDescripcion());
        precioAnuncio.setText(Integer.toString(t.getPrecio())+t.getMoneda());
        horaInicio.setText(t.getHora());
        duracion.setText(Integer.toString(t.getDuracion())+"h");
        fecha.setText(t.getFecha());
        capacidad.setText(Integer.toString(t.getCapacidad()));
        idGuia = t.getIdGuia();
    }
}
