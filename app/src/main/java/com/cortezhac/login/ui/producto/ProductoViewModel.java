package com.cortezhac.login.ui.producto;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

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

public class ProductoViewModel extends ViewModel {
    SentingURI Setings = new SentingURI();
    private final String URL = Setings.IP1 +"api/producto/guardarProducto.php";
    private ArrayList<String> categorias;
    private ArrayAdapter adapter;
    private String valorSeleccionado = "";

    // TODO: Implement the ViewModel
    public ProductoViewModel(){

    }

    public ArrayAdapter setAdapterCategorias(Context context, ArrayList<String> categorias){
        return new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, categorias);
    }

    public ArrayAdapter setAdapterEstados(Context context, ArrayList<String> estados){
        return new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, estados);
    }

    public ArrayList<String> getEstadosList(){
        ArrayList<String> estados = new ArrayList<>();
        estados.add("Seleccione una opcion");
        estados.add("Activo");
        estados.add("Inactivo");
        return estados;
    }

    public HashMap<String, String> getClavesEstados(){
        HashMap<String, String> claves = new HashMap<>();
        claves.put("Activo", "1");
        claves.put("Inactivo", "0");
        return  claves;
    }

    public boolean validarEditText(EditText editText){
        boolean condicion = true;
        if(!(editText.getText().toString().length() > 0)){
            editText.setError("Campo obligatorio");
            condicion = false;
        }
        return condicion;
    }

    public void guardarProducto(final Context context, final tb_producto nuevoProducto){
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Toast.makeText(context, "Los datos enviados", Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject respuesta;
                    try{
                        respuesta = new JSONObject(response);
                        if(respuesta.getString("estado").equals("0")){
                            Toast.makeText(context, "El producto se guardo con exito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Ocurrio un error :(", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception ex){
                        Log.i("Response" , "Returned: " + ex);
                    }
                }
                Log.d("Response", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
                Log.i("Error Response", "Returned: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> producto = new HashMap<>();
                producto.put("Content-Type", "application/json; charset=utf-8");
                producto.put("Accept", "application/json");
                producto.put("nombre", nuevoProducto.getNombre_producto());
                producto.put("descripcion", nuevoProducto.getDescripcion_producto());
                producto.put("stock", String.valueOf(nuevoProducto.getStock()));
                producto.put("precio", String.valueOf(nuevoProducto.getPrecio()));
                producto.put("unidad", nuevoProducto.getUnidad_medida());
                producto.put("estado", String.valueOf(nuevoProducto.getEstado_producto()));
                producto.put("categoria", String.valueOf(nuevoProducto.getCategoria()));
                return  producto;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    /*
    public void getCategorias(final Context context , Spinner spinCategoria, final HashMap<String,String> claves){
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Toast.makeText(context, "Los parametros enviados son incorrectos", Toast.LENGTH_SHORT).show();
                }else {
                    JSONArray datosRemotos = null;
                    JSONObject registro = null;
                    try {
                        // Inicializar Mapa de claves y ArrayList
                        categorias = new ArrayList<>();
                        datosRemotos = new JSONArray(response);
                        Log.d("RESPONSE ", "JSON Recibido : "+ datosRemotos + "\nTamaño " + datosRemotos.length());
                        for (int i = 0; i < datosRemotos.length(); i++) {
                            // JsonArray trae toda la fila de registros por lo que se tiene que guardar en un JSONObject para acceder a las llaves
                            registro = new JSONObject(datosRemotos.getString(i));
                            Log.d("RESPONSE", "JSON REGISTRO : " + registro.get("nom_categoria"));// Resultado consola
                            categorias.add(registro.getString("nom_categoria"));
                            claves.put(registro.getString("nom_categoria"), registro.getString("id_categoria"));
                        }

                    }catch (Exception ex){
                        Log.d("RESPONSE ", "getAdapterCategorias returned : " + ex);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sin conxecion a internet", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> datosEnviados = new HashMap<>();
                datosEnviados.put("Content-Type", "application/json; charset=utf-8");
                datosEnviados.put("Accept", "application/json");
                datosEnviados.put("opcion", "listar");
                return datosEnviados;
            }
        };
        // Enviar peticion HTTP
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
    */
}