package com.example.brigadauniversitaria.localModule.agenda;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.brigadauniversitaria.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AgregarAgenda extends Fragment {

    EditText editnom1, editnum2;
    FloatingActionButton btaction;

    public AgregarAgenda() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View A= inflater.inflate(R.layout.fragment_agregar_agenda,container,false);
        editnom1=(EditText) A.findViewById(R.id.idnombre);
        editnum2=(EditText) A.findViewById(R.id.idnumero);

        btaction=(FloatingActionButton) A.findViewById(R.id.idbtnSelect);

        btaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nos permite interactuar cpn ptras aplicaciones
                Intent intent = new Intent(Intent.ACTION_PICK);
                //setType es el tipo de informacion que podemos acceder. phone obtiene
                // el nombre y numero del contacto en formato Uri
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                //ejecuta odo lo que se menciono anteriormente
                startActivityForResult(intent, 1);
            }
        });

        return A;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // verificiamos con los parametros de requestCode que sea igual a 1 parametro que se puso
        //en le metodo anterior y que resultCode sea igual a 1, variable predefinida
        if (requestCode == 1 && resultCode ==  RESULT_OK){
            //obtener la informacion de los contactos y se almacena en una variable tipo Uri
            Uri uri = data.getData();
            // geContentResolver para poder acceder a la informacion de la aplicacion

            Cursor cursor = getActivity().getContentResolver().query(uri, null,null,null,null,null);

            if (cursor != null && cursor.moveToFirst()){
                //obtenemos la ubicacion del nombre y el numero. esta informacion es de tipo entero y las almacenamos en unas variables
                //podemos acceder al nombre y numero con DISPLAY_NAME Y NUMBER
                int indiceName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indiceNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                //obenemos los valores de nombre y numero del contacto
                String nombre = cursor.getString(indiceName);
                String numero = cursor.getString(indiceNumber);

                //hacemos la limpieza de los datos del numero
                numero = numero.replace("(", "").replace("-","").replace(")","");

                //colocamos aqui los valores que se obtuvieron
                editnom1.setText(nombre);
                editnum2.setText(numero);

            }

        }
    }

}