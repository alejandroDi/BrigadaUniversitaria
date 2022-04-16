package com.example.brigadauniversitaria.localModule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.localModule.agenda.AgregarAgenda;
import com.example.brigadauniversitaria.localModule.agenda.ListarAgenda;

public class LocalesFragment extends Fragment {

    Button btnagenda1, btnbuscar2;

    public LocalesFragment() {


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            //View V = super.onCreateView(inflater, container, savedInstanceState);
            View A= inflater.inflate(R.layout.fragment_locales,container,false);
            btnagenda1= (Button) A.findViewById(R.id.idagenda);

            btnagenda1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Intent intent= new Intent(ListarAdmin.this,ListaAgenda.class);
                    FragmentManager fragmentManager= getFragmentManager();
                    FragmentTransaction myfragmentTransaction = fragmentManager.beginTransaction();

                    ListarAgenda listaAgenda = new ListarAgenda();

                    myfragmentTransaction.replace(R.id.contenedor, listaAgenda);
                    myfragmentTransaction.commit();



                }
            });

            btnbuscar2=(Button) A.findViewById(R.id.idagregar);

            btnbuscar2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager= getFragmentManager();
                    FragmentTransaction myfragmentTransaction = fragmentManager.beginTransaction();

                    //ListaAgenda listaAgenda = new ListaAgenda();
                    AgregarAgenda agregarAgenda= new AgregarAgenda();

                    myfragmentTransaction.replace(R.id.contenedor, agregarAgenda);
                    myfragmentTransaction.commit();
                }
            });

            return  A;
    }
}