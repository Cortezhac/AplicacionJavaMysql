package com.cortezhac.login.ui.producto;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.cortezhac.login.data.model.tb_producto;
import com.cortezhac.login.data.volley.MySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductoFragment extends Fragment {
    // Elementos de la vista
    private Button guardarProducto , listarProducto;
    private EditText campoNmbre, campoDescripcion, campoStock, campoPrecio, campoUnidad;
    // Elementos para el Spinner
    private Spinner spinCategoria, spinEstado;
    private ArrayList<String> categorias; // Contendra los Strings del espinner
    private HashMap<String, String> clavesCategorias;// Contendra las claves String y su valor correspondiente
    // Link del servidor
    SentingURI Setings = new SentingURI();
    private final String URL = Setings.IP1 +"api/categorias/consultarCategorias.php";
    // Tabla de datos
    tb_producto nuevoProducto = new tb_producto();;
    // Modelo de manipulacion
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
        listarProducto = fragemntRoot.findViewById(R.id.btnListarProducto);
        return fragemntRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // La confugrcion de los elementos graficos se realizan en este objeto
        mViewModel = ViewModelProviders.of(this).get(ProductoViewModel.class);
        // Obtener categorias
        getCategorias(getContext());
        // Evento onItemSelected de las categorias
        spinCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!categorias.get(i).equals("Seleccione una opcion")){
                    nuevoProducto.setCategoria(Integer.parseInt(clavesCategorias.get(categorias.get(i))));
                    nuevoProducto.setCategoriaString(categorias.get(i));
                }else{
                    Toast.makeText(getContext(), "Seleccione una categoria", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "Por favor seleccione una opcion", Toast.LENGTH_SHORT).show();
            }
        });
        // Iniciamos el adaptador con la lista de estados
        spinEstado.setAdapter(mViewModel.setAdapterEstados(getContext(), mViewModel.getEstadosList()));
        // Evento onItemSelected de los estados
        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!mViewModel.getEstadosList().get(i).equals("Seleccione una opcion")){
                    //                                                     Hasmap Estados                   claves estados         
                    nuevoProducto.setEstado_producto(Integer.parseInt(mViewModel.getClavesEstados().get(mViewModel.getEstadosList().get(i))));
                }else{
                    Toast.makeText(getContext(), "Seleecione un estado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "Seleccione un estado", Toast.LENGTH_SHORT).show();
            }
        });
        // Evento onclick que capturara los datos;
        guardarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validar todos los campos
                if((mViewModel.validarEditText(campoNmbre) && mViewModel.validarEditText(campoDescripcion)) &&
                        (mViewModel.validarEditText(campoDescripcion) && mViewModel.validarEditText(campoStock)) &&
                        (mViewModel.validarEditText(campoPrecio) && mViewModel.validarEditText(campoUnidad))){
                    nuevoProducto.setNombre_producto(campoNmbre.getText().toString());
                    nuevoProducto.setDescripcion_producto(campoDescripcion.getText().toString());
                    nuevoProducto.setStock(Double.parseDouble(campoStock.getText().toString()));
                    nuevoProducto.setPrecio(Double.parseDouble(campoPrecio.getText().toString()));
                    nuevoProducto.setUnidad_medida(campoUnidad.getText().toString());
                    Log.i("Dato Get", "Datos capturados : " + nuevoProducto.getNombre_producto() + "\n Estado"
                    + nuevoProducto.getEstado_producto());
                    if(!nuevoProducto.getCategoriaString().equals("")){
                        mViewModel.guardarProducto(getContext(), nuevoProducto);
                    }else{
                        Toast.makeText(getContext(), "Por favor complete el formulario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        
        // Boton que muestra el fragmento de lista de productos
        listarProducto.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_productoFragment_to_listarProductoFragment));
    }

    public void getCategorias(final Context context){
        categorias = new ArrayList<>();
        categorias.add("Seleccione una opcion");
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Toast.makeText(context, "Los datos enviados son incorrectos", Toast.LENGTH_SHORT).show();
                }else{
                    JSONArray datosRemotos = null;
                    JSONObject registro = null;
                    try{
                        // Inicializar HasMap
                        clavesCategorias = new HashMap<>();
                        datosRemotos = new JSONArray(response);
                        for (int i = 0; i < datosRemotos.length(); i++){
                            // Los datos traidos son son separados por fila de datos
                            registro = new JSONObject(datosRemotos.getString(i));
                            // Retornamos el nombre de la categoria en una lista
                            categorias.add(registro.getString("nom_categoria"));
                            // Guardamos el nombre y el valor correpodiente a la categoria
                            clavesCategorias.put(registro.getString("nom_categoria"), registro.getString("id_categoria"));
                        }
                        // Llenar arreglo con categorias
                        spinCategoria.setAdapter(mViewModel.setAdapterCategorias(context, categorias));
                    }catch (Exception ex){
                        Log.i("Response ", "returned exception " + ex);
                    }
                    Log.d("Response", "Listar retorno : " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Sin conexion a internet", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> paquete = new HashMap<>();
                paquete.put("Content-Type", "application/json; charset=utf-8");
                paquete.put("Accept", "application/json");
                paquete.put("opcion", "listar");
                return paquete;
            }
        };
        // Enviar peticion HTTP
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}