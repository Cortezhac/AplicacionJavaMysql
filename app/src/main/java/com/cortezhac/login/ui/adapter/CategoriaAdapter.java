package com.cortezhac.login.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cortezhac.login.R;
import com.cortezhac.login.data.model.tb_categoria;
import com.cortezhac.login.ui.categoria.CategoriaFragment;

import java.util.ArrayList;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriasViewHolder> implements View.OnClickListener{
    ArrayList<tb_categoria> listaArticulos;
    private View.OnClickListener listener; // Escuchador de evnto click

    public CategoriaAdapter(ArrayList<tb_categoria> listaArticulos){
        this.listaArticulos = listaArticulos;
    }

    @NonNull
    @Override
    public CategoriasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_categoria_recycler_layout,null, false);
        vista.setOnClickListener(this);// asignar evento onclick a la vista
        return new CategoriasViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriasViewHolder holder, int position) {
        holder.id.setText(String.valueOf(this.listaArticulos.get(position).getId()));
        holder.nombre.setText(listaArticulos.get(position).getNombre());
        holder.estado.setText(listaArticulos.get(position).getEstadoString());
    }

    @Override
    public int getItemCount() {
        return this.listaArticulos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    // Evento onclick del implements
    @Override
    public void onClick(View  view){
        if(listener != null){
            this.listener.onClick(view);
        }
    }

    public class CategoriasViewHolder extends RecyclerView.ViewHolder{
        TextView id, nombre, estado;
        public CategoriasViewHolder(@NonNull View itemView) {
            super(itemView);
            this.id = (TextView) itemView.findViewById(R.id.customTextId);
            this.nombre = (TextView) itemView.findViewById(R.id.customTextNombre);
            this.estado = (TextView) itemView.findViewById(R.id.customTextEstado);
        }
    }
}
