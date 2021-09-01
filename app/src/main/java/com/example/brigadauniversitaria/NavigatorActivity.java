package com.example.brigadauniversitaria;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.common.utils.UtilsCommon;
import com.example.brigadauniversitaria.loginModule.view.Login;
import com.example.brigadauniversitaria.mainModule.MainPresenter;
import com.example.brigadauniversitaria.mainModule.MainPresenterClass;
import com.example.brigadauniversitaria.mainModule.view.MainView;
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
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NavigatorActivity extends AppCompatActivity{


    private AppBarConfiguration mAppBarConfiguration;
    private MainPresenter mPresenter;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPresenter = new MainPresenterClass(this);
        mPresenter.onCreate();
        mUser = mPresenter.getCurrentUser();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        navigationView.getMenu().findItem(R.id.nav_close).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mPresenter.singOff();
                startActivity(new Intent(getApplicationContext(),Login.class)
                                      .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                        | Intent.FLAG_ACTIVITY_NEW_TASK
                                                        |Intent.FLAG_ACTIVITY_CLEAR_TASK));
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


    public void updateNavHeader () {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.username);
        TextView navMail = headerView.findViewById(R.id.usermail);
        ImageView navImg = headerView.findViewById(R.id.image_user);
        if (mUser != null) {
            // Name, email address, and profile photo Url
            UtilsCommon.loadImagen(this,mUser.getPhotoUrl(),navImg);
            navMail.setText(mUser.getEmail());
            navUsername.setText(mUser.getUsernameValid());
        }
    }

}