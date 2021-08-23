package com.example.brigadauniversitaria;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brigadauniversitaria.loginModule.view.Login;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NavigatorActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseUser currentUser;
    StorageReference storageReference;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        storageReference = FirebaseStorage.getInstance().getReference();

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        navigationView.getMenu().findItem(R.id.nav_close).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                return true;
            }
        });
        updateNavHeader();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_map,R.id.nav_perfil,R.id.nav_notif,R.id.nav_chat,R.id.nav_locales,R.id.nav_contactos,R.id.nav_informacion)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView,navController);
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController,mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void updateNavHeader (){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.username);
        TextView navMail = headerView.findViewById(R.id.usermail);
        ImageView navImg = headerView.findViewById(R.id.image_user);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            StorageReference profileref = storageReference.child("users/"+currentUser.getUid()+"/profile.jpg");
            profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(navImg);
                }
            });
           navMail.setText(user.getEmail());
            navUsername.setText(user.getDisplayName());
        }




    }

}