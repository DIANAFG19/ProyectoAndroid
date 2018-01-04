package diana.org.proyectoandroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {
    String result ;
    String id = "5a2f10dbffd76a000427b189";
    String resul = "nada";
    Mensajito mensa = new Mensajito();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buscarID = (Button)findViewById(R.id.buscarID);
        Button insertarBD = (Button)findViewById(R.id.insertarBD);
        Button mostrar = (Button)findViewById(R.id.mostrar);

        buscarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idv = (EditText) findViewById(R.id.ingresaID);
                if (idv.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"lo siento pero debes ingresar un ID", Toast.LENGTH_LONG).show();
                }else {
                    id = idv.getText().toString();
                    BuscarIdMensajitos busqueda= new BuscarIdMensajitos();
                    busqueda.execute(null,null,null);
                    idv.setText("");

                }
            }
        });

        insertarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText cuerpov = (EditText) findViewById(R.id.ingresaCuerpo);
                EditText titulov = (EditText) findViewById(R.id.ingresaTitulo);
                if (cuerpov.getText().toString().equals("")||titulov.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"lo siento pero debes ingresar un titulo y un cuerpo", Toast.LENGTH_LONG).show();
                }else {
                    mensa.setTitulo(titulov.getText().toString());
                    mensa.setCuerpo(cuerpov.getText().toString());
                    InsertarMensajito inserta= new InsertarMensajito();
                    inserta.execute(null,null,null);
                    cuerpov.setText("");
                    titulov.setText("");
                }
            }
        });

        mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaMensajitos tarea = new TareaMensajitos();
                tarea.execute(null,null,null);
            }
        });
    }

    public class TareaMensajitos extends AsyncTask<Integer, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Integer... params) {
            // The connection URL
            String url = "https://df-monguito.herokuapp.com/api/mensajito";

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Add the String message converter
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            // Make the HTTP GET request, marshaling the response to a String
            result = restTemplate.getForObject(url, String.class, "Android");

            return null;
        }
    }


    public class BuscarIdMensajitos extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"Busqueda:\n "+ result, Toast.LENGTH_LONG).show();
            // Toast.makeText(getApplicationContext(), usuario, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            // The connection URL
            String url = "https://df-monguito.herokuapp.com/api/mensajito/"+id;

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Add the String message converter
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            // Make the HTTP GET request, marshaling the response to a String
            result = restTemplate.getForObject(url, String.class, "Noseparaquesirva");
            return null;

        }
    }


    public class InsertarMensajito extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"ESTATUS DEL SERVICIO:\n ¡se inserto correctamente! \nTitulo: "+mensa.getTitulo().toString()
                    +"\nCuerpo: "+mensa.getCuerpo().toString(), Toast.LENGTH_LONG).show();
        }
        @Override
        protected String doInBackground(String... string) {

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());


            String resultado = restTemplate.postForObject("https://df-monguito.herokuapp.com/api/mensajito", mensa, String.class);

            ObjectMapper mapper=new ObjectMapper();
            try {
                Estatus estatus=mapper.readValue(resultado, Estatus.class);
                System.out.println("ESTATUS DEL SERVICIO: "+estatus.isSuccess());
            } catch (Exception e) {
                System.out.println("ERROR "+e.getMessage());
                Toast.makeText(getApplicationContext(),"ESTATUS DEL SERVICIO: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }

}