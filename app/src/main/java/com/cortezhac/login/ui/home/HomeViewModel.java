package com.cortezhac.login.ui.home;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cortezhac.login.data.model.SentingURI;
import com.cortezhac.login.data.model.tb_usuario;
import com.cortezhac.login.data.volley.AppSingleton;
import com.cortezhac.login.data.volley.MySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HomeViewModel extends ViewModel {
    SentingURI setings = new SentingURI();
    private tb_usuario persona = new tb_usuario();
    private final String URLWIFI = "http://192.168.43.237/serviceAPI/api/usuario/consultaUsuario.php";
    private final String URL = "http://"+setings+"/serviceAPI/api/usuario/consultaUsuario.php";
    private MutableLiveData<String> mText;
    // texto muted(color gris) desde java
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Datos de base de datos remota");
    }
    // retorna el  valor muted con string
    public LiveData<String> getText() {
        return mText;
    }

    public void getDatosRemotos(final Context context , final TextView nombre, final TextView apellido){
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            // Peticcion HTTP exitosa
            @Override
            public void onResponse(String response) {
                // 0 = sin datos
                if(response.equals("0")){
                    Toast.makeText(context, "No se encontro ningun dato", Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject datosRemotos = null;
                    try{
                        datosRemotos = new JSONObject(response);
                        // las claves usadas para obtener los datos son las mismas de la base de datos
                        persona.setNombre(datosRemotos.getString("nombre"));
                        persona.setApellido(datosRemotos.getString("apellidos"));
                        nombre.setText(persona.getNombre());
                        apellido.setText(persona.getApellido());
                        Log.i("remote", "Datos personas momnbre" + datosRemotos.getString("nombre"));
                        Log.i("remote", "Datos personas apellido" + datosRemotos.getString("apellido"));
                    }catch (Exception ex){
                        Log.d("RESPONSE ", "Home.onResponse() returned: " + ex);
                        persona.setNombre("sin datos");
                        persona.setApellido("sin datos");
                    }
                }

                Log.d("RESPONSE 1", "onResponse() retunend: " + persona.getNombre());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError{
                Map<String, String> parametros =  new HashMap<>();
                // En principio los datos se tiene que pasar en la funcion y guardarlos aqui para mandar la peticion
                parametros.put("Content-Type", "application/json; charset=utf-8");
                parametros.put("Accept", "application/json");
                parametros.put("id",String.valueOf(1));
                return parametros;
            }
        };

        // Agrega la peticion a la Queue
        MySingleton.getInstance(context).addToRequestQueue(request);
        Toast.makeText(context, "Traer datos ejecutada", Toast.LENGTH_SHORT).show();
    }


}