package com.cortezhac.login.ui.producto;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.cortezhac.login.ui.adapter.ProductoAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListarProductoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListarProductoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView productosAdapter;
    private SentingURI settings = new SentingURI();
    private String URLListar = settings.IP1 + "api/producto/consultarProductos.php";
    
    //Lista de productos
    ArrayList<tb_producto> productosList;
    tb_producto producto;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListarProductoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListarProductoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListarProductoFragment newInstance(String param1, String param2) {
        ListarProductoFragment fragment = new ListarProductoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vistaFragment = inflater.inflate(R.layout.fragment_listar_producto, container, false);
        productosAdapter = vistaFragment.findViewById(R.id.listaProductosRecycler);
        return vistaFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Fragment fragment = this;
        StringRequest request = new StringRequest(Request.Method.POST, URLListar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Toast.makeText(getContext(), "Los datos enviados", Toast.LENGTH_SHORT).show();
                }else{
                    JSONArray lista;
                    JSONObject registro;
                    try{
                        productosList = new ArrayList<>();
                        lista = new JSONArray(response);
                        for(int i = 0; i < lista.length(); i++){
                            registro = new JSONObject(lista.getString(i));
                            producto = new tb_producto();
                            producto.setId_producto(registro.getInt("id_producto"));
                            producto.setNombre_producto(registro.getString("nom_producto"));
                            producto.setDescripcion_producto(registro.getString("des_producto"));
                            producto.setStock(registro.getInt("stock"));
                            producto.setPrecio(registro.getDouble("precio"));
                            producto.setUnidad_medida(registro.getString("unidad_medida"));
                            producto.setEstado_producto(registro.getInt("estado_producto"));
                            producto.setCategoria(registro.getInt("categoria"));
                            productosList.add(producto);
                            Log.d("RESPONSE", "onResponse: " + registro);
                        }
                        productosAdapter.setLayoutManager(new LinearLayoutManager(getContext()));
                        // Praparar adaptador
                        ProductoAdapter adapter = new ProductoAdapter(productosList);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(), "Click en " + productosList.get(productosAdapter.getChildAdapterPosition(view)).getNombre_producto(), Toast.LENGTH_SHORT).show();
                                // Datos para enviar
                                Bundle datos = new Bundle();
                                datos.putString("id", String.valueOf(productosList.get(productosAdapter.getChildAdapterPosition(view)).getId_producto()));
                                datos.putString("nombre", productosList.get(productosAdapter.getChildAdapterPosition(view)).getNombre_producto());
                                datos.putString("descrip", productosList.get(productosAdapter.getChildAdapterPosition(view)).getDescripcion_producto());
                                datos.putString("stock", String.valueOf(productosList.get(productosAdapter.getChildAdapterPosition(view)).getStock()));
                                datos.putString("precio", String.valueOf(productosList.get(productosAdapter.getChildAdapterPosition(view)).getPrecio()));
                                datos.putString("unidad", String.valueOf(productosList.get(productosAdapter.getChildAdapterPosition(view)).getUnidad_medida()));
                                datos.putString("estado", String.valueOf(productosList.get(productosAdapter.getChildAdapterPosition(view)).getEstado_producto()));
                                datos.putString("categoria", String.valueOf(productosList.get(productosAdapter.getChildAdapterPosition(view)).getCategoria()));

                                NavHostFragment.findNavController(fragment).navigate(R.id.action_listarProductoFragment_to_updateDeleteFragment2, datos);
                            }
                        });

                        productosAdapter.setAdapter(adapter);

                    }catch (Exception ex){
                        Log.i("Response" , "Returned: " + ex);
                    }
                }
                Log.d("Response", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Sin conexion a internet", Toast.LENGTH_SHORT).show();
                Log.i("Error Response", "Returned: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> producto = new HashMap<>();
                producto.put("Content-Type", "application/json; charset=utf-8");
                producto.put("Accept", "application/json");
                producto.put("opcion", "listar");
                return  producto;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void getProductos(){
        StringRequest request = new StringRequest(Request.Method.POST, URLListar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Toast.makeText(getContext(), "Los datos enviados", Toast.LENGTH_SHORT).show();
                }else{
                    JSONArray lista;
                    JSONObject registro;
                    try{
                        productosList = new ArrayList<>();
                        lista = new JSONArray(response);
                        for(int i = 0; i < lista.length(); i++){
                            registro = new JSONObject(lista.getString(i));
                            producto.setId_producto(registro.getInt("id_producto"));
                            producto.setNombre_producto(registro.getString("nom_producto"));
                            producto.setEstado_producto(registro.getInt("estado_producto"));
                            producto.setCategoria(registro.getInt("categoria"));
                            productosList.add(producto);
                            Log.d("RESPONSE", "onResponse: " + registro);
                        }
                        productosAdapter.setLayoutManager(new LinearLayoutManager(getContext()));
                        // Praparar adaptador
                        ProductoAdapter adapter = new ProductoAdapter(productosList);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(), "Click en " + productosList.get(productosAdapter.getChildAdapterPosition(view)).getNombre_producto(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        productosAdapter.setAdapter(adapter);

                    }catch (Exception ex){
                        Log.i("Response" , "Returned: " + ex);
                    }
                }
                Log.d("Response", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Sin conexion a internet", Toast.LENGTH_SHORT).show();
                Log.i("Error Response", "Returned: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> producto = new HashMap<>();
                producto.put("Content-Type", "application/json; charset=utf-8");
                producto.put("Accept", "application/json");
                producto.put("opcion", "listar");
                return  producto;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}