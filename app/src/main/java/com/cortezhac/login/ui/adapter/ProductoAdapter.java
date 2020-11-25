package com.cortezhac.login.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cortezhac.login.R;
import com.cortezhac.login.data.model.tb_producto;
import com.cortezhac.login.ui.producto.ProductoViewModel;

import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> implements View.OnClickListener{
    ArrayList<tb_producto> listaProductos;
    private View.OnClickListener listener; // Escucha de eventos

    public ProductoAdapter(ArrayList<tb_producto> listaProductos){
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Prepara la vista personalizada que se va a utilizar
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_producto_recycler_layout, null, false);
        vista.setOnClickListener(this);// evento onclick
        return new ProductoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        holder.id.setText(String.valueOf(this.listaProductos.get(position).getId_producto()));
        holder.nombre.setText(this.listaProductos.get(position).getNombre_producto());
        holder.estado.setText(this.listaProductos.get(position).getEstado_producto() == 1 ? "Activo" : "Inactivo");
        holder.categoria.setText(String.valueOf(this.listaProductos.get(position).getCategoria()));
    }

    @Override
    public int getItemCount() {
        return this.listaProductos.size();
    }

    public  void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            this.listener.onClick(view);
        }
    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder{
        TextView id, nombre, estado, categoria;
        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.id = (TextView) itemView.findViewById(R.id.txtIdProductoAdapter);
            this.nombre = (TextView) itemView.findViewById(R.id.txtNombreProductoAdapter);
            this.estado = (TextView) itemView.findViewById(R.id.txtEstadoProductoAdapter);
            this.categoria = (TextView) itemView.findViewById(R.id.txtCategoriaProductoAdapter);
        }
    }

}
