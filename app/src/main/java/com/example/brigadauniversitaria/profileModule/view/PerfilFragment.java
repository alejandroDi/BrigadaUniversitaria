package com.example.brigadauniversitaria.profileModule.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.brigadauniversitaria.NavigatorActivity;
import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.common.utils.UtilsCommon;
import com.example.brigadauniversitaria.common.utils.UtilsImage;
import com.example.brigadauniversitaria.mainModule.MainPresenterClass;
import com.example.brigadauniversitaria.profileModule.ProfilePresenter;
import com.example.brigadauniversitaria.profileModule.ProfilePresenterClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.app.ActivityCompat.finishAfterTransition;


public class PerfilFragment extends Fragment implements ProfileView{
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.btnEditPhoto)
    ImageButton btnEditPhoto;
    @BindView(R.id.progressBarImage)
    ProgressBar progressBarImage;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.contentMain)
    LinearLayoutCompat contentMain;
    Unbinder unbinder;

    private MenuItem mCurrentMenuItem;
    public static final int RC_PHOTO_PICKER = 22;
    private ProfilePresenter mPresenter;
    private User mUser;


    public PerfilFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil,container,false);
        unbinder = ButterKnife.bind(this,view);
        mPresenter = new ProfilePresenterClass(this);
        mPresenter.onCreate();
        mUser = mPresenter.getCurrentUser();
        setHasOptionsMenu(true);
        mPresenter.setupUser(mUser.getUsername(),
                             mUser.getEmail(),
                             mUser.getPhotoUrl());
        configActionBar();
        return view;
    }

    private void configActionBar(){
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setImageProfile(String photoUrl){
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.ic_timer_sand)
                .error(R.drawable.ic_emoticon_sad)
                .centerCrop();

        Glide.with(this)
                .asBitmap()
                .load(photoUrl)
                .apply(options)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e,Object model,Target<Bitmap> target,boolean isFirstResource) {
                        hideProgressImage();
                        imgProfile.setImageDrawable(ContextCompat.getDrawable(getContext(),
                                                                              R.drawable.ic_upload));
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource,Object model,Target<Bitmap> target,DataSource dataSource,boolean isFirstResource) {
                        imgProfile.setImageBitmap(resource);
                        hideProgressImage();
                        return true;
                    }
                })
                .into(imgProfile);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save_profile:
                    mCurrentMenuItem = item;
                    if (etUsername.getText() != null){
                        mPresenter.updateUsername(etUsername.getText().toString().trim());
                    }
                break;
            case android.R.id.home:
                    if (UtilsCommon.hasMaterialDesign()){
                        ((AppCompatActivity)getActivity()).finishAfterTransition();
                    }else {
                        ((AppCompatActivity)getActivity()).finish();
                    }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,@Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        mPresenter.result(requestCode,resultCode,data);
    }

    @OnClick({R.id.imgProfile,R.id.btnEditPhoto})
    public void onSelectPhoto(View view){
        mPresenter.checkMode();
    }

    /*
    * ProfileView
    *
    * */

    @Override
    public void enableUIElements() {
        setInputs(true);
    }

    @Override
    public void disableUIElements() {
        setInputs(false);
    }

    private void setInputs(boolean enable) {
        etUsername.setEnabled(enable);
        btnEditPhoto.setVisibility(enable? View.VISIBLE : View.GONE);
        if (mCurrentMenuItem != null){
            mCurrentMenuItem.setEnabled(enable);
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressImage() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressImage() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showUserData(String username,String email,String photoUrl) {
        setImageProfile(photoUrl);
         etUsername.setText(username);
         etEmail.setText(email);


    }

    @Override
    public void launchGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,RC_PHOTO_PICKER);
    }

    @Override
    public void openDialogPreview(Intent data) {
        final String urlLocal = data.getDataString();

        final ViewGroup nullParent = null;
        View view = getLayoutInflater().inflate(R.layout.dialog_image_upload_preview,nullParent);
        final CircleImageView imgDialog = view.findViewById(R.id.imgDialog);
        final TextView tvMessage = view.findViewById(R.id.tcMessage);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.DialogFragmentTheme)
                .setTitle(R.string.profile_dialog_title)
                .setPositiveButton(R.string.profile_dialog_accept,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int i) {
                        mPresenter.updateImage(Uri.parse(urlLocal));
                        UtilsCommon.showSnackbar(contentMain,R.string.profile_message_imageUploading,
                                                 Snackbar.LENGTH_LONG);
                    }
                })
                .setNegativeButton(R.string.common_label_cancel,null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                int sizeImagePreview = getResources().getDimensionPixelSize(R.dimen.chat_size_img_preview);
                Bitmap bitmap = UtilsImage.reduceBitmap(getContext(),contentMain,
                                                        urlLocal,sizeImagePreview,sizeImagePreview);
                if (bitmap != null){
                    imgDialog.setImageBitmap(bitmap);
                }
                tvMessage.setText(R.string.profile_dialog_message);
            }
        });
        alertDialog.show();
    }

    @Override
    public void menuEditMode() {
        mCurrentMenuItem.setIcon(ContextCompat.getDrawable(getContext(),R.drawable.ic_check));
    }

    @Override
    public void menuNormalMode() {
        if (mCurrentMenuItem != null){
            mCurrentMenuItem.setEnabled(true);
            mCurrentMenuItem.setIcon(ContextCompat.getDrawable(getContext(),R.drawable.ic_pencil));
        }
    }

    @Override
    public void saveUsernameSuccess() {
        UtilsCommon.showSnackbar(contentMain,R.string.profile_message_userUpdated);
    }

    @Override
    public void updateImageSuccess(String photoUrl) {
        setImageProfile(photoUrl);
        UtilsCommon.showSnackbar(contentMain,R.string.profile_message_imageUpdated);
    }

    @Override
    public void setResultOK(String username,String photoUrl) {
        Intent data = new Intent();
        data.putExtra(User.USERNAME, username);
        data.putExtra(User.PHOTO_URL,photoUrl);
        ((AppCompatActivity)getActivity()).setResult(Activity.RESULT_OK,data);
    }

    @Override
    public void onErrorUpload(int resMsg) {
        UtilsCommon.showSnackbar(contentMain,resMsg);
    }

    @Override
    public void onError(int resMsg) {
        etUsername.requestFocus();
        etUsername.setError(getString(resMsg));
    }



}