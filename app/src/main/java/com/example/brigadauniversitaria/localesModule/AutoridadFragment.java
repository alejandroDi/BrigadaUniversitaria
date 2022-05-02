package com.example.brigadauniversitaria.localesModule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.common.pojo.Contacto;
import com.example.brigadauniversitaria.iComunicaFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AutoridadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AutoridadFragment extends Fragment {
    @BindView(R.id.lvContactos)
    ListView lvContactos;
    ArrayList<Contacto> contactos;
    Unbinder unbinder;
    Activity activity;
    iComunicaFragment iterfaceComucaFragment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AutoridadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AutoridadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AutoridadFragment newInstance(String param1,String param2) {
        AutoridadFragment fragment = new AutoridadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,param1);
        args.putString(ARG_PARAM2,param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_autoridad,container,false);
        unbinder = ButterKnife.bind(this,view);
        contactos = new ArrayList<>();
        contactos.add(new Contacto("Atencion ciudadana","3317595411"));
        contactos.add(new Contacto("Cruz roja","3320669274"));
        contactos.add(new Contacto("Cruz verde","3312646491"));
        contactos.add(new Contacto("Denuncia escolar","331759548"));
        contactos.add(new Contacto("Proteccion civil","331759544"));
        ArrayList<String> nombreContactos = new ArrayList<>();

        for (Contacto contacto:contactos) {
            nombreContactos.add(contacto.getNombre()+"\n\n"+contacto.getTelefono());

        }
        lvContactos.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,nombreContactos));
        lvContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view,int i,long l) {
                Contacto con = new Contacto();
                con.setNombre(contactos.get(i).getNombre());
                con.setTelefono(contactos.get(i).getTelefono());
                iterfaceComucaFragment.contactOpen(con);
            }
        });
        return view;
    }
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.activity = (Activity) context;
            iterfaceComucaFragment = (iComunicaFragment) this.activity;
        }
    }
}