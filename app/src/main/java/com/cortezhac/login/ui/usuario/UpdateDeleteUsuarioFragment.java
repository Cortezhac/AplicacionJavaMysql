package com.cortezhac.login.ui.usuario;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cortezhac.login.R;

public class UpdateDeleteUsuarioFragment extends Fragment {
    private Button editarUsuario, eliminarusuario;
    private EditText campoNombre, campoApe, campoCorreo, campoUser, campoContra, campoRespuesta;
    private Spinner spinTipo, spinEstado, spinPregunta;
    private String id, estadoString , estados, tipos, preguntas;

    private UpdateDeleteUsuarioViewModel mViewModel;

    public static UpdateDeleteUsuarioFragment newInstance() {
        return new UpdateDeleteUsuarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragemntRoot = inflater.inflate(R.layout.update_delete__usuariofragment, container, false);
        campoNombre = fragemntRoot.findViewById(R.id.NombreUsuarioUpdate);
        campoApe = fragemntRoot.findViewById(R.id.ApellidoUsuarioUpdate);
        campoCorreo = fragemntRoot.findViewById(R.id.CorreoUsuarioUpdate);
        campoUser = fragemntRoot.findViewById(R.id.UserUsuarioUpdate);
        campoContra = fragemntRoot.findViewById(R.id.ContrasenaUsuarioUpdate);
        campoRespuesta = fragemntRoot.findViewById(R.id.RespuestaUsuarioUpdate);
        spinEstado = fragemntRoot.findViewById(R.id.spinnerEstadoUsuarioUpdate);
        spinTipo = fragemntRoot.findViewById(R.id.spinnerTipoUsuarioUpdate);
        spinPregunta = fragemntRoot.findViewById(R.id.spinnerPreguntaUsuarioUpdate);
        editarUsuario = fragemntRoot.findViewById(R.id.btnActualizarUsuarioUpdate);
        eliminarusuario = fragemntRoot.findViewById(R.id.btnEliminarUsuarioUpdate);
        return fragemntRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdateDeleteUsuarioViewModel.class);
        // TODO: Use the ViewModel
        id = String.valueOf(getArguments().getInt("id"));
        campoNombre.setText(getArguments().getString("nombre"));
        campoApe.setText(getArguments().getString("apellidos"));
        campoCorreo.setText(getArguments().getString("correo"));
        campoUser.setText(getArguments().getString("usuario"));
        campoContra.setText(getArguments().getString("clave"));
        campoRespuesta.setText(getArguments().getString("respuesta"));
        estadoString = "";
        estados = String.valueOf(getArguments().getInt("estado"));
        tipos = String.valueOf(getArguments().getInt("tipo"));
        preguntas = String.valueOf(getArguments().getString("pregunta"));

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mViewModel.getEstados());
        spinEstado.setAdapter(adapter);
        spinEstado.setSelection(getArguments().getInt("estado"));
        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        ArrayAdapter adapter1 = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mViewModel.getTipos());
        spinTipo.setAdapter(adapter1);
        spinTipo.setSelection(getArguments().getInt("estado"));
        spinTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipos = String.valueOf(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tipos = String.valueOf(getArguments().getInt("estado"));
            }
        });

        ArrayAdapter adapter2 = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mViewModel.getPreguntas());
        spinPregunta.setAdapter(adapter2);
        spinPregunta.setSelection(getArguments().getInt("estado"));
        spinPregunta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                preguntas = String.valueOf(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                preguntas = String.valueOf(getArguments().getInt("estado"));
            }
        });

        editarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Procesando...", Toast.LENGTH_SHORT).show();
                mViewModel.actualizaDatosRemotos(getContext(), id, campoNombre.getText().toString(), campoApe.getText().toString(), campoCorreo.getText().toString(),
                        campoUser.getText().toString(), campoContra.getText().toString(), tipos, estados, preguntas, campoRespuesta.getText().toString());
                if(getActivity() != null){
                    getActivity().onBackPressed();
                }
            }
        });

        eliminarusuario.setOnClickListener(new View.OnClickListener() {
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