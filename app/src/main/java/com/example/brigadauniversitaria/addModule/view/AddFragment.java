package com.example.brigadauniversitaria.addModule.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.addModule.AddPresenter;
import com.example.brigadauniversitaria.addModule.AddPresenterClass;
import com.example.brigadauniversitaria.common.utils.UtilsCommon;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AddFragment extends DialogFragment implements DialogInterface.OnShowListener, AddView {

    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.contentMain)
    FrameLayout contentMain;

    private Button positivButton;

    private AddPresenter mPresenter;

    Unbinder unbinder;

    public AddFragment() {
        // Required empty public constructor
        mPresenter = new AddPresenterClass(this);
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.addFriend_title)
                .setPositiveButton(R.string.common_label_accept, null)
                .setNeutralButton(R.string.common_label_cancel,null);
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add, null);
        builder.setView(view);
        unbinder = ButterKnife.bind(this,view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }



    @Override
    public void onShow(DialogInterface dialogInterface) {
        final AlertDialog dialog = (AlertDialog)getDialog();
        if (dialog != null){
            positivButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);

            positivButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (UtilsCommon.validateEmail(getActivity(),etEmail)) {
                        mPresenter.addFriend(etEmail.getText().toString().trim());
                    }
                }
            });
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        mPresenter.onShow();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void enableUIElements() {
        etEmail.setEnabled(true);
        positivButton.setEnabled(true);
    }

    @Override
    public void disableUIElements() {
        etEmail.setEnabled(false);
        positivButton.setEnabled(false);
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
    public void friendAdded() {
        Toast.makeText(getActivity(), R.string.addFriend_message_request_dispatched,Toast.LENGTH_SHORT)
                .show();
        dismiss();
    }

    @Override
    public void friendNotAdded() {
        etEmail.setError(getString(R.string.addFriend_error_message));
        etEmail.requestFocus();

    }
}