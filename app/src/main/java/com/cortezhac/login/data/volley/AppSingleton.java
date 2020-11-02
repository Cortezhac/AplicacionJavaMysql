package com.cortezhac.login.data.volley;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

public class AppSingleton extends Application {
    public static final String TAG = AppSingleton.class.getSimpleName();
    private RequestQueue requestQueue;
    private static AppSingleton instancia;

    @Override
    public void onCreate(){
        super.onCreate();
        instancia = this; // instancia estatica de ella misma
    }

    public static synchronized  AppSingleton getInstance(){
        return instancia;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return  requestQueue;
    }

    public <I> void addToRequestQueue(Request<I> req, String tag){
        //Asigan un valor a tag si esta vacio
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <I> void addToRequestQueue(Request<I> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequest(Object tag){
        if(requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }

}
