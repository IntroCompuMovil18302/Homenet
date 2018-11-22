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
import javeriana.edu.co.homenet.models.Usuario;

public class UserAdapter extends ArrayAdapter<Usuario> {

    public UserAdapter (Context context, ArrayList<Usuario> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Usuario u = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_gut, parent, false);
        }
        ImageView imgUser = convertView.findViewById(R.id.imgPerfilIUGUT);
        TextView userName = convertView.findViewById(R.id.tvUserIUGUT);
        TextView telefono = convertView.findViewById(R.id.tvTelefonoIUGUT);
        TextView mail = convertView.findViewById(R.id.tvMailIUGUT);

        userName.setText(u.getNombre());
        try{
            Picasso.get()
                    .load(u.getUrlImg())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imgUser);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        telefono.setText(u.getTelefono());
        mail.setText(u.getCorreo());
        return convertView;
    }
}
