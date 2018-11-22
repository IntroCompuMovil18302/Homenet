package javeriana.edu.co.homenet.activities.guia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javeriana.edu.co.homenet.R;

public class GuiaEliminarTourActivity extends DialogFragment {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public static final String PATH_TOURS="Tours/";

    private String tourID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        tourID = (String) getArguments().get("idTour");
        database = FirebaseDatabase.getInstance();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.activity_guia_eliminar_tour, null))
                // Add action buttons
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        myRef = database.getReference(PATH_TOURS+"/"+tourID);
                        myRef.removeValue();
                        Intent intent = new  Intent(getContext(), GuiaPrincipalActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GuiaEliminarTourActivity.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public static GuiaEliminarTourActivity newInstance(String idTour) {
        GuiaEliminarTourActivity f = new GuiaEliminarTourActivity();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("idTour", idTour);
        f.setArguments(args);
        return f;
    }
}
