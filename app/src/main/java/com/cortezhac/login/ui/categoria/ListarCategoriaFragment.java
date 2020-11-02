package com.cortezhac.login.ui.categoria;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cortezhac.login.R;

public class ListarCategoriaFragment extends Fragment {
    private RecyclerView recycler;
    private ListarCategoriaViewModel mViewModel;
    private EditText nombre, estado;
    public static ListarCategoriaFragment newInstance() {
        return new ListarCategoriaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.listar_categoria_fragment, container, false);
        recycler = root.findViewById(R.id.recyclerCategorias);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListarCategoriaViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.getListaCategoriasRemoto(getContext(), recycler , this);
    }

}