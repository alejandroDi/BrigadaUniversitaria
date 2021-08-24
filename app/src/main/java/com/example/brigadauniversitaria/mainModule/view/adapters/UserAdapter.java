package com.example.brigadauniversitaria.mainModule.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> mUser;
    private Context mContext;
    private OnItemClickListener mListener;

    public UserAdapter(List<User> mUser,OnItemClickListener mListener) {
        this.mUser = mUser;
        this.mListener = mListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,
                                                                     parent,false);
        mContext = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder,int position) {
        User user = mUser.get(position);

        holder.setClickListener(user, mListener);

        holder.nombre.setText(user.getUsernameValid());

        int messageUnread = user.getMessagesUnread();
        if (messageUnread > 0){
            String countStr = messageUnread > 99?
                    mContext.getString(R.string.main_item_max_messageUnread) : String.valueOf(messageUnread);
            holder.chat_cont.setText(countStr);
            holder.chat_cont.setVisibility(View.VISIBLE);
        }else {
            holder.chat_cont.setVisibility(View.GONE);
        }

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
        return mUser.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgFoto)
        CircleImageView imgFoto;
        @BindView(R.id.nombre)
        TextView nombre;
        @BindView(R.id.chat_cont)
        TextView chat_cont;

        private View view;

        ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.view = itemView;
        }

        private void setClickListener(User user,OnItemClickListener listener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(user);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(user);
                    return true;
                }
            });

        }
    }
}
