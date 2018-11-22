package javeriana.edu.co.homenet.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.OpinionAlojamiento;
import javeriana.edu.co.homenet.models.Reserva;

public class AnfComentariosAdapter extends RecyclerView.Adapter<AnfComentariosAdapter.ComentariosViewHolder>{
    private ArrayList<OpinionAlojamiento> opiniones;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick (int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public static class ComentariosViewHolder extends RecyclerView.ViewHolder{
        public TextView comentario;
        public RatingBar calificacion;

        public ComentariosViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            comentario = itemView.findViewById(R.id.tvUsuarioAR);
            calificacion = itemView.findViewById(R.id.rbCalificacionAC);

            // listener para todo el item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // listener para el boton eliminar
        }
    }

    public AnfComentariosAdapter(ArrayList<OpinionAlojamiento> opiniones){
        this.opiniones =  opiniones;
    }

    @NonNull
    @Override
    public AnfComentariosAdapter.ComentariosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_anfitrion_comentarios, viewGroup, false);
        AnfComentariosAdapter.ComentariosViewHolder evh = new AnfComentariosAdapter.ComentariosViewHolder(v, listener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull AnfComentariosAdapter.ComentariosViewHolder comentariosViewHolder, int i) {
        comentariosViewHolder.comentario.setText(opiniones.get(i).getComentario());
        System.out.println("======="+opiniones.get(i).getComentario());
        if(comentariosViewHolder.calificacion != null)
        {
            comentariosViewHolder.calificacion.setRating((float) opiniones.get(i).getCalificacion());
        }
        else {
            System.out.println("NULLLLLLLLLLLLLLLLLLLLLLLLLLL");
        }

    }

    @Override
    public int getItemCount() {
        return opiniones.size();
    }
}
