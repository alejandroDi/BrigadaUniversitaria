package com.example.brigadauniversitaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import java.security.MessageDigest;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    private EditText mNombre, mCorreo, mCContraseña, mContraseña;
    private RadioButton mHombre, mMujer;
    private Button mRegistrar;
    private TextView mLogin;
    private String contraseñaen;
    private String contraseñaen2;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    DatabaseReference mDatabase;

    private String nombre="";
    private String genero="";
    private String correo="";
    private String contraseña="";
    private String contraseña2="";
    private String key="segapp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mNombre = findViewById(R.id.txtNombre);
        mHombre = findViewById(R.id.Hombre);
        mMujer = findViewById(R.id.Mujer);
        mCorreo = findViewById(R.id.txtCorreo);
        mCContraseña = findViewById(R.id.txtCContraseña);
        mContraseña = findViewById(R.id.txtContraseña);
        mRegistrar = findViewById(R.id.btnRegistrar);
        mLogin = findViewById(R.id.Loginbtn);

        fAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        progressBar = findViewById(R.id.progressBar2);

       /* if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Registro.class));
            finish();
        }*/

        mRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = mNombre.getText().toString().trim();
                if(mHombre.isChecked() == true){
                    genero="Hombre";
                }else if(mMujer.isChecked() == true){
                    genero="Mujer";
                }
                correo = mCorreo.getText().toString().trim();
                contraseña = mContraseña.getText().toString().trim();
                contraseña2 = mCContraseña.getText().toString().trim();
                try {
                    contraseñaen = encriptar(contraseña, key);
                    contraseñaen2 = encriptar(contraseña2, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!TextUtils.isEmpty(correo) && !TextUtils.isEmpty(contraseña) && !TextUtils.isEmpty(genero)
                        && !TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(contraseña2) && contraseñaen.equals(contraseñaen2)){
                    progressBar.setVisibility(View.VISIBLE);
                    Registrar();
                }else{
                    if (TextUtils.isEmpty(correo)){
                        mCorreo.setError("Ingresa el Correo");
                    }
                    if (TextUtils.isEmpty(contraseña)){
                        mContraseña.setError("Ingresa el Cotraseña");
                    }
                    if (TextUtils.isEmpty(contraseña2)){
                        mCContraseña.setError("Ingresa el Cotraseña");
                    }
                    if (TextUtils.isEmpty(nombre)){
                        mNombre.setError("Ingresa el Nombre");
                    }

                    if(!contraseñaen.equals(contraseñaen2)){
                        mCContraseña.setError("Contraseña incorrecta");
                    }

                    if (genero==""){

                        Toast.makeText(Registro.this, "Seleciona un genero", Toast.LENGTH_SHORT).show();
                    }

                    if (contraseña.length()<6){
                        Toast.makeText(Registro.this, "La contraseña debe ser mayo a 5 caracteres", Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(Registro.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });



    }

    private String encriptar(String contra, String keyy) throws Exception{
        SecretKeySpec secretKey = generateKey(keyy);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] datosEncriptadosBytes = cipher.doFinal(contra.getBytes());
        String datosEncriptadosString = Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
        return datosEncriptadosString;
    }

    private SecretKeySpec generateKey(String keyy) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] ke = keyy.getBytes("UTF-8");
        ke = sha.digest(ke);
        SecretKeySpec secretKey = new SecretKeySpec(ke, "AES");
        return secretKey;
    }

    private void Registrar(){
        fAuth.createUserWithEmailAndPassword(correo,contraseñaen).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    String id = fAuth.getCurrentUser().getUid();
                    Map<String,Object> map = new HashMap<>();
                    map.put("nombre",nombre);
                    map.put("genero",genero);
                    map.put("correo",correo);
                    map.put("contraseña",contraseñaen);


                    mDatabase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Toast.makeText(Registro.this, "Usuario creado", Toast.LENGTH_SHORT).show();


                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nombre)
                                        .build();
                                String name = user.getDisplayName();
                                Toast.makeText(Registro.this, name, Toast.LENGTH_SHORT).show();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    String TAG = null;
                                                    Log.d(TAG,"User profile updated.");
                                                } 
                                            }
                                        });
                                startActivity(new Intent(getApplicationContext(),NavigatorActivity.class));
                            }else {
                                Toast.makeText(Registro.this, "Error al registrar", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }else{
                    Toast.makeText(Registro.this, "Error este correo ya esta en uso !", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}