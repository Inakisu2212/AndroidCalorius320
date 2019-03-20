package com.example.calorius;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;


/**
 * A simple {@link Fragment} subclass.
 */
@TargetApi(Build.VERSION_CODES.N)
public class regCalFragment extends Fragment {

    private Spinner dropdownAl;
    private Spinner dropdownCom;
    private CalendarView calendar;
    private String fechaSeleccionada;
    private String correoLog = "a";
    private Spinner dropdownCant;

    //Estos son params que damos a AsyncTask
    private String nombreAlSel;
    private String fechaAlSel;
    private String tipoComidaSel;
    private String codigoAlSel;
    private String cantidadAlSel;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public regCalFragment() {
        // Required empty public constructor
    }

    //    public void ponerCorreo(String correo){
//        correoLog = correo;
//    }
    public void pasarInfo(String email){

        String Email = email;
    }

    @Override
    @TargetApi(android.os.Build.VERSION_CODES.N)
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reg_cal, container, false);

        //Obtenemos que comida se ha seleccionado

        //Obtenemos los spinner y calendario desde el xml
        dropdownAl =(Spinner)v.findViewById(R.id.spinnerAlimentos);
        dropdownCom = (Spinner) v.findViewById(R.id.spinnerComidaArray);
        calendar = (CalendarView) v.findViewById(R.id.calendarView);
        dropdownCant = (Spinner) v.findViewById(R.id.spinnerCant);
        //Creamos una lista para los alimentos del spinner
        final JSONArray jsonAl = obtenerAlimentos();
        String[]  spinnerAlAr = null;
        String[] spinnerNombreAlimentosArray = new String[jsonAl.length()];//Array con nombres alim.
        final String[] spinnerAlimentosArray = new String[jsonAl.length()];//Array con objs. alim.
        for(int i = 0; i<jsonAl.length();i++){
            try {
                JSONObject jAl = jsonAl.getJSONObject(i);
                spinnerAlimentosArray[i] = jAl.toString();//Lista para obtener obj. alim. al seleccionar del spinner
                String nombre = jAl.getString("nombre");
                spinnerNombreAlimentosArray[i]=nombre; //Introd. nombres alim. en spinner
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinnerAlAr = spinnerNombreAlimentosArray;
        String[] spinnerComAr = new String[3];
        spinnerComAr[0]="Desayuno";
        spinnerComAr[1]="Comida";
        spinnerComAr[2]="Cena";
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerAlAr);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerComAr);
        //set the spinners adapter to the previously created one.
        dropdownAl.setAdapter(adapter);
        dropdownCom.setAdapter(adapter2);
        //Ejecutamos para introducir valores en la base de datos
        Button botonReg = (Button) v.findViewById(R.id.botonReg);
        //Obtenemos la fecha introducida en el calendarView
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month = month+1;
                fechaSeleccionada = year+"-"+month+"-"+dayOfMonth;
            }
        });
        botonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.N)
            public void onClick(View v) {
                //Obtenemos el tipo de comida que se ha seleccionado
                if(dropdownCom.getSelectedItemPosition()==0){
                    tipoComidaSel = "D";
                }else if(dropdownCom.getSelectedItemPosition()==1) {
                    tipoComidaSel = "C";
                }else if(dropdownCom.getSelectedItemPosition()==2){
                    tipoComidaSel = "A";
                }
                //Obtener el id del alimento que se ha seleccionado
                int idAlSeleccionado = dropdownAl.getSelectedItemPosition();
                //Obtenemos el jsonObject del alimento corresp. al id selecc.
                JSONObject JOAlSel = new JSONObject();
                try {
                    JOAlSel = jsonAl.getJSONObject(idAlSeleccionado);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Obtenemos la cantidad de alimentos que queremos introducir
                cantidadAlSel = dropdownCant.getSelectedItem().toString();
                //Obtenemos los parámetros del objeto para enviarlos a asynctask
                try {
                    nombreAlSel = JOAlSel.getString("nombre");
                    codigoAlSel= JOAlSel.getString("codigo");
                 //   cantidadAlSel = JOAlSel.getString("calorias");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = getActivity().getIntent();
                Bundle b = intent.getExtras();

//                if(b!=null)
//                {
//                    String Email =(String) b.get("Email");
//                    correoLog = Email;
//                }

                String alSeleccionado = spinnerAlimentosArray[idAlSeleccionado];
                //fechaSeleccionada = sdf.format(new Date(calendar.getDate()));
                regCalFragment.TareaWSEnviar tareaAsincrona = new regCalFragment.TareaWSEnviar();
                System.out.println("Fecha: "+fechaSeleccionada+" jsonAlimento: "+alSeleccionado);
                tareaAsincrona.execute(alSeleccionado, fechaSeleccionada, nombreAlSel,
                        fechaSeleccionada, tipoComidaSel, codigoAlSel, cantidadAlSel, correoLog);
            }
        });
        return v;
    }

    public JSONArray obtenerAlimentos() { //Conexión para obtener alimentos
        JSONArray jsonArray = new JSONArray();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

            //Preparamos la conexión HTTP
            HttpClient httpClient = new DefaultHttpClient();
            String laUrl;
            laUrl = "http://10.111.66.10:567/Api/Alimentos";

            HttpGet del = new HttpGet(laUrl);
            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray jsonAl = new JSONArray(respStr);
                for (int j = 0 ; j<jsonAl.length() ; j++){
                    JSONObject jOb = jsonAl.getJSONObject(j);
                    jsonArray.put(j, jOb);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return jsonArray;

    }
    @TargetApi(11)
    private class TareaWSEnviar extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            String codigoAl = params[5];

            //Preparamos la conexión HTTP
            HttpClient httpClient = new DefaultHttpClient();
            String laUrl;
            laUrl = "http://10.111.66.10:567/Api/Calorias/Caloria";

            HttpPost del = new HttpPost(laUrl);
            del.setHeader("Accept", "application/json");
            del.setHeader("Content-type", "application/json");

            try {
                SharedPreferences spf = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String sharedEmail = spf.getString("Email","");
                //Creamos el objeto JSON
                JSONObject respJSON = new JSONObject();
                //Obtenemos valores del objeto JSON para su uso
                respJSON.put("email", sharedEmail);
                respJSON.put("fecha", params[3]); //Aquí iba params[3]
                respJSON.put("tipocomida", params[4]);
                respJSON.put("codigoalimento", params[5]);
                respJSON.put("cantidad", params[6]);

                StringEntity entity = new StringEntity(respJSON.toString());
                del.setEntity(entity);
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                System.out.println("--> Respuesta respString: "+ respStr);
                if(respStr.equals("true"))
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),"Introducción realizada", Toast.LENGTH_SHORT).show();
                        }
                    });
                    System.out.println("---> Introducción OK!");
                resul = true;


            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
            }

            return resul;
        }


        protected void onPostExecute(Boolean result) {

            if (result) {

            }
        }
    }
}
