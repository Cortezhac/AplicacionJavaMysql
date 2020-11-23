package com.cortezhac.login.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cortezhac.login.R;
import com.cortezhac.login.data.model.tb_categoria;
import com.cortezhac.login.data.model.tb_usuario;

import java.util.ArrayList;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuariosViewHolder> implements View.OnClickListener{
    ArrayList<tb_usuario> listaArticulos;
    private View.OnClickListener listener; // Escuchador de evnto click

    public UsuarioAdapter(ArrayList<tb_usuario> listaArticulos){
        this.listaArticulos = listaArticulos;
    }

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_usuario_recycler_layout,null, false);
        vista.setOnClickListener(this);// asignar evento onclick a la vista
        return new UsuariosViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {
        holder.id.setText(String.valueOf(this.listaArticulos.get(position).getId()));
        holder.nombre.setText(listaArticulos.get(position).getNombre());
        holder.apellido.setText(listaArticulos.get(position).getApellido());
        holder.correo.setText(listaArticulos.get(position).getCorreo());
        holder.usuario.setText(listaArticulos.get(position).getUsuario());
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

    public class UsuariosViewHolder extends RecyclerView.ViewHolder{
        TextView id, nombre, estado, apellido, correo, usuario;
        public UsuariosViewHolder(@NonNull View itemView) {
            super(itemView);
            this.id = (TextView) itemView.findViewById(R.id.customTextIdUsuario);
            this.nombre = (TextView) itemView.findViewById(R.id.customTextNombreUsuario);
            this.apellido = (TextView) itemView.findViewById(R.id.customTextApellidoUsuario);
            this.correo = (TextView) itemView.findViewById(R.id.customTextCorreoUsuario);
            this.usuario = (TextView) itemView.findViewById(R.id.customTextUserUsuario);
            this.estado = (TextView) itemView.findViewById(R.id.customTextEstadoUsuario);
        }
    }
}
