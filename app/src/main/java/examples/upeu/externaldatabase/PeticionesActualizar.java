package examples.upeu.externaldatabase;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

/**
 * Created by Kelvin on 31/10/2015.
 */
public class PeticionesActualizar extends AsyncTask<String,String,String> {
    URL URLCONNECTION ;
    String urlString="http://192.168.1.35/Service/index.php?opc=3";
    Activity activity;
    JSONObject jsonArrayLista;
    public PeticionesActualizar(Activity ctx){
        activity=ctx;

    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String id = params[0];
            String usuario = params[1];
            String clave = params[2];
            URLCONNECTION= new URL(urlString+"&id="+id+"&usuario="+usuario+"&clave="+clave);
            HttpURLConnection urlConnection = (HttpURLConnection) URLCONNECTION.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
             jsonArrayLista= new JSONObject(responseStrBuilder.toString());
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
        try {
            if (jsonArrayLista.getBoolean("value")){
                Toast.makeText(activity.getApplicationContext(),"Actualizo Correctamente!",Toast.LENGTH_SHORT).show();
                new PeticionesListar(activity).execute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        super.onPostExecute(s);
    }


}
