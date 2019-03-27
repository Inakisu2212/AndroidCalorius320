package com.example.calorius;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogoutFragment extends Fragment {
    private Button botonLogout;

    public LogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(),"Sesión cerrada", Toast.LENGTH_SHORT).show();
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).actualizarHeader();
            }

        });
        botonLogout = (Button) v.findViewById(R.id.logoutButton);
        //botonLogout.setVisibility(v.VISIBLE);
        /*botonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(),"Sesión cerrada", Toast.LENGTH_SHORT).show();
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).actualizarHeader();
                    }

                });
                Fragment freg = new LoginFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment fragmentLogI = fragmentManager.findFragmentById(R.id.frameLayout);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout2, freg);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                botonLogout.setVisibility(v.INVISIBLE);
            }
        });*/
        return v;
    }
}


