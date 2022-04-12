package com.example.brigadauniversitaria.addMarketModule;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.addModule.view.AddView;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.brigadauniversitaria.common.pojo.Market;
import com.example.brigadauniversitaria.common.utils.UtilsCommon;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMarketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMarketFragment extends DialogFragment implements DialogInterface.OnShowListener {
    @BindView(R.id.btnUbiActual)
    MaterialButton btnUbiActual;
    @BindView(R.id.actvClasificacion)
    AutoCompleteTextView actvClasificacion;
    @BindView(R.id.actvLugar)
    AutoCompleteTextView actvLugar;
    @BindView(R.id.etDescripcion)
    TextInputEditText etDescripcion;
    private Button positivButton;
    GoogleMap googleMap;
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
    private int MY_PERMISSION_REQUEST_FINE_LOCATION;
    private FusedLocationProviderClient fusedLocationClient;
    double latitud;
    double longitud;
    long maxid;
    Unbinder unbinder;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddMarketFragment() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMarketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMarketFragment newInstance(String param1,String param2) {
        AddMarketFragment fragment = new AddMarketFragment();
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
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.addFriend_title)
                .setPositiveButton(R.string.common_label_accept, null)
                .setNeutralButton(R.string.common_label_cancel,null);
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_market,null);
        builder.setView(view);
        unbinder = ButterKnife.bind(this,view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);
        String[] lugar = new String[]{"carretera","domicilio","edificio",
                "institucion educativa","local cormercial","puente","trasporte publico","vehiculo","via publicac",};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.item_dropdown_lugar,lugar);
        actvLugar.setAdapter(adapter);
        String[] clasificacion = new String[]{"homicidio doloso","lesiones dolosas","Robo a int de vehiculos"
                ,"Robo a negocio","Robo a persona","Robo a vehiculos particulares"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(),R.layout.item_dropdown_clasificacion,clasificacion);
        actvClasificacion.setAdapter(adapter2);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        return dialog;
    }





    @Override
    public void onShow(DialogInterface dialogInterface) {
        final AlertDialog dialog = (AlertDialog)getDialog();
        if (dialog != null){
            positivButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);

            positivButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ActivityCompat.checkSelfPermission(getContext(),
                                                           Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) getContext(),
                                                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                          MY_PERMISSION_REQUEST_FINE_LOCATION);

                        return;
                    }
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener((Activity) getContext(),new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        LatLng mylocation = new LatLng(location.getLatitude(),location.getLongitude());
                                        ////agregar datos


                                        String ubi = String.valueOf(location.getLatitude());
                                        String calsificacion = actvClasificacion.getText().toString().trim();
                                        String lugar = actvLugar.getText().toString().trim();
                                        String descripcion = etDescripcion.getText().toString().trim();
                                        if (TextUtils.isEmpty(ubi)) {
                                            Toast.makeText(getActivity().getApplicationContext(),"Por favor pulsar el boton Generar ubicacion ",Toast.LENGTH_LONG).show();
                                        } else if (TextUtils.isEmpty(calsificacion)) {
                                            Toast.makeText(getActivity().getApplicationContext(),"Por favor ingresa una clasificacion ",Toast.LENGTH_LONG).show();
                                        } else if (TextUtils.isEmpty(lugar)) {
                                            Toast.makeText(getActivity().getApplicationContext(),"Por favor ingresa un lugar ",Toast.LENGTH_LONG).show();
                                        } else if (TextUtils.isEmpty(descripcion)) {
                                            Toast.makeText(getActivity().getApplicationContext(),"Por favor ingresa una descripcion ",Toast.LENGTH_LONG).show();
                                        } else {
                                            Market market = new Market(Double.valueOf(location.getLatitude()),Double.valueOf(location.getLongitude()),calsificacion,lugar,descripcion);
                                            mDatabaseAPI.getMarksReferences().push().setValue(market);
                                            Toast.makeText(getActivity().getApplicationContext(),"Datos guardados correctamente ",Toast.LENGTH_LONG).show();
                                        }
                                        //Toast.makeText(getContext(),"latitud: "+location.getLatitude()+"longitud: "+location.getLongitude(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                }
            });
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
    }
}