package com.example.brigadauniversitaria.loginModule.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.brigadauniversitaria.NavigatorActivity;
import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.loginModule.LoginPresenter;
import com.example.brigadauniversitaria.loginModule.LoginPresenterClass;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Login extends AppCompatActivity implements LoginView{
    public static final int RC_SIGN_IN = 21;
    @BindView(R.id.txtMessage)
    TextView txtMessage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mPresenter = new LoginPresenterClass(this);
        mPresenter.onCreate();
        mPresenter.getStatusAuth();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        mPresenter.result(requestCode, resultCode, data);
    }
    /*
    * Login view
    * */

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(this,NavigatorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    @Override
    public void openUILogin() {
        AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.GoogleBuilder().build();
        startActivityForResult(AuthUI.getInstance()
                                       .createSignInIntentBuilder()
                                       .setIsSmartLockEnabled(false)
                                       .setTosAndPrivacyPolicyUrls("www.brigada.policy.udg.mx","www.brigada.privacy.udg.mx")
                                       .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                                                            googleIdp))
                                       .setTheme(R.style.Theme_BrigadaUniversitaria)
                                       .setLogo(R.mipmap.ic_launcher_foreground)
                                       .build(),RC_SIGN_IN);
    }

    @Override
    public void showLoginSuccessfully(Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        String email = "";
        if (response != null){
            email = response.getEmail();
        }
        Toast.makeText(this,
                       getString(R.string.login_message_success, email),
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMenssageStarting() {
        txtMessage.setText(R.string.login_message_loading);

    }

    @Override
    public void showError(int resMsg) {
        Toast.makeText(this, resMsg, Toast.LENGTH_LONG).show();
    }
}