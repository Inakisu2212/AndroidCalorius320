package com.example.calorius;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calorius.objetos.usuario;
import com.example.calorius.objetosServiceInterfaces.usuarioService;

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
public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button botonLogin;
    private TextView textoEmail, textoPasswd;
    //private TextView lblResultado;
    private SharedPreferences sharedPreferences;
    private usuarioService usuService; //Esta clase la crea más adelante el retrofit
    private final String laUrl = "http://10.111.66.10:567/";
    private List<usuario> listaUsu = new ArrayList<usuario>();
    private usuario elUsuario;

    public LoginFragment() {
        // Contructor publico requerido. Vacio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        textoEmail = (TextView) v.findViewById(R.id.emailText);
        textoPasswd = (TextView) v.findViewById(R.id.passwdText);

        botonLogin = (Button) v.findViewById(R.id.loginButton);

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(11)
            public void onClick(View v) {
               String stringEmail = textoEmail.getText().toString();
                String stringPasswd = textoPasswd.getText().toString();
                accionLogin(stringEmail, stringPasswd);

            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).actualizarHeader();
            }

        });
        return v;
    }
    public  void vibrate(LoginFragment fragment, int tiempo) {
        Vibrator vibrator = (Vibrator) fragment.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(tiempo);
    }
    @Override
    public void onClick(View v) {

    }

    public void accionLogin(String emailIntrod, String passwdIntrod){
        final String emilio = emailIntrod;
        final String passwd = passwdIntrod;

        Retrofit instRetrofit = new Retrofit.Builder().baseUrl(laUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();
        usuService = instRetrofit.create(usuarioService.class);

        Call<usuario> elUsu = usuService.getObtenerUsuario(emilio, passwd);

        elUsu.enqueue(new Callback<usuario>(){
            @Override
            public void onResponse(Call<usuario> call, Response<usuario> response){
                if(response.isSuccessful()) {
                    elUsuario = response.body();
                    boolean correcto = false; //Para comprobar coindicendia email y passwd

                    String emails = elUsuario.getEmailUsuario();
                    String passwds = elUsuario.getPasswordUsuario();
                    if (emails.equals(emilio) && passwds.equals(passwd)) {
                        correcto = true;
                    }

                    //Shared preferences
                    sharedPreferences = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                    if (correcto) {
                        System.out.println("-----> Login correcto!");
                        //Guardar información en sharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Email", emilio);
                        editor.putString("Contra", passwd);
                        editor.commit();

                        //Que suelte un toast diciendo que es correcto
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(60);
                                Toast.makeText(getActivity(), "Login correcto", Toast.LENGTH_SHORT).show();
                                //Actualizamos el header para que aparezca el nombre
                                ((MainActivity) getActivity()).actualizarHeader();
                            }
                        });

                    } else {
                        //Que suelte un toast diciendo que es incorrecto
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(), "Login incorrecto", Toast.LENGTH_SHORT).show();
                                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                long[] pattern = {0, 60, 50, 60, 50, 60};
                                vibrator.vibrate(pattern, -1);

                            }
                        });

                    }
                }
            }
            @Override
            public void onFailure(Call<usuario> call, Throwable t) {
                //Que suelte un toast diciendo que es incorrecto
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Login incorrecto", Toast.LENGTH_SHORT).show();
                        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        long[] pattern = {0, 60, 50, 60, 50, 60};
                        vibrator.vibrate(pattern, -1);

                    }
                });
                System.out.println("------>  Error al obtener el usuario. Comprobar si se han introducido " +
                "correctamente el mail y la passwd");
            }
        });

    }

/*
    //A partir de aquí pasan cosas de HTTP REST
    @TargetApi(11)
    private class TareaWSObtener extends AsyncTask<String,Integer,Boolean> {

        private String fotoUsu = "No se ha provisto";

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            final String emailIntrod = params[0];
            String passwdIntrod = params[1];

            //Preparamos la conexión HTTP
            HttpClient httpClient = new DefaultHttpClient();

            System.out.println("Email escrito: "+ emailIntrod);
            System.out.println("Passwd escrito: "+ passwdIntrod);


            HttpGet del =
                    new HttpGet("http://10.111.66.10:567/Api/Usuarios/Usuario/" + emailIntrod+"/");
            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                //Creamos el objeto JSON
                JSONObject respJSON = new JSONObject(respStr);
                //Obtenemos valores del objeto JSON para su uso
                String emailUsu = respJSON.getString("email");
                String passwdUsu = respJSON.getString("password");
                fotoUsu = respJSON.getString("foto");

                //Shared preferences
                sharedPreferences = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);

                System.out.println("Devuelve: " + emailUsu + " - " + passwdUsu + " - " + fotoUsu);
                resul = true;
                //lblResultado.setText("" + emailUsu + " - " + passwdUsu + " - " + fotoUsu);
                //------Comprobamos que email y password coincidan
                if(emailUsu.equals(emailIntrod) && passwdUsu.equals(passwdIntrod)){
                    System.out.println("-----> Login correcto!");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Email", emailUsu);
                    editor.putString("Contra",passwdUsu);
                    editor.commit();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity)getActivity()).actualizarHeader();
                        }

                    });
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),"Login correcto", Toast.LENGTH_SHORT).show();
                        }
                    });
//                    Toast toast = Toast.makeText(getContext(), "Login correcto", Toast.LENGTH_SHORT);
//                    toast.show();
//                    Fragment frg = new Fragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.nav_login, frg);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();

                    //No sé si el resto de cosas suceden aquí o en otro sitio
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),"Login INcorrecto", Toast.LENGTH_SHORT).show();
                        }
                    });
//                    Toast toast = Toast.makeText(getContext(), "Login incorrecto", Toast.LENGTH_SHORT);
//                    toast.show();
                }


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {

            }
        }
    }*/

}
