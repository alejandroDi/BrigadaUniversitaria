package com.example.brigadauniversitaria.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.brigadauniversitaria.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapsFragment extends Fragment {
    private int MY_PERMISSION_REQUEST_FINE_LOCATION;
    FloatingActionButton ubicacion, compRuta, verificar, insi;
    double longitud = -103.3475239, latitud = 20.730948;
    private FusedLocationProviderClient fusedLocationClient;
    GoogleMap mMap;
    View view;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
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
                            LatLng sydney = new LatLng(location.getLatitude(),location.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));
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
        ubicacion = view.findViewById(R.id.btnLocation);
        verificar = view.findViewById(R.id.btnVeri);
        insi = view.findViewById(R.id.btnInsidente);
        return view;
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