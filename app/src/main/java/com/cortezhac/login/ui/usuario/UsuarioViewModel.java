package com.cortezhac.login.ui.usuario;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cortezhac.login.R;
import com.cortezhac.login.data.model.SentingURI;
import com.cortezhac.login.data.volley.MySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsuarioViewModel extends ViewModel {
    SentingURI Setings = new SentingURI();
    private final String URL = Setings.IP1 +"api/usuario/guardarUsuario.php";
    private ArrayList<String> categorias;
    private ArrayAdapter adapter;
    private String valorSeleccionado = "";

    public UsuarioViewModel() {
    }


    public String[] getEstado(){
        String[] estado0 = {"Seleccione una opcion", "Activo", "Inactivo"};
        return estado0;
    }

    public String[] getTipo(){
        String[] estado1 = {"Seleccione una opcion", "Administrador", "Usuario", "Gerente" };
        return estado1;
    }

    public String[] getPregunta(){
        String[] estado2 = {"Seleccione una opcion", "¿Cual es nombre de tu mamá?", "¿Nombre de tu primera escuela?", "¿Nombre de tu primera mascota?" };
        return estado2;
    }

    public HashMap<String, String> getClavesEstados(){
        HashMap<String, String> claves0 = new HashMap<>();
        claves0.put("Activo", "1");
        claves0.put("Inactivo", "2");
        return  claves0;
    }

    public HashMap<String, String> getTipos(){
        HashMap<String, String> claves1 = new HashMap<>();
        claves1.put("Usuario", "2");
        claves1.put("Gerente", "3");
        claves1.put("Administrador", "1");
        return  claves1;
    }

    public HashMap<String, String> getPreguntas(){
        HashMap<String, String> claves2 = new HashMap<>();
        claves2.put("¿Cual es nombre de tu mamá?", "1");
        claves2.put("¿Nombre de tu primera escuela?", "2");
        claves2.put("¿Nombre de tu primera mascota?", "3");
        return  claves2;
    }

    public void guardarDatosRemotos(final Context context, final EditText nombre, final EditText apellido, final EditText correo,
                                    final EditText user, final EditText clave, final String tipo,  final String estado, final String pregunta, final EditText respuesta){
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
                parametros.put("nombre", nombre.getText().toString());
                parametros.put("apellidos", apellido.getText().toString());
                parametros.put("correo", correo.getText().toString());
                parametros.put("usuario", user.getText().toString());
                parametros.put("clave", clave.getText().toString());
                parametros.put("tipo", tipo);
                parametros.put("estado", estado);
                parametros.put("pregunta", pregunta);
                parametros.put("respuesta", respuesta.getText().toString());
                return parametros;
            }
        };
        // Ejecutar la peticion
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

}