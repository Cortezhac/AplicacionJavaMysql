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
import com.cortezhac.login.data.model.tb_categoria;
import com.cortezhac.login.data.volley.MySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CategoriaViewModel extends ViewModel {
    SentingURI Setings = new SentingURI();
    tb_categoria categoria = new tb_categoria();
    private final String URL = Setings.IP1+"api/categorias/guardarCategoria.php";
    
    public CategoriaViewModel(){}

    public HashMap<String, String> getSpinnerData(){
        HashMap<String, String> data = new HashMap<>();
        data.put("Activo","1");
        data.put("Inactivo", "0");
        return data;
    }

    public String[] getDatoSpinner(){
        String[] estado = {"Activo", "Inactivo"};
        return estado;
    }

    public void guardarDatosRemotos(final Context context, final EditText nombreCategoria, final String estado){
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Toast.makeText(context, "Los valores enviados son incorrectos", Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject datosRemotos = null;
                    try{
                        datosRemotos = new JSONObject(response);
                        String respuesta = datosRemotos.getString("estado");
                        if(respuesta.equals("0")){
                            Log.d("RESPONSE", "response value : " + response);
                            Toast.makeText(context, "Datos Guardados con exito", Toast.LENGTH_LONG).show();
                        }else{
                            Log.d("RESPONSE", "response value : " + response);
                            Toast.makeText(context, "Error :(", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception ex){
                        Log.d("RESPONSE ", "Categoria.onResponse() returned: " + ex);
                        Log.d("RESPONSE", "response value : " + response);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sin conexion a internet", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String, String> parametros =  new HashMap<>();
                // En principio los datos se tiene que pasar en la funcion y guardarlos aqui para mandar la peticion
                parametros.put("Content-Type", "application/json; charset=utf-8");
                parametros.put("Accept", "application/json");
                parametros.put("nom_categoria", nombreCategoria.getText().toString());
                parametros.put("estado_categoria", estado);
                return parametros;
            }
        };
        // Ejecutar la peticion
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}