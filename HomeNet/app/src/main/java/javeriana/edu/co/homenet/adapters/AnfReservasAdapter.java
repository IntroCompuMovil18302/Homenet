package javeriana.edu.co.homenet.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.Reserva;

public class AnfReservasAdapter extends RecyclerView.Adapter<AnfReservasAdapter.ReservasViewHolder>{
    private ArrayList<Reserva> reservas;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick (int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public static class ReservasViewHolder extends RecyclerView.ViewHolder{
        public TextView huesped;
        public TextView fecha;

        public ReservasViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            huesped = itemView.findViewById(R.id.tvElemento);
            fecha = itemView.findViewById(R.id.btDeleteAEA);

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

    public AnfReservasAdapter(ArrayList<Reserva> reservas){
        this.reservas =  reservas;
    }

    @NonNull
    @Override
    public AnfReservasAdapter.ReservasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_anf_elementos_aloj, viewGroup, false);
        AnfReservasAdapter.ReservasViewHolder evh = new AnfReservasAdapter.ReservasViewHolder(v, listener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull AnfReservasAdapter.ReservasViewHolder reservasViewHolder, int i) {
        //reservasViewHolder.elemento.setText(reservas.get(i));
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }
}
