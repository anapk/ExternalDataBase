package examples.upeu.externaldatabase;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;
        listView = (ListView) findViewById(R.id.listView);
        listarUsuarios();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
    }

    private void listarUsuarios() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Operaciones http
            new PeticionesListar(this).execute();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String s = (String) parent.getAdapter().getItem(position);
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        actualizarUsuario(new String[]{jsonObject.getString("id"), jsonObject.getString("usuario"), jsonObject.getString("clave")});
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("DATACLICK", s);

                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String s = (String) parent.getAdapter().getItem(position);
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        eliminarUsuario(jsonObject.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("DATACLICK", s);
                    return false;
                }
            });

        } else {
            // Mostrar errores
        }

    }

    private void actualizarUsuario(final String[] data) {
        final View layout_insert = View.inflate(this, R.layout.layout_add, null);
        final EditText editTextUsuario = (EditText) layout_insert.findViewById(R.id.editUsuario);
        final EditText editTextClave = (EditText) layout_insert.findViewById(R.id.editClave);
        editTextUsuario.setText(data[1]);
        editTextClave.setText(data[2]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar");
        builder.setView(layout_insert);
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sUsuario = editTextUsuario.getText().toString();
                String sClave = editTextClave.getText().toString();
                Log.d("DATOS", sUsuario + "-" + sClave);
                new PeticionesActualizar(activity).execute(data[0], sUsuario, sClave);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void registrarUsuario() {
        final View layout_insert = View.inflate(this, R.layout.layout_add, null);
        final EditText editTextUsuario = (EditText) layout_insert.findViewById(R.id.editUsuario);
        final EditText editTextClave = (EditText) layout_insert.findViewById(R.id.editClave);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registrar");
        builder.setView(layout_insert);
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sUsuario = editTextUsuario.getText().toString();
                String sClave = editTextClave.getText().toString();
                Log.d("DATOS", sUsuario + "-" + sClave);
                new PeticionesRegistrar(activity).execute(sUsuario, sClave);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void eliminarUsuario(final String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Esta Seguro de Eliminar?");
        builder.setPositiveButton("Seguro", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new PeticionesEliminar(activity).execute(id);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


}
