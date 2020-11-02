package com.cortezhac.login.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cortezhac.login.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView fragNombre, fragApellido;
    private Button fragBtnConsultar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // En este objeto se crea la logica del diseño para enlazarla con eventos en la clase actual
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Todo tu binding aqui
        final TextView textView = root.findViewById(R.id.text_home);
        fragNombre = root.findViewById(R.id.txtViewNombre);
        fragApellido = root.findViewById(R.id.txtViewApellidos);

        // obtiene el texto muted del modelo de datos
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }
}