package com.example.brigadauniversitaria.mainModule.view;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.mainModule.MainPresenter;
import com.example.brigadauniversitaria.mainModule.MainPresenterClass;
import com.example.brigadauniversitaria.mainModule.view.MainView;
import com.example.brigadauniversitaria.mainModule.view.adapters.OnItemClickListener;
import com.example.brigadauniversitaria.mainModule.view.adapters.RequestAdapter;
import com.example.brigadauniversitaria.mainModule.view.adapters.UserAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainFragment extends Fragment implements OnItemClickListener, MainView {
    @BindView(R.id.rvRequests)
    RecyclerView rvRequests;
    @BindView(R.id.rvUsers)
    RecyclerView rvUsers;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.contentMain)
    CoordinatorLayout contentMain;
    Unbinder unbinder;
    private UserAdapter mUserAdapter;
    private RequestAdapter mRequestAdapter;
    private MainPresenter mPresenter;

    private User mUser;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_chat,container,false);
        unbinder = ButterKnife.bind(this,view);
        mPresenter = new MainPresenterClass(this);
        mPresenter.onCreate();
        mUser = mPresenter.getCurrentUser();
        configAdapter();
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUsers.setAdapter(mUserAdapter);
        rvRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRequests.setAdapter(mRequestAdapter);

        return view;
    }

    private void configAdapter() {
        mUserAdapter = new UserAdapter(new ArrayList<>(),this);
        mRequestAdapter = new RequestAdapter(new ArrayList<>(),this);
    }





    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
        clearNotifycation();
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

    private void clearNotifycation(){
        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        if (notificationManager != null){
            notificationManager.cancelAll();
        }
    }

    /*
     * main view
     * */

    @Override
    public void friendAdded(User user) {
        mUserAdapter.add(user);
    }

    @Override
    public void friendUpdate(User user) {
        mUserAdapter.update(user);
    }

    @Override
    public void friendRemove(User user) {
        mUserAdapter.remove(user);
    }

    @Override
    public void requestAdded(User user) {
        mRequestAdapter.add(user);
    }

    @Override
    public void requestUpdate(User user) {
        mRequestAdapter.update(user);
    }

    @Override
    public void requestRemove(User user) {
        mRequestAdapter.remove(user);
    }

    @Override
    public void showRequestAccepted(String username) {
        Snackbar.make(contentMain,getString(R.string.main_message_request_accepted,username),
                      Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showRequestDenied() {
        Snackbar.make(contentMain,R.string.main_message_request_denied,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFriendRemove() {
        Snackbar.make(contentMain,R.string.main_message_user_removed,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(int resMsg) {
        Snackbar.make(contentMain, resMsg, Snackbar.LENGTH_LONG).show();
    }
    /*
     * OnItemClickListener
     * */

    @Override
    public void onItemClick(User user) {

    }

    @Override
    public void onItemLongClick(final User user) {
        new AlertDialog.Builder(getActivity(),R.style.DialogFragmentTheme)
                .setTitle(getString(R.string.main_dialog_title_confirmDelete))
                .setMessage(String.format(Locale.ROOT,getString(R.string.main_dialog_message_confirmDelete),
                                          user.getUsernameValid()))
                .setPositiveButton(R.string.main_dialog_accept,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int which) {
                        mPresenter.removeFriend(user.getUid());
                    }
                })
                .setNegativeButton(R.string.common_label_cancel, null)
                .show();
    }

    @Override
    public void onAcceptRequest(User user) {
        mPresenter.acceptRequest(user);
    }

    @Override
    public void onDenyRequest(User user) {
        mPresenter.denyRequest(user);
    }

}