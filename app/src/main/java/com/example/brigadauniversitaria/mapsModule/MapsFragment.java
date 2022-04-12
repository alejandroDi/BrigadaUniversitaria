package com.example.brigadauniversitaria.mapsModule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.addMarketModule.AddMarketFragment;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MapsFragment extends Fragment{
    @BindView(R.id.btnInsidente)
    FloatingActionButton btnInsidente;
    Unbinder unbinder;
    private int MY_PERMISSION_REQUEST_FINE_LOCATION;
    FloatingActionButton ubicacion,verificar, insi;
    double longitud = -103.3475239, latitud = 20.730948;
    private FusedLocationProviderClient fusedLocationClient;
    GoogleMap mMap;
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
    View view;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            LatLng centro = new LatLng(latitud,longitud);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centro,15));

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

            ///botones////
            ubicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ubicacionActual(googleMap);
                }
            });


        }
    };

    private void ubicacionActual(GoogleMap googleMap) {
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
                            googleMap.addMarker(new MarkerOptions().position(mylocation).title("Ubicación Actual")
                                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_miubi)).anchor(0.0f,0.0f));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,16));
                            Toast.makeText(getContext(),"latitud: "+location.getLatitude()+"longitud: "+location.getLongitude(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps,container,false);
        unbinder = ButterKnife.bind(this,view);
        ubicacion = view.findViewById(R.id.btnLocation);
        verificar = view.findViewById(R.id.btnVeri);
        insi = view.findViewById(R.id.btnInsidente);
        return view;
    }
    @OnClick(R.id.btnInsidente)
    public void onAddClicked(){
        new AddMarketFragment().show(getActivity().getSupportFragmentManager(),getString(R.string.addFriend_title));
    }


    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }



}