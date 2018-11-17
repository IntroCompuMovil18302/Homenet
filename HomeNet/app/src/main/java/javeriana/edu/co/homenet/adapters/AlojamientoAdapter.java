package javeriana.edu.co.homenet.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.Alojamiento;

public class AlojamientoAdapter  extends ArrayAdapter<Alojamiento> {

    public AlojamientoAdapter(Context context, ArrayList<Alojamiento> alojamientos) {
        super(context, 0, alojamientos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Alojamiento alojamiento = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alojamiento_hca, parent, false);
        }
        // Lookup view for data population
        TextView tvNombre = convertView.findViewById(R.id.tvNombreITGP);
        TextView tvDescripcion = convertView.findViewById(R.id.tvDescripcionITGP);
        TextView tvPrecio = convertView.findViewById(R.id.tvPrecioITGP);
        TextView tvDist = convertView.findViewById(R.id.tvDistIA);
        // Populate the data into the template view using the data object
        tvNombre.setText(alojamiento.getNombre());
        tvDescripcion.setText(alojamiento.getDescripcion());
        tvPrecio.setText(String.valueOf(alojamiento.getPrecio()));
        tvDist.setText(String.valueOf(alojamiento.getDist()));
        //Image
        if(alojamiento.getUrlImgs()!= null && alojamiento.getUrlImgs().size()>0){
            ImageView imgAlojamiento = convertView.findViewById(R.id.imgAlojamientoIA);
            imgAlojamiento.setImageURI(Uri.parse(alojamiento.getUrlImgs().get(0)));
            Glide.with(convertView.getContext())
                    .load(alojamiento.getUrlImgs().get(0))
                    .into(imgAlojamiento);
        }
        // Return the completed view to render on screen
        return convertView;
    }

}
