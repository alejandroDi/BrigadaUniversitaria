package com.example.brigadauniversitaria;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;

import com.example.brigadauniversitaria.chatModule.view.ChatsFragment;
import com.example.brigadauniversitaria.common.pojo.Contacto;
import com.example.brigadauniversitaria.mainModule.view.adapters.OnItemClickListener;
import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.common.utils.UtilsCommon;
import com.example.brigadauniversitaria.loginModule.view.Login;
import com.example.brigadauniversitaria.mainModule.MainPresenter;
import com.example.brigadauniversitaria.mainModule.MainPresenterClass;
import com.example.brigadauniversitaria.mainModule.view.MainView;
import com.example.brigadauniversitaria.profileModule.view.PerfilFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.example.brigadauniversitaria.common.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

public class  NavigatorActivity extends AppCompatActivity implements iComunicaFragment{


    private static final int RC_PROFILE = 23;
    private AppBarConfiguration mAppBarConfiguration;
    private MainPresenter mPresenter;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ChatsFragment chatsFragment;

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
                    R.id.nav_map,R.id.nav_perfil,R.id.nav_chat,R.id.nav_locales,R.id.nav_informacion)
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

    @Override
    public void chatOpen(User user) {
        chatsFragment = new ChatsFragment();
        //objecto bundle para trasportar informacion
        Bundle bundle = new Bundle();
        //enviar objecto que recibe con serializable
        bundle.putString(User.UID,user.getUid());
        bundle.putString(User.USERNAME,user.getUsername());
        bundle.putString(User.EMAIL,user.getEmail());
        bundle.putString(User.PHOTO_URL,user.getPhotoUrl());
        chatsFragment.setArguments(bundle);
        //abrir fragemnto
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.drawer_layout,chatsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void contactOpen(Contacto contacto) {
        String nombre = contacto.getNombre();
        String telefono = contacto.getTelefono();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+telefono));
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling
            // ActivityCompart#requestPerissions
            //  here to request the missing permissions, and then overriding
            //    public  void onRequestPermissionsResult (int requestCode, String[] permissions,
            //                                             int[] grantResults)
            //to handle the case where user granted permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this,"Falta activar el permiso de llamadas", Toast.LENGTH_SHORT);
            //Toast.makeText(DetalleContacto.this, "Falta activar el permiso de llamda", (Toast.LENGTH_SHORT));
            return;

        }

        startActivity(intent);

        Toast.makeText(this,contacto.getNombre()+contacto.getTelefono(),Toast.LENGTH_LONG).show();

    }
}