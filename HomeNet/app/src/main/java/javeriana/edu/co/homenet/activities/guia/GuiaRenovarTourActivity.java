package javeriana.edu.co.homenet.activities.guia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import javeriana.edu.co.homenet.R;

public class GuiaRenovarTourActivity extends DialogFragment {

    public static final String PATH_TOURS="Tours/";

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private String tourID;

    EditText fecha;
    EditText hora;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_guia_renovar_tour, null);

        tourID = (String) getArguments().get("idTour");
        database = FirebaseDatabase.getInstance();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        fecha = view.findViewById(R.id.etFechaGRT);
                        hora = view.findViewById(R.id.etHoraGRT);
                        myRef = database.getReference(PATH_TOURS+"/"+tourID);
                        Map<String, Object> tourUpdates = new HashMap<>();
                        tourUpdates.put("fecha", hora.getText().toString());
                        tourUpdates.put("hora", hora.getText().toString());
                        myRef.updateChildren(tourUpdates);
                        Bundle b = new Bundle();
                        b.putString("idTour", tourID);
                        Intent intent = new  Intent(getContext(), GuiaDetalleTourActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GuiaRenovarTourActivity.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public static GuiaRenovarTourActivity newInstance(String idTour) {
        GuiaRenovarTourActivity f = new GuiaRenovarTourActivity();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("idTour", idTour);
        f.setArguments(args);
        return f;
    }
}
