package com.cortezhac.login.ui.producto;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
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

public class UpdateDeleteFragment extends Fragment {
    private EditText nombre, descripcion, stock, precio, unidad;
    private Spinner estado, categoria;
    private Button actualizar, eliminar;
    private tb_producto producto = new tb_producto();

    private ArrayList<String> categorias; // Contendra los Strings del espinner
    private HashMap<String, String> clavesCategorias;// Contendra las claves String y su valor correspondiente
    // Link del servidor
    SentingURI Setings = new SentingURI();
    private final String URL = Setings.IP1 +"api/categorias/consultarCategorias.php";

    private UpdateDeleteViewModel mViewModel;

    public static UpdateDeleteFragment newInstance() {
        return new UpdateDeleteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewfragment = inflater.inflate(R.layout.update_delete_fragment2, container, false);
        nombre = viewfragment.findViewById(R.id.editNombreProductoUPDEL);
        descripcion = viewfragment.findViewById(R.id.editDescripUPDEL);
        stock = viewfragment.findViewById(R.id.editStockUPDEL);
        precio = viewfragment.findViewById(R.id.editPrecioUPDEL);
        unidad = viewfragment.findViewById(R.id.editUnidadUPDEL);
        estado = viewfragment.findViewById(R.id.spinnerEstadoUPDEL);
        categoria = viewfragment.findViewById(R.id.spinnerCategoriaUPDEL);
        actualizar = viewfragment.findViewById(R.id.btnActualizarProducto);
        eliminar = viewfragment.findViewById(R.id.btnEliminarProducto);
        return viewfragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdateDeleteViewModel.class);
        // TODO: Use the ViewModel
        assert getArguments() != null;
        producto.setId_producto(Integer.parseInt(getArguments().getString("id")));
        producto.setNombre_producto(getArguments().getString("nombre"));
        producto.setDescripcion_producto(getArguments().getString("descrip"));
        producto.setStock(Double.parseDouble(getArguments().getString("stock")));
        producto.setPrecio(Double.parseDouble(getArguments().getString("precio")));
        producto.setUnidad_medida(getArguments().getString("unidad"));
        producto.setCategoria(Integer.parseInt(getArguments().getString("categoria")));

        // Asignar los campos a la vista
        nombre.setText(producto.getNombre_producto());
        descripcion.setText(producto.getDescripcion_producto());
        stock.setText(String.valueOf(producto.getStock()));
        precio.setText(String.valueOf(producto.getPrecio()));
        unidad.setText(producto.getUnidad_medida());
        estado.setAdapter(mViewModel.getEstadosAdapter(getContext()));
        getCategorias(getContext());
        // Obtener estado
        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!mViewModel.getEstados().get(i).equals("Seleccione un estado")){
                    producto.setEstado_producto(Integer.parseInt(mViewModel.getClavesEstados().get(mViewModel.getEstados().get(i))));
                }else{
                    Toast.makeText(getContext(), "Por favor seleccione un opcion", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                producto.setEstado_producto(Integer.parseInt(getArguments().getString("estado")));
            }
        });
        // Obtener categoria
        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!categorias.get(i).equals("Seleccione una opcion")){
                    producto.setCategoria(Integer.parseInt(clavesCategorias.get(categorias.get(i))));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                producto.setCategoria(Integer.parseInt(getArguments().getString("categoria")));
            }
        });

        // Botones de accion
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((mViewModel.validarEditText(nombre) && mViewModel.validarEditText(descripcion)) &&
                        (mViewModel.validarEditText(stock) && mViewModel.validarEditText(precio)) ){
                    producto.setNombre_producto(nombre.getText().toString());
                    producto.setDescripcion_producto(descripcion.getText().toString());
                    producto.setStock(Double.parseDouble(stock.getText().toString()));
                    producto.setPrecio(Double.parseDouble(precio.getText().toString()));
                    producto.setUnidad_medida(unidad.getText().toString());
                    mViewModel.actualizaDatosRemotos(getContext(), producto, getActivity());
                }else{
                    Toast.makeText(getContext(), "Complete el formulario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.eliminarDatosRemotos(getContext(), String.valueOf(producto.getId_producto()), getActivity());
            }
        });
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
                        categoria.setAdapter(mViewModel.getCategoriaAdapter(getContext(), categorias));
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