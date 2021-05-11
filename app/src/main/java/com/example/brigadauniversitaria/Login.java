package com.example.brigadauniversitaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import java.security.MessageDigest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {
    private EditText mCorreo, mContraseña;
    private Button mRegistrar, mLogin;
    private ProgressBar progressBar;
    private FirebaseAuth fAuth;
    private String contraseñaLen;
    private String correo;
    private String contraseña;
    private String key="segapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCorreo = findViewById(R.id.txtCorreoL);
        mContraseña = findViewById(R.id.txtContraseñaL);
        progressBar = findViewById(R.id.progressBar);
        mLogin = findViewById(R.id.btnLogin);
        mRegistrar = findViewById(R.id.btnRegistro);

        fAuth = FirebaseAuth.getInstance();
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                correo = mCorreo.getText().toString().trim();
                contraseña = mContraseña.getText().toString().trim();
                try {
                    contraseñaLen= encriptar(contraseña, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!TextUtils.isEmpty(correo) && !TextUtils.isEmpty(contraseña)){
                    Identificar();
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    if (TextUtils.isEmpty(correo)){
                        mCorreo.setError("Ingresa el Correo");
                    }
                   if (TextUtils.isEmpty(contraseña)){
                       mContraseña.setError("Ingresa la contraseña");
                   }
                    Toast.makeText(Login.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();

                }

            }
        });

        mRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    
                startActivity(new Intent(getApplicationContext(),Registro.class));
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

    private void Identificar(){
        fAuth.signInWithEmailAndPassword(correo,contraseñaLen).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()){
                  Toast.makeText(Login.this, "Iniciando sesion", Toast.LENGTH_SHORT).show();
                  startActivity(new Intent(getApplicationContext(),NavigatorActivity.class));
              }else {
                  Toast.makeText(Login.this, "Error usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                  progressBar.setVisibility(View.GONE);
              }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = fAuth.getCurrentUser();

        if (user != null){
            startActivity(new Intent(getApplicationContext(),NavigatorActivity.class));
        }
    }
}