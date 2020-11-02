package com.cortezhac.login.ui.categoria;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cortezhac.login.R;

import java.util.ArrayList;

public class UpdateDeleteFragment extends Fragment {
    private EditText nombre;
    private Spinner estado;
    private UpdateDeleteViewModel mViewModel;
    private String id, estadoString , estados;
    private Button btnActualizar, btnBorrar;

    public static UpdateDeleteFragment newInstance() {
        return new UpdateDeleteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.update_delete_fragment, container, false);
        nombre = vista.findViewById(R.id.nombreCategoriaUpdateFragment);
        estado = vista.findViewById(R.id.spinnerEstadoUpdate);
        btnActualizar = vista.findViewById(R.id.btnActualizarFragment);
        btnBorrar = vista.findViewById(R.id.btnBorrarFragment);
        return vista;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdateDeleteViewModel.class);
        // TODO: Use the ViewModel
        id = String.valueOf(getArguments().getInt("id"));
        nombre.setText(getArguments().getString("nombre"));
        estadoString = "";
        estados = String.valueOf(getArguments().getInt("estado"));

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mViewModel.getEstados());
        estado.setAdapter(adapter);
        estado.setSelection(getArguments().getInt("estado"));
        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estadoString = mViewModel.getEstados().get(i);
                estados = String.valueOf(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                estados = String.valueOf(getArguments().getInt("estado"));
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Toast.makeText(getContext(), "Procesando...", Toast.LENGTH_SHORT).show();
            mViewModel.actualizaDatosRemotos(getContext(), id, nombre.getText().toString(), estados);
            if(getActivity() != null){
                getActivity().onBackPressed();
            }
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Toast.makeText(getContext(), "Procesando...", Toast.LENGTH_SHORT).show();
            mViewModel.eliminarDatosRemotos(getContext(), id);
            if(getActivity() != null){
                getActivity().onBackPressed();
            }
            }
        });
    }

}