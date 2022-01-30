package com.example.brigadauniversitaria.chatModule.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.brigadauniversitaria.Fragments.LocalesFragment;
import com.example.brigadauniversitaria.NavigatorActivity;
import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.chatModule.ChatPresenter;
import com.example.brigadauniversitaria.chatModule.ChatPresenterClass;
import com.example.brigadauniversitaria.chatModule.view.adapters.ChatAdapter;
import com.example.brigadauniversitaria.chatModule.view.adapters.OnItemClickListener;
import com.example.brigadauniversitaria.common.Constants;
import com.example.brigadauniversitaria.common.pojo.Message;
import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.common.utils.UtilsCommon;
import com.example.brigadauniversitaria.common.utils.UtilsImage;
import com.example.brigadauniversitaria.mainModule.MainPresenterClass;
import com.example.brigadauniversitaria.mainModule.view.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatsFragment extends Fragment implements OnItemClickListener,ChatView {

    @BindView(R.id.imgPhoto)
    CircleImageView imgPhoto;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.contentMain)
    CoordinatorLayout contentMain;
    @BindView(R.id.etMessage)
    AppCompatEditText etMessage;

    Unbinder unbinder;

    private ChatAdapter mAdapter;
    private ChatPresenter mPresenter;
    private Message messageSelected;
    private static final String ARG_PARAM1 = "uid";
    private static final String ARG_PARAM2 = "username";
    private static final String ARG_PARAM3 = "email";
    private static final String ARG_PARAM4 = "photoUrl";
    private User mUser;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ChatsFragment chatsFragment;


    private String uid;
    private String username;
    private String email;
    private String photoUrl;

    public ChatsFragment() {
        // Required empty public constructor
    }

    public static ChatsFragment newInstance(String uid,String username,String email,String photoUrl) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,uid);
        args.putString(ARG_PARAM2,username);
        args.putString(ARG_PARAM2,email);
        args.putString(ARG_PARAM2,photoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(ARG_PARAM1);
            username = getArguments().getString(ARG_PARAM2);
            email = getArguments().getString(ARG_PARAM3);
            photoUrl = getArguments().getString(ARG_PARAM4);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats,container,false);
        unbinder = ButterKnife.bind(this,view);
        mPresenter = new ChatPresenterClass(this);
        mPresenter.onCreate();
        mUser = mPresenter.getCurrentUser();
        configAdapter();
        configRecyclerView();
        configToolbar(uid,username,email,photoUrl);


        return view;
    }

    private void configAdapter() {
        mAdapter = new ChatAdapter(new ArrayList<Message>(),this);
    }

    private void configRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    private void configToolbar(String uid,String username,String email,String photoUrl) {

        mPresenter.setupFriend(uid, email);

        tvName.setText(username);
        tvStatus.setVisibility(View.VISIBLE);

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.ic_emoticon_happy)
                .centerCrop();
        Glide.with(this)
                .asBitmap()
                .load(photoUrl)
                .apply(options)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e,Object model,Target<Bitmap> target,boolean isFirstResource) {
                        imgPhoto.setImageDrawable(ContextCompat.getDrawable(getContext(),
                                                                            R.drawable.ic_emoticon_sad));
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource,Object model,Target<Bitmap> target,DataSource dataSource,boolean isFirstResource) {
                        imgPhoto.setImageBitmap(resource);
                        return true;
                    }
                })
                .into(imgPhoto);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onBackPressed() {
        Intent intent = new Intent(getActivity(), NavigatorActivity.class);
        startActivity(intent);
        if (UtilsCommon.hasMaterialDesign()) {
            ((AppCompatActivity)getActivity()).finishAfterTransition();
        } else {
            ((AppCompatActivity)getActivity()).finish();
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
    public void onStatusUser(boolean connected,long lastConnection) {
        if (connected){
            tvStatus.setText(R.string.chat_status_connected);
        }else {
            tvStatus.setText(getString(R.string.chat_status_last_connection,(new SimpleDateFormat(
                    "dd-MM-yyyy - HH:mm",Locale.ROOT).format(new Date(lastConnection)))));
        }
    }

    @Override
    public void onError(int resMsg) {
        UtilsCommon.showSnackbar(contentMain,resMsg);
    }

    @Override
    public void onMessageReceived(Message msg) {
        mAdapter.add(msg);
        recyclerView.scrollToPosition(mAdapter.getItemCount() -1);
    }

    @Override
    public void openDialogPreview(Intent data) {
        final String urlLocal = data.getDataString();

        final ViewGroup nullParent = null;
        View view = getLayoutInflater().inflate(R.layout.dialog_image_upload_preview,nullParent);
        final CircleImageView imgDialog = view.findViewById(R.id.imgDialog);
        final TextView tvMessage = view.findViewById(R.id.tcMessage);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext(),R.style.DialogFragmentTheme)
                .setTitle(R.string.chat_dialog_sendImage_title)
                .setPositiveButton(R.string.chat_dialog_sendImage_accept,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int i) {
                        mPresenter.sendImage(ChatsFragment.this,Uri.parse(urlLocal));

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
                tvMessage.setText(String.format(Locale.ROOT,
                                                getString(R.string.chat_dialog_sendImage_message),tvName.getText()));
            }
        });
        alertDialog.show();
    }

    /*
    * Click events
    * */

    @OnClick(R.id.btnSendMessage)
    public void sendMessage(){
        if(UtilsCommon.validateMessage(etMessage)) {
            mPresenter.sendMessage(etMessage.getText().toString().trim());
            etMessage.setText("");
        }
    }

    @Override
    public void onImageLoaded() {

    }

    @Override
    public void onClickImage(Message message) {

    }
}