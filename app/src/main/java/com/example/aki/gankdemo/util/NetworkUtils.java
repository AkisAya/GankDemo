package com.example.aki.gankdemo.util;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aki on 2016/9/24.
 */

public class NetworkUtils {

    public interface DataCallbackListener {
        void onResponse (JSONObject response);
        void onError (Exception e);

    }

    private Context mContext;
    private DataCallbackListener mDataCallbackListener;


    public NetworkUtils(Context Context) {
        mContext = Context;
    }

    // fetchData JsonData from volley
    public static void FetchDataFromUrl (final Context context, String urlString, final DataCallbackListener callbackListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if (callbackListener != null) {
                    callbackListener.onResponse(response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callbackListener != null) {
                    callbackListener.onError(error);
                }

            }
        });

        queue.add(jsonObjectRequest);
    }

    // fetch byte[]
    public static byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in  = connection.getInputStream();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() + ": with" + urlSpec);
            }

            int length = 0;
            byte[] buffer = new byte[1024];
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

}
