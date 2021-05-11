package com.example.brigadauniversitaria.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.fonts.FontFamily;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.brigadauniversitaria.NavigatorActivity;
import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.Registro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PerfilFragment extends Fragment {

    ImageView fotoPerfil;
    EditText nombre, correo;
    RadioGroup radio;
    RadioButton genero,hombre,mujer;
    Button modificar;
    private DatabaseReference mDatabase;
    private String nom="";
    private String gen="";
    private String cor="";
    FirebaseUser user;
    StorageReference storageReference;
    View vista;

    public PerfilFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_perfil,container,false);
        fotoPerfil = vista.findViewById(R.id.image_profile);
        nombre = vista.findViewById(R.id.txtNombreP);
        correo = vista.findViewById(R.id.txtCorreoP);
        radio = vista.findViewById(R.id.radio);
        hombre = vista.findViewById(R.id.HombreP);
        mujer = vista.findViewById(R.id.MujerP);
        modificar = vista.findViewById(R.id.btnModificarP);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileref = storageReference.child("users/"+user.getUid()+"/profile.jpg");
        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(fotoPerfil);
            }

        });

        cargarDatos(user);

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nom = nombre.getText().toString().trim();
                if(hombre.isChecked() == true){
                    gen="Hombre";
                }else if(mujer.isChecked() == true){
                    gen="Mujer";
                }
                cor = correo.getText().toString().trim();

                if (!TextUtils.isEmpty(cor)  && !TextUtils.isEmpty(nom) ){
                    Registrar();
                    Intent navigator = new Intent( getActivity() ,NavigatorActivity.class);
                    startActivity(navigator);
                    Toast.makeText(getContext(),"Modificado",Toast.LENGTH_LONG).show();

                }else {
                    if (TextUtils.isEmpty(cor)){
                        correo.setError("Ingresa el Correo");
                    }

                    if (TextUtils.isEmpty(nom)){
                        nombre.setError("Ingresa el Nombre");
                    }


                    if (radio.getCheckedRadioButtonId() == -1){

                        Toast.makeText(getContext(), "Seleciona un genero", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(getContext(), "Debe completar los campos", Toast.LENGTH_SHORT).show();



                }
            }
        });


        return vista;
    }

    private void Registrar() {
        String id = user.getUid();
        Map<String,Object> Map = new HashMap<>();
        Map.put("nombre",nom);
        Map.put("genero",gen);
        Map.put("correo",cor);
        mDatabase.child("Usuarios").child(id).updateChildren(Map);
    }

    public void cargarDatos(FirebaseUser user){
        if (user != null) {
            String id = user.getUid();


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String nom = snapshot.child("Usuarios").child(id).child("nombre").getValue().toString();
                        String gen = snapshot.child("Usuarios").child(id).child("genero").getValue().toString();
                        String corr = snapshot.child("Usuarios").child(id).child("correo").getValue().toString();
                        nombre.setText(nom);
                        if (gen.equals("Hombre")){
                            genero = (RadioButton) radio.getChildAt(0);
                            genero.setChecked(true);
                        }else {
                            genero = (RadioButton) radio.getChildAt(1);
                            genero.setChecked(true);

                        }
                        correo.setText(corr);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageuri = data.getData();
                //fotoPerfil.setImageURI(imageuri);
                subirImagen(imageuri);
            }
        }
    }

    private void subirImagen(Uri image) {
        StorageReference fileRef = storageReference.child("users/"+user.getUid()+"/profile.jpg");
        fileRef.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(fotoPerfil);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"error subir imagen",Toast.LENGTH_LONG).show();
            }
        });
    }

}