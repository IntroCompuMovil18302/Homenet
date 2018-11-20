package javeriana.edu.co.homenet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javeriana.edu.co.homenet.R;

public class TourGuiaAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;
    String ivTour;
    String nombreTour;

    public TourGuiaAdapter(Context contexto, String ivTour, String nombreTour) {
        this.contexto = contexto;
        this.ivTour = ivTour;
        this.nombreTour = nombreTour;
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View vista = inflater.inflate(R.layout.item_tour_guia, null);
        ImageView imgTour = vista.findViewById(R.id.imTour);
        TextView nTour = vista.findViewById(R.id.tvNombreTour);
        nTour.setText(nombreTour);
        try{
            Picasso.get()
                    .load(ivTour)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imgTour);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return vista;
    }
}
