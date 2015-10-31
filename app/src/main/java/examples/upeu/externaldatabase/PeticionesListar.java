package examples.upeu.externaldatabase;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Kelvin on 31/10/2015.
 */
public class PeticionesListar extends AsyncTask<String,String,String> {
    URL URLCONNECTION ;
    String urlString="http://192.168.1.35/Service/index.php?opc=1";
    Activity activity;
    JSONArray jsonArrayLista;
    public PeticionesListar(Activity ctx){
        activity=ctx;

    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URLCONNECTION= new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) URLCONNECTION.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
             jsonArrayLista= new JSONArray(responseStrBuilder.toString());
            Log.d("CONNECTEDSERVICE",jsonArrayLista.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("CONNECTEDSERVICE", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("CONNECTEDSERVICE", e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("CONNECTEDSERVICE", e.getMessage());
        }
        return null;
    }



    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(activity.getApplicationContext(), "Listando...", Toast.LENGTH_SHORT).show();
       ArrayList<String> jsonObj = new ArrayList<>();
        for (int i = 0; i < jsonArrayLista.length(); i++) {
            try {
                jsonObj.add(jsonArrayLista.getJSONObject(i).toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        ArrayAdapterCustom arrayAdapterCustom = new ArrayAdapterCustom(activity.getApplicationContext(),0,jsonObj);
        ListView listView = (ListView)activity.findViewById(R.id.listView);
        listView.setAdapter(arrayAdapterCustom);
        super.onPostExecute(s);
    }
}
