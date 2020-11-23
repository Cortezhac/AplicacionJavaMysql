package com.cortezhac.login.ui.usuario;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cortezhac.login.R;
import com.cortezhac.login.data.model.SentingURI;
import com.cortezhac.login.data.volley.MySingleton;
import com.cortezhac.login.ui.producto.ProductoFragment;
import com.cortezhac.login.ui.producto.ProductoViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsuarioFragment extends Fragment {
    private Button guardarUsuario, listarusuario;
    private EditText campoNmbre, campoApe, campoCorreo, campoUser, campoContra, campoRespuesta;
    // Elementos para el Spinner
    private Spinner spinTipo, spinEstado, spinPregunta;
    // Link del servidor
    SentingURI Setings = new SentingURI();
    private final String URL = Setings.IP1 +"api/producto/consultarProductos.php";
    // Modelo de manipulacion
    private UsuarioViewModel mViewModel;
    private String estado, pregunta, tipo;

    public static UsuarioFragment newInstance() {
        return new UsuarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View fragemntRoot = inflater.inflate(R.layout.usuario_fragment, container, false);
        campoNmbre = fragemntRoot.findViewById(R.id.editNombreUsuario);
        campoApe = fragemntRoot.findViewById(R.id.editApellidoUsuario);
        campoCorreo = fragemntRoot.findViewById(R.id.editCorreoUsuario);
        campoUser = fragemntRoot.findViewById(R.id.editUserUsuario);
        campoContra = fragemntRoot.findViewById(R.id.editContrasenaUsuario);
        campoRespuesta = fragemntRoot.findViewById(R.id.editRespuestaUsuario);
        spinEstado = fragemntRoot.findViewById(R.id.spinnerEstadoProducto);
        spinTipo = fragemntRoot.findViewById(R.id.spinnerTipoUsuario);
        spinPregunta = fragemntRoot.findViewById(R.id.spinnerPreguntaUsuario);
        guardarUsuario = fragemntRoot.findViewById(R.id.btnGuradarProducto);
        listarusuario = fragemntRoot.findViewById(R.id.btnListarUsuario);
        return fragemntRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        // Iniciamos la lista de estados con sus claves
        // Evento onItemSelected de las categorias
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mViewModel.getEstado());
        spinEstado.setAdapter(adapter);
        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estado = mViewModel.getClavesEstados().get(spinEstado.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                estado = "0";
            }
        });

        // Obtener preguntas
        ArrayAdapter adapter1 = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mViewModel.getPregunta());
        spinPregunta.setAdapter(adapter1);
        spinPregunta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pregunta = mViewModel.getPreguntas().get(spinPregunta.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                pregunta = "0";
            }
        });

        // Obtener categorias
        ArrayAdapter adapter2 = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mViewModel.getTipo());
        spinTipo.setAdapter(adapter2);
        spinTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipo = mViewModel.getTipos().get(spinTipo.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tipo = "0";
            }
        });

        guardarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(campoNmbre.getText().toString().length() > 0){
                    Toast.makeText(getContext(), "Procesando...." ,Toast.LENGTH_SHORT).show();
                    mViewModel.guardarDatosRemotos(getContext(), campoNmbre, campoApe, campoCorreo,campoUser, campoContra, tipo, estado, pregunta, campoRespuesta);
                }else{
                    campoNmbre.setError("Campo obligatorio");
                }
            }
        });

        listarusuario.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_usuarioFragment_to_listarUsuarioFragment));
    }
}