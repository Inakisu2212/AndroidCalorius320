package com.example.calorius;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.calorius.objetos.alimentos;
import com.example.calorius.objetos.calorias;
import com.example.calorius.objetosServiceInterfaces.alimentosService;
import com.example.calorius.objetosServiceInterfaces.caloriasService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
    private final String laUrl = "http://10.111.66.10:567/";
    //private alimentosService aliService;
    private caloriasService calService;
    private List<alimentos> listaAli = new ArrayList<alimentos>();
    //private int numeroAlimento;

    private ArrayList<String> listaNombreAls;
    private ArrayList<String> listaCodigoAls;
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
        obtenerAlimentos();
        //Obtenemos que comida se ha seleccionado

        //Obtenemos los spinner y calendario desde el xml
        dropdownAl =(Spinner)v.findViewById(R.id.spinnerAlimentos);
        dropdownCom = (Spinner) v.findViewById(R.id.spinnerComidaArray);
        calendar = (CalendarView) v.findViewById(R.id.calendarView);
        dropdownCant = (Spinner) v.findViewById(R.id.spinnerCant);
        //Creamos una lista para los alimentos del spinner

        //final List<alimentos> alsEnLista = listaAli;
        String[]  spinnerAlAr = null;
        final String[] spinnerNombreAlimentosArray = new String[listaNombreAls.size()];//Array con nombres alim.
        final String[] spinnerAlimentosArray = new String[listaCodigoAls.size()];//Array con objs. alim.
        for(int i = 0; i<listaNombreAls.size();i++){
            try {
                spinnerAlimentosArray[i] = listaCodigoAls.get(i).toString();//Lista para obtener obj. alim. al seleccionar del spinner
                //String nombre = jAl.getString("nombre");
                spinnerNombreAlimentosArray[i]=listaNombreAls.get(i); //Introd. nombres alim. en spinner
            } catch (Exception e) {
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
                //numeroAlimento = spinnerAlimentosArray[idAlSeleccionado];

                //Obtenemos la cantidad de alimentos que queremos introducir
                cantidadAlSel = dropdownCant.getSelectedItem().toString();

                //Obtenemos los parámetros del objeto para enviarlos a método introducirCalorias()
                nombreAlSel = spinnerNombreAlimentosArray[idAlSeleccionado];
                codigoAlSel= spinnerAlimentosArray[idAlSeleccionado];

                Intent intent = getActivity().getIntent();
                Bundle b = intent.getExtras();

//                if(b!=null)
//                {
//                    String Email =(String) b.get("Email");
//                    correoLog = Email;
//                }

                String alSeleccionado = spinnerAlimentosArray[idAlSeleccionado].toString(); //sobra?
                //Obtenemos el correo con el que se ha logueado
                SharedPreferences sharedPreferences = getContext()
                        .getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                correoLog = sharedPreferences.getString("Email", "a"); //Habría que controlar en caso de que no se haya logueado
                //fechaSeleccionada = sdf.format(new Date(calendar.getDate()));
//                regCalFragment.TareaWSEnviar tareaAsincrona = new regCalFragment.TareaWSEnviar();
                System.out.println("Fecha: "+fechaSeleccionada+" jsonAlimento: "+alSeleccionado);
//                tareaAsincrona.execute(alSeleccionado, fechaSeleccionada, nombreAlSel,
//                        fechaSeleccionada, tipoComidaSel, codigoAlSel, cantidadAlSel, correoLog);
                introducirCalorias(alSeleccionado, fechaSeleccionada, nombreAlSel, fechaSeleccionada,
                        tipoComidaSel, codigoAlSel, cantidadAlSel, correoLog);
            }
        });
        return v;
    }

    public void obtenerAlimentos() { //Conexión para obtener alimentos
        //final String emilio = emailIntrod;
        //final String passwd = passwdIntrod;

        Retrofit instaRetrofit = new Retrofit.Builder().baseUrl(laUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        alimentosService aliService = instaRetrofit.create(alimentosService.class);

        Call<List<alimentos>> listaAlis = aliService.getAlimentos();

        listaAlis.enqueue(new Callback<List<alimentos>>(){
            @Override
            public void onResponse(Call<List<alimentos>> call, Response<List<alimentos>> response){
                if(response.isSuccessful()){
                    listaAli = response.body();
                }else{
                    System.out.println("---------> "+ response.errorBody());
                }

                for(int i = 0; i < listaAli.size(); i++){
                    listaNombreAls.add(listaAli.get(i).getNombreAlimento());
                    listaCodigoAls.add(listaAli.get(i).getCodigoAlimento().toString());
                }

            }
            @Override
            public void onFailure(Call<List<alimentos>> call, Throwable t) {

            }
        });


        /*JSONArray jsonArray = new JSONArray();
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
        return jsonArray;*/

    }

    private void introducirCalorias(String... params){
        calorias calo = new calorias();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(laUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        calService = retrofit.create(caloriasService.class);

        //Introducimos valores en objeto caloria que enviaremos
        calo.setEmailCaloria(params[7]);
        calo.setFechaCaloria(params[1]);
        calo.setTipocomidaCaloria(params[4]);
        calo.setCodigoAlimentoCaloria(Integer.parseInt(params[5]));
        calo.setCantidadCaloria(Integer.parseInt(params[6]));
        //Introducimos el objt caloria en llamada
        Call<calorias> p = calService.registrarCalorias(calo);
        p.enqueue(new Callback<calorias>() {
            @Override
            public void onResponse(Call<calorias> call, Response<calorias> response) {
                if(response.isSuccessful()){
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(60);
                            Toast.makeText(getActivity(),"Registro realizado", Toast.LENGTH_SHORT).show();
                            System.out.println("---> Reg Cal OK: Verificar en BD!");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<calorias> call, final Throwable t) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        Toast.makeText(getActivity(),"Registro fallido", Toast.LENGTH_SHORT).show();
                        long[] pattern = { 0,60,50,60,50,60};
                        vibrator.vibrate(pattern,-1);
                        System.out.println("----->Onfailure de introducirCalorias: "+ t); //info del error
                    }
                });
            }
        });
    }
    /*@TargetApi(11)
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
    }*/
}
