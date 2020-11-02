package com.cortezhac.login.ui.categoria;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.cortezhac.login.R;

public class CategoriaFragment extends Fragment {
    private EditText editCategorias;
    private Spinner spinEstadoCat;
    private Button btnGuardarFragment, btnListarFragmento;
    private String estado;
    private CategoriaViewModel mViewModel;

    public static CategoriaFragment newInstance() {
        return new CategoriaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View fragmentRoot = inflater.inflate(R.layout.categoria_fragment, container, false);
        editCategorias = fragmentRoot.findViewById(R.id.nombreCategoriaFragment);
        spinEstadoCat = fragmentRoot.findViewById(R.id.spinnerEstado);
        btnGuardarFragment = fragmentRoot.findViewById(R.id.btnGuardar);
        btnListarFragmento = fragmentRoot.findViewById(R.id.btnListarCategoria);

        return fragmentRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CategoriaViewModel.class);
        // TODO: Use the ViewModel
        // Preparar adapter
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mViewModel.getDatoSpinner());
        spinEstadoCat.setAdapter(adapter);

        spinEstadoCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estado = mViewModel.getSpinnerData().get(spinEstadoCat.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                estado = "0";
            }
        });

        btnGuardarFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editCategorias.getText().toString().length() > 0){
                    Toast.makeText(getContext(), "Procesando...." ,Toast.LENGTH_SHORT).show();
                    mViewModel.guardarDatosRemotos(getContext(),editCategorias,estado);
                }else{
                    editCategorias.setError("Campo obligatorio");
                }
            }
        });
        /* sin animacion
        btnListarFragmento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navegar sin enviar parametros
                NavHostFragment.findNavController(CategoriaFragment.this).navigate(R.id.listarCategoriaFragment);
            }
        });*/
        btnListarFragmento.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_categoriaFragment_to_listarCategoriaFragment));
    }

}