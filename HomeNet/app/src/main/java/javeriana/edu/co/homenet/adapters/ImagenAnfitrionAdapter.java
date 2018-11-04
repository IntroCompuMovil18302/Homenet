package javeriana.edu.co.homenet.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javeriana.edu.co.homenet.R;

public class ImagenAnfitrionAdapter extends PagerAdapter {

    Activity actividad;
    List<String> imagenes ;
    LayoutInflater inflater;

    public ImagenAnfitrionAdapter(Activity actividad, List<String> imagenes) {
        this.actividad = actividad;
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return imagenes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater)actividad.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView =inflater.inflate(R.layout.it_anf_pub_img,container,false);

        ImageView image;
        image = (ImageView)itemView.findViewById(R.id.ivImagenAPA);
        DisplayMetrics dis = new DisplayMetrics();
        actividad.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);
        try{
            Picasso.with(actividad.getApplicationContext())
                    .load(imagenes.get(position))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(image);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }
}
