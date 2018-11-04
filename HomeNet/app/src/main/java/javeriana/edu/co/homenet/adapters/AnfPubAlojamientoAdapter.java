package javeriana.edu.co.homenet.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.Disponibilidad;

public class AnfPubAlojamientoAdapter
        extends
        RecyclerView.Adapter<AnfPubAlojamientoAdapter.MyViewHolder>{

    private ArrayList<Disponibilidad> disponibilidads ;

    public AnfPubAlojamientoAdapter(ArrayList<Disponibilidad> disponibilidads) {
        this.disponibilidads = disponibilidads;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_anf_fecha, null, false);
        return new MyViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.finicio.setText(disponibilidads.get(i).getFechaInicio());
        myViewHolder.ffin.setText(disponibilidads.get(i).getFechaFin());
    }

    @Override
    public int getItemCount() {

        return disponibilidads.size();
    }

    public ArrayList<Disponibilidad> getDisponibilidads(){
        return  this.disponibilidads;
    }

    public void addItem(Disponibilidad u) {
        disponibilidads.add(u);
        notifyItemInserted(disponibilidads.size()-1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView finicio;
        TextView ffin;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            finicio = itemView.findViewById(R.id.tvFechaIniAF);
            ffin = itemView.findViewById(R.id.tvFechaFinAF);


        }
    }
}
