package javeriana.edu.co.homenet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.OpinionGuia;

public class ComentarioAdapter extends ArrayAdapter<OpinionGuia> {

    public ComentarioAdapter (Context context, ArrayList<OpinionGuia> comentarios) {
        super(context, 0, comentarios);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OpinionGuia oG = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comentario_gc, parent, false);
        }
        RatingBar rb = convertView.findViewById(R.id.rbCalificacionICGC);
        TextView comment = convertView.findViewById(R.id.tvComentarioICGC);
        rb.setRating((float)oG.getCalificacion());
        comment.setText(oG.getDescripcion());
        return convertView;
    }
}
