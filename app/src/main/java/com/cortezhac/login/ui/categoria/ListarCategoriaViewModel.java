package com.cortezhac.login.ui.categoria;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
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
import com.cortezhac.login.data.volley.MySingleton;
import com.cortezhac.login.ui.adapter.CategoriaAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListarCategoriaViewModel extends ViewModel {
    SentingURI Setings = new SentingURI();
    private final String URL = "http://"+Setings.IP1+"/serviceAPI/api/categorias/consultarCategorias.php";
    //private final String URL = "http://192.168.8.100/serviceAPI/api/categorias/consultarCategorias.php";

    // TODO: Implement the ViewModel

    public ListarCategoriaViewModel(){

    }

    public void getListaCategoriasRemoto(final Context context , final RecyclerView listaCategorias , final Fragment fragment){
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
                        final ArrayList<tb_categoria> listaArticulos = new ArrayList<>();

                        for(int i = 0; i< datosObtenidos.length(); i++){
                            tb_categoria datoCategoria = new tb_categoria();
                            // Obtiene el primer conjunto del bloque String
                            registro = new JSONObject(datosObtenidos.getString(i));
                            datoCategoria.setId(registro.getInt("id_categoria"));
                            datoCategoria.setNombre(registro.getString("nom_categoria"));
                            datoCategoria.setEstado(registro.getInt("estado_categoria"));
                            datoCategoria.setEstadoString(datoCategoria.getEstado() == 1 ? "Activo" : "Inactivo");
                            listaArticulos.add(datoCategoria);
                            Log.d("RESPONSE","REGISTRO JSON " + registro.get("nom_categoria"));
                        }
                        listaCategorias.setLayoutManager(new LinearLayoutManager(context));
                        // Prepara adaptador
                        CategoriaAdapter adapter = new CategoriaAdapter(listaArticulos);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(context, "Click en " + listaArticulos.get(listaCategorias.getChildAdapterPosition(view)).getNombre(), Toast.LENGTH_SHORT).show();
                                // Datos para enviar
                                Bundle datos = new Bundle();
                                datos.putInt("id", listaArticulos.get(listaCategorias.getChildAdapterPosition(view)).getId());
                                datos.putString("nombre", listaArticulos.get(listaCategorias.getChildAdapterPosition(view)).getNombre());
                                datos.putInt("estado", listaArticulos.get(listaCategorias.getChildAdapterPosition(view)).getEstado());

                                NavHostFragment.findNavController(fragment).navigate(R.id.action_listarCategoriaFragment_to_updateDeleteFragment,datos);
                            }
                        });
                        listaCategorias.setAdapter(adapter);

                        
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