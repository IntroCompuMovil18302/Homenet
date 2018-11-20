package javeriana.edu.co.homenet.adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.Alojamiento;

public class AnfAlojamientosAdapter extends RecyclerView.Adapter<AnfAlojamientosAdapter.AlojamientosViewHolder>{

    private ArrayList<Alojamiento> alojamientos;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick (int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public static class AlojamientosViewHolder extends RecyclerView.ViewHolder {

        public TextView tipo;
        public TextView nombre;
        public TextView ubicacion;
        public TextView descripcion;
        public ImageView imagen;

        public AlojamientosViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tipo = itemView.findViewById(R.id.tvTipoAEA);
            nombre = itemView.findViewById(R.id.tvNombreAEA);
            ubicacion = itemView.findViewById(R.id.tvUbicacionAEA);
            descripcion = itemView.findViewById(R.id.tvDescripcionAEA);
            imagen = itemView.findViewById(R.id.ivImagenAlogAEA);

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

        }
    }

    public AnfAlojamientosAdapter(ArrayList<Alojamiento> alojamientos){
        this.alojamientos =  alojamientos;
    }

    @NonNull
    @Override
    public AlojamientosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_anf_alojamientos, viewGroup, false);
        AlojamientosViewHolder evh = new AlojamientosViewHolder(v, listener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull AlojamientosViewHolder alojamientosViewHolder, int i) {
        Alojamiento a = alojamientos.get(i);
        alojamientosViewHolder.tipo.setText(a.getTipo());
        alojamientosViewHolder.nombre.setText(a.getNombre());
        alojamientosViewHolder.ubicacion.setText(a.getUbicacion().getDireccion());
        alojamientosViewHolder.descripcion.setText(a.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return alojamientos.size();
    }
}
