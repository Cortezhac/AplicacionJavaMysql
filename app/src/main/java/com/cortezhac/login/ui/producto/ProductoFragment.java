package com.cortezhac.login.ui.producto;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cortezhac.login.R;

import java.util.HashMap;

public class ProductoFragment extends Fragment {
    private Button guardarProducto;
    private EditText campoNmbre, campoDescripcion, campoStock, campoPrecio, campoUnidad;
    private Spinner spinCategoria, spinEstado;

    private ProductoViewModel mViewModel;

    public static ProductoFragment newInstance() {
        return new ProductoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Puente que contiene los elementos de la vista
        final View fragemntRoot = inflater.inflate(R.layout.producto_fragment, container, false);
        campoNmbre = fragemntRoot.findViewById(R.id.editNombreProducto);
        campoDescripcion = fragemntRoot.findViewById(R.id.editDescripcionProducto);
        campoStock = fragemntRoot.findViewById(R.id.editStockProducto);
        campoPrecio = fragemntRoot.findViewById(R.id.editPrecioProducto);
        campoUnidad = fragemntRoot.findViewById(R.id.editTipoUnidad);
        spinEstado = fragemntRoot.findViewById(R.id.spinnerEstadoProducto);
        spinCategoria = fragemntRoot.findViewById(R.id.spinnerCategoriasProducto);
        guardarProducto = fragemntRoot.findViewById(R.id.btnGuradarProducto);
        return fragemntRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // La confugrcion de los elementos graficos se realizan en este objeto
        mViewModel = ViewModelProviders.of(this).get(ProductoViewModel.class);
        // TODO: Use the ViewModel
        final HashMap<String,String> claves = new HashMap<>();
        mViewModel.getCategorias(getContext(), spinCategoria, claves);
    }

}