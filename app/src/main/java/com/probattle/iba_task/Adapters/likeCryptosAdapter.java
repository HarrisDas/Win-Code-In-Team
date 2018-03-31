package com.probattle.iba_task.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.probattle.iba_task.CryptoModel;
import com.probattle.iba_task.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 31/03/2018.
 */

public class likeCryptosAdapter extends RecyclerView.Adapter<likeCryptosAdapter.CryptoViewHolder>{

    List<CryptoModel> list=new ArrayList<CryptoModel>();
    Context context;
    private AdapterView.OnItemClickListener onItemClickListener;//click listeer

    public likeCryptosAdapter(List<CryptoModel> list, Context context, AdapterView.OnItemClickListener onItemClickListener) {
        this.list = list;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public likeCryptosAdapter.CryptoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypto_row_like, parent, false);

        likeCryptosAdapter.CryptoViewHolder viewHolder = new likeCryptosAdapter.CryptoViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(likeCryptosAdapter.CryptoViewHolder holder, int position) {
//        Log.d("adapter", "onBindViewHolder: "+list.get(position).getDescription());
        holder.CurrencyName.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice_usd());


//        if(list.get(position).getLike()){
//            holder.likeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
//            holder.likeButton.setClickable(false);
//
//        }else{
//            holder.likeButton.setImageResource(R.drawable.ic_thumb_up_light_24dp);
//
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CryptoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView CurrencyName;
        TextView price;

//        TextView like;
//        ImageView likeButton;

        public CryptoViewHolder(View itemView) {
            super(itemView);
            CurrencyName =itemView.findViewById(R.id.currenncyName);
            price =itemView.findViewById(R.id.price);

//            likeButton =itemView.findViewById(R.id.like_button);
//            like =itemView.findViewById(R.id.like_text);
//            likeButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
//            likeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
//            likeButton.setClickable(false);
            onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());

        }
    }

}