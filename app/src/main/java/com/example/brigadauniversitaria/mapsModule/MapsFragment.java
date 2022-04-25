package com.example.brigadauniversitaria.mapsModule;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.addMarketModule.AddMarketFragment;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.brigadauniversitaria.common.pojo.Market;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MapsFragment extends Fragment{
    @BindView(R.id.btnInsidente)
    FloatingActionButton btnInsidente;
    @BindView(R.id.btnVeri)
    FloatingActionButton btnVeri;
    Unbinder unbinder;
    private int MY_PERMISSION_REQUEST_FINE_LOCATION;
    FloatingActionButton ubicacion,verificar, insi;
    double longitud = -103.32341, latitud = 20.6584339;
    private FusedLocationProviderClient fusedLocationClient;
    GoogleMap mMap;
    private ArrayList<Marker> temporalRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> RealTimeMarkers = new ArrayList<>();
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
    View view;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
            mMap = googleMap;

            LatLng mylocation = new LatLng(latitud,longitud);
            googleMap.addMarker(new MarkerOptions().position(mylocation).title("cucei")
                                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_miubi)).anchor(0.0f,0.0f));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,16));
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            verIncidentes(googleMap);
            ///botones////
            ubicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ubicacionActual(googleMap);
                }
            });

            btnVeri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validarZona();
                }
            });



        }
    };
    private void verIncidentes(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getContext(),
                                               Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(),
                                              new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                              MY_PERMISSION_REQUEST_FINE_LOCATION);

            return;
        }
            mDatabaseAPI.getMarksReferences().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (Marker dest:RealTimeMarkers){
                        dest.remove();
                    }
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        Market mr = snapshot1.getValue(Market.class);
                        Double latitud = mr.getLatitud();
                        Double longitud = mr.getLongitud();
                        String clasificacion = mr.getClasificacion();
                        String lugar = mr.getLugar();
                        String descrip = mr.getDescripcion();
                        String snip = "lugar: "+lugar+"\n"+"descripcion: "+descrip;
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(latitud,longitud)).title(clasificacion).snippet(snip)
                                .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_incidente)).anchor(0.0f,0.0f);

                        temporalRealTimeMarkers.add(mMap.addMarker(markerOptions));
                    }
                    RealTimeMarkers.clear();
                    RealTimeMarkers.addAll(temporalRealTimeMarkers);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
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
                                                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_miubi)).anchor(0.0f,0.0f));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,16));
                            Toast.makeText(getContext(),"latitud: "+location.getLatitude()+"longitud: "+location.getLongitude(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void validarZona(){

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
                            if(!Python.isStarted()){
                                Python.start(new AndroidPlatform(getContext()));
                            }
                            Python py = Python.getInstance();
                            PyObject pyobj = py.getModule("script");

                            PyObject obj = pyobj.callAttr("main",Double.valueOf(location.getLatitude()),Double.valueOf(location.getLongitude()));
                            Toast.makeText(getContext(),""+obj.toString(),Toast.LENGTH_LONG).show();
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