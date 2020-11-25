package com.cortezhac.login.ui.producto;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
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

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateDeleteViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    // Link del servidor
    SentingURI Setings = new SentingURI();
    private final String URLEliminar = Setings.IP1 +"api/producto/eliminarProducto.php";
    private final String URLActualizar = SentingURI.IP1 + "api/producto/actualizarProducto.php";
    public UpdateDeleteViewModel(){

    }

    public boolean validarEditText(EditText editText){
        boolean condicion = true;
        if(!(editText.getText().toString().length() > 0)){
            editText.setError("Campo obligatorio");
            condicion = false;
        }
        return condicion;
    }

    public ArrayList<String> getEstados (){
        ArrayList<String> estados = new ArrayList<>();
        estados.add("Seleccione un estado");
        estados.add("Inactivo");
        estados.add("Activo");
        return estados;
    }

    public HashMap<String,String> getClavesEstados(){
        HashMap<String, String> claves = new HashMap<>();
        claves.put("Inactivo", "0");
        claves.put("Activo", "1");
        return claves;
    }

    public ArrayAdapter getEstadosAdapter(Context context){
        return new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, getEstados());
    }

    public ArrayAdapter getCategoriaAdapter(Context context , ArrayList<String> categorias){
        return new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, categorias);
    }

    public void actualizaDatosRemotos(final Context context, final tb_producto producto, final FragmentActivity activity){
        StringRequest request = new StringRequest(Request.Method.POST, this.URLActualizar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Toast.makeText(context, "Los valores enviados son incorrectos", Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject datos = null;
                    try{
                        datos = new JSONObject(response);
                        String respuesta = datos.getString("estado");
                        if(respuesta.equals("1")){
                            Toast.makeText(context, "Datos Actualizados", Toast.LENGTH_SHORT).show();
                            if(activity != null){
                                activity.onBackPressed();
                            }
                        }else{
                            Toast.makeText(context, "Error :(", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception ex){
                        Log.e("RESPONSE", "Respuesta " + ex + "response " + response);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sin conexion", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> parametros = new HashMap<>();
                parametros.put("Content-Type", "application/json; charset=utf-8");
                parametros.put("Accept", "application/json");
                parametros.put("id", String.valueOf(producto.getId_producto()));
                parametros.put("nombre", producto.getNombre_producto());
                parametros.put("descripcion", producto.getDescripcion_producto());
                parametros.put("stock", String.valueOf(producto.getStock()));
                parametros.put("precio", String.valueOf(producto.getPrecio()));
                parametros.put("unidad", String.valueOf(producto.getUnidad_medida()));
                parametros.put("estado", String.valueOf(producto.getEstado_producto()));
                parametros.put("categoria", String.valueOf(producto.getCategoria()));
                return parametros;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void eliminarDatosRemotos(final Context context, final String id,final FragmentActivity activity){
        StringRequest request = new StringRequest(Request.Method.POST, this.URLEliminar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Toast.makeText(context, "Los valores enviados son incorrectos", Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject datos = null;
                    try{
                        datos = new JSONObject(response);
                        String respuesta = datos.getString("estado");
                        if(respuesta.equals("0")){
                            Toast.makeText(context, "Datos Eliminados", Toast.LENGTH_SHORT).show();
                            if(activity != null){
                                activity.onBackPressed();
                            }
                        }else{
                            Toast.makeText(context, "Error :(", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception ex){
                        Log.e("RESPONSE", "Respuesta " + ex + "response " + response);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sin conexion", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> parametros = new HashMap<>();
                parametros.put("Content-Type", "application/json; charset=utf-8");
                parametros.put("Accept", "application/json");
                parametros.put("id", id);
                return parametros;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}