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

public class AnfPubElementosAlojAdapter extends RecyclerView.Adapter<AnfPubElementosAlojAdapter.ElementosViewHOlder> {

    private ArrayList<String> elementos;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick (int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public static class ElementosViewHOlder extends RecyclerView.ViewHolder{

        public TextView elemento;
        public Button delete;

        public ElementosViewHOlder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            elemento = itemView.findViewById(R.id.tvElemento);
            delete = itemView.findViewById(R.id.btDeleteAEA);

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
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }

    public AnfPubElementosAlojAdapter(ArrayList<String> elementos){
        this.elementos =  elementos;
    }

    @NonNull
    @Override
    public ElementosViewHOlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_anf_elementos_aloj, viewGroup, false);
        ElementosViewHOlder evh = new ElementosViewHOlder(v, listener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ElementosViewHOlder elementosViewHOlder, int i) {
        elementosViewHOlder.elemento.setText(elementos.get(i));
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }
}
