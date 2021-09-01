package com.example.brigadauniversitaria.mainModule.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.common.pojo.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private List<User> mUsers;
    private Context mContext;
    private OnItemClickListener mListener;

    public RequestAdapter(List<User> mUsers,OnItemClickListener mListener) {
        this.mUsers = mUsers;
        this.mListener = mListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request,
                                                                        parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder,int position) {
        User user = mUsers.get(position);

        holder.setOnClickListener(user, mListener);

        holder.tvNombre.setText(user.getUsername());
        holder.tvEmail.setText(user.getEmail());

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.drawable.ic_sad)
                .placeholder(R.drawable.ic_tongue);
        Glide.with(mContext)
                .load(user.getPhotoUrl())
                .apply(options)
                .into(holder.imgFoto);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void add(User user){
        if (!mUsers.contains(user)){
            mUsers.add(user);
            notifyItemInserted(mUsers.size()-1);
        }else {
            update(user);
        }
    }

    public void update(User user) {
        if (mUsers.contains(user)){
            int index = mUsers.indexOf(user);
            mUsers.set(index, user);
            notifyItemChanged(index);
        }
    }

    public void remove(User user){
        if (mUsers.contains(user)){
            int index = mUsers.indexOf(user);
            mUsers.remove(index);
            notifyItemRemoved(index);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgFoto)
        CircleImageView imgFoto;
        @BindView(R.id.tvNombre)
        TextView tvNombre;
        @BindView(R.id.tvEmail)
        TextView tvEmail;
        @BindView(R.id.btnAdd)
        AppCompatImageButton btnAdd;
        @BindView(R.id.btnDeny)
        AppCompatImageButton btnDeny;

        ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void setOnClickListener(User user, OnItemClickListener listener){
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAcceptRequest(user);
                }
            });

            btnDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDenyRequest(user);
                }
            });
        }
    }
}
