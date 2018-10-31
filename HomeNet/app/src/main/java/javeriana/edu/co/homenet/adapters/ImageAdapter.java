package javeriana.edu.co.homenet.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javeriana.edu.co.homenet.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
        private Context mContext ;
        List<Uri> mUploads ;

        public ImageAdapter(Context context, List<Uri> uploads){
            mContext=context;
            mUploads=uploads;
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder{
            ImageView imagen;
            public ImageViewHolder(View itemView){
                super(itemView);
                imagen = (ImageView)itemView.findViewById(R.id.image_view_name);
            }
        }
        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
            return new ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {
            Uri currentFile = mUploads.get(position);
            Picasso.with(mContext).load(currentFile).into(holder.imagen);
        }

        @Override
        public int getItemCount() {
            return mUploads.size();
        }
    }
