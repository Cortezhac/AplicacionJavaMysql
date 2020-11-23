package com.cortezhac.login.ui.usuario;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cortezhac.login.R;
import com.cortezhac.login.ui.categoria.ListarCategoriaFragment;
import com.cortezhac.login.ui.categoria.ListarCategoriaViewModel;

public class ListarUsuario extends Fragment {
    private RecyclerView recycler;
    private ListarUsuarioViewModel mViewModel;

    public static ListarUsuario newInstance() {
        return new ListarUsuario();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.listar_usuario_fragment, container, false);
        recycler = root.findViewById(R.id.recyclerUsuarios);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListarUsuarioViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getListaUsuariosRemoto(getContext(), recycler , this);
    }

}