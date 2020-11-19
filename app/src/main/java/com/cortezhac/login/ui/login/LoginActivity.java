package com.cortezhac.login.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cortezhac.login.NavigationDrawerActivity;
import com.cortezhac.login.R;
import com.cortezhac.login.data.model.SentingURI;
import com.cortezhac.login.data.volley.MySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    SentingURI setings = new SentingURI();
    private final String URL = setings.IP1+"api/usuario/consultaUsuario.php";
    private EditText nombreUsuario, clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nombreUsuario = findViewById(R.id.editUsuario);
        clave = findViewById(R.id.editClave);

    }

    public void Logear(View v){
        if(comprobar(nombreUsuario) && comprobar(clave)){
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                // Peticcion HTTP exitosa
                @Override
                public void onResponse(String response) {
                    // 0 = sin datos
                    if(response.equals("0")){
                        Toast.makeText(getApplicationContext(), "No se encontro ningun dato", Toast.LENGTH_SHORT).show();
                    }else{
                        JSONObject datosRemotos = null;
                        try{
                            datosRemotos = new JSONObject(response);
                            // las claves usadas para obtener los datos son las mismas de la base de datos
                            datosRemotos.getString("nombre");
                            datosRemotos.getString("apellidos");
                            if(datosRemotos.getString("nombre").length() > 0 && datosRemotos.getString("apellidos").length() > 0){
                                Intent datos = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                                startActivity(datos);
                            }
                            Log.i("remote", "Datos personas momnbre" + datosRemotos.getString("nombre"));
                            Log.i("remote", "Datos personas apellido" + datosRemotos.getString("apellido"));
                        }catch (Exception ex){
                            Log.d("RESPONSE ", "Home.onResponse() returned: " + ex);
                        }
                    }
                    Log.d("RESPONSE 1", "onResponse() "+ URL +" retunend: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Sin conexion a internet", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }){
                @Override
                protected Map<String,String> getParams()throws AuthFailureError {
                    Map<String, String> parametros =  new HashMap<>();
                    // En principio los datos se tiene que pasar en la funcion y guardarlos aqui para mandar la peticion
                    parametros.put("Content-Type", "application/json; charset=utf-8");
                    parametros.put("Accept", "application/json");
                    parametros.put("nombre", nombreUsuario.getText().toString());
                    parametros.put("clave", clave.getText().toString());
                    return parametros;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }else{
            nombreUsuario.setError("Obligatorio");
            clave.setError("Obligatorio");
        }

    }


    public boolean comprobar(EditText campo){
        if(campo.getText().toString().length() > 0){
            return true;
        }
        return false;
    }
}