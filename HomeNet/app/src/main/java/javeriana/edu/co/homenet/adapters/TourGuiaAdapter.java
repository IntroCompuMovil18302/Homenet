package javeriana.edu.co.homenet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.Tour;

public class TourGuiaAdapter extends ArrayAdapter<Tour> {

    public TourGuiaAdapter (Context context, ArrayList<Tour> toures) {
        super(context, 0, toures);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tour t = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tour_gp_gut, parent, false);
        }
        ImageView imgTour = convertView.findViewById(R.id.imgTourIT);
        TextView nTour = convertView.findViewById(R.id.tvNombreITGP);
        TextView descTour = convertView.findViewById(R.id.tvDescripcionITGP);
        TextView precioTour = convertView.findViewById(R.id.tvPrecioITGP);
        TextView monedaTour = convertView.findViewById(R.id.tvMonedaITGP);
        TextView fechaTour = convertView.findViewById(R.id.tvDateITGP);
        TextView horaTour = convertView.findViewById(R.id.tvHourITGP);
        nTour.setText(t.getTitulo());
        try{
            Picasso.get()
                    .load(t.getUrlImg())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imgTour);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        descTour.setText(t.getDescripcion());
        precioTour.setText(String.valueOf(t.getPrecio()));
        monedaTour.setText(t.getMoneda());
        fechaTour.setText(t.getFecha());
        horaTour.setText(t.getHora());
        return convertView;
    }
}
