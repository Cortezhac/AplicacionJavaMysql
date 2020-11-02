package com.cortezhac.login.ui.categoria;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cortezhac.login.data.model.SentingURI;
import com.cortezhac.login.data.volley.MySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateDeleteViewModel extends ViewModel {
    SentingURI Setings = new SentingURI();
    // TODO: Implement the ViewModel
    private final String URLUP = "http://"+ Setings.IP1+"/serviceAPI/api/categorias/actualizarCategorias.php";
    private final String URLDEL = "http://192.168.43.237/serviceAPI/api/categorias/eliminarCategorias.php";
    
    public UpdateDeleteViewModel(){

    }

    public ArrayList<String> getEstados(){
        ArrayList<String> datos = new ArrayList<>();
        datos.add("Inactivo");
        datos.add("Activo");
        return datos;
    }

    public void actualizaDatosRemotos(final Context context, final String id, final String nombre, final String estado){
        StringRequest request = new StringRequest(Request.Method.POST, this.URLUP, new Response.Listener<String>() {
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
                parametros.put("nombre", nombre);
                parametros.put("estado", estado);
                return parametros;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }


    public void eliminarDatosRemotos(final Context context, final String id){
        StringRequest request = new StringRequest(Request.Method.POST, this.URLDEL, new Response.Listener<String>() {
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