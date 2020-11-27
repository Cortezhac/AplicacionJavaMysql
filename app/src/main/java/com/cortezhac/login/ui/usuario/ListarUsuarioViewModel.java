package com.cortezhac.login.ui.usuario;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cortezhac.login.R;
import com.cortezhac.login.data.model.SentingURI;
import com.cortezhac.login.data.model.tb_categoria;
import com.cortezhac.login.data.model.tb_usuario;
import com.cortezhac.login.data.volley.MySingleton;
import com.cortezhac.login.ui.adapter.CategoriaAdapter;
import com.cortezhac.login.ui.adapter.UsuarioAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListarUsuarioViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    SentingURI Setings = new SentingURI();
    private final String URL = Setings.IP1+"api/usuario/consultaUsuarios.php";

    public ListarUsuarioViewModel() {
    }

    public void getListaUsuariosRemoto(final Context context , final RecyclerView listausuarios , final Fragment fragment){
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Toast.makeText(context, "Los valores enviados son incorrectos", Toast.LENGTH_SHORT).show();
                }else{
                    JSONArray datosObtenidos = null;
                    JSONObject registro = null;
                    try{
                        datosObtenidos = new JSONArray(response);
                        final ArrayList<tb_usuario> listaArticulos = new ArrayList<>();

                        for(int i = 0; i< datosObtenidos.length(); i++){
                            tb_usuario datoCategoria = new tb_usuario();
                            // Obtiene el primer conjunto del bloque String
                            registro = new JSONObject(datosObtenidos.getString(i));
                            datoCategoria.setId(registro.getInt("id"));
                            datoCategoria.setNombre(registro.getString("nombre"));
                            datoCategoria.setApellido(registro.getString("apellidos"));
                            datoCategoria.setUsuario(registro.getString("usuario"));
                            datoCategoria.setCorreo(registro.getString("correo"));
                            datoCategoria.setClave(registro.getString("clave"));
                            datoCategoria.setTipo(registro.getInt("tipo"));
                            datoCategoria.setEstado(registro.getInt("estado"));
                            datoCategoria.setPregunta(registro.getString("pregunta"));
                            datoCategoria.setRespuesta(registro.getString("respuesta"));
                            datoCategoria.setEstadoString(datoCategoria.getEstado() == 1 ? "Activo" : "Inactivo");
                            listaArticulos.add(datoCategoria);
                            Log.d("RESPONSE","REGISTRO JSON " + registro.get("nombre"));
                        }
                        listausuarios.setLayoutManager(new LinearLayoutManager(context));
                        // Prepara adaptador
                        UsuarioAdapter adapter = new UsuarioAdapter(listaArticulos);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(context, "Selecciono el usuario: " + listaArticulos.get(listausuarios.getChildAdapterPosition(view)).getNombre(), Toast.LENGTH_SHORT).show();
                                // Datos para enviar
                                Bundle datos = new Bundle();
                                datos.putInt("id", listaArticulos.get(listausuarios.getChildAdapterPosition(view)).getId());
                                datos.putString("nombre", listaArticulos.get(listausuarios.getChildAdapterPosition(view)).getNombre());
                                datos.putString("apellidos", listaArticulos.get(listausuarios.getChildAdapterPosition(view)).getApellido());
                                datos.putString("correo", listaArticulos.get(listausuarios.getChildAdapterPosition(view)).getCorreo());
                                datos.putString("usuario", listaArticulos.get(listausuarios.getChildAdapterPosition(view)).getUsuario());
                                datos.putString("clave", listaArticulos.get(listausuarios.getChildAdapterPosition(view)).getClave());
                                datos.putInt("tipo", listaArticulos.get(listausuarios.getChildAdapterPosition(view)).getTipo());
                                datos.putInt("estado", listaArticulos.get(listausuarios.getChildAdapterPosition(view)).getEstado());
                                datos.putString("pregunta", listaArticulos.get(listausuarios.getChildAdapterPosition(view)).getPregunta());
                                datos.putString("respuesta", listaArticulos.get(listausuarios.getChildAdapterPosition(view)).getRespuesta());

                                NavHostFragment.findNavController(fragment).navigate(R.id.action_listarUsuarioFragment_to_updateDeleteFragment, datos);
                            }
                        });
                        listausuarios.setAdapter(adapter);


                    }catch (Exception ex){
                        Log.d("RESPONSE ", "Usuario.onResponse() returned: " + ex);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("Content-Type", "application/json; charset=utf-8");
                parametros.put("Accept", "application/json");
                parametros.put("opcion", "listar");
                return parametros;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}