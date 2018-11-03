package javeriana.edu.co.homenet.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import javeriana.edu.co.homenet.models.Disponibilidad;

public class AnfPubAlojamientoAdapter
        extends
        RecyclerView.Adapter<AnfPubAlojamientoAdapter.MyViewHolder>{

    private List<Disponibilidad> disponibilidads ;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

    }
}
