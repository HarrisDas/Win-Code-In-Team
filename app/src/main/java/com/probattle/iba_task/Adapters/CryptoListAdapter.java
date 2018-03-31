package com.probattle.iba_task.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.probattle.iba_task.CryptoModel;
import com.probattle.iba_task.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 31/03/2018.
 */

public class CryptoListAdapter extends RecyclerView.Adapter<CryptoListAdapter.CryptoViewHolder> implements Filterable{

    List<CryptoModel> list=new ArrayList<CryptoModel>();
    List<CryptoModel> filteredArrayList=new ArrayList<CryptoModel>();

    Context context;
    private AdapterView.OnItemClickListener onItemClickListener;//click listeer

    public CryptoListAdapter(List<CryptoModel> list, Context context, AdapterView.OnItemClickListener onItemClickListener) {
        this.list = list;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        filteredArrayList=list;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredArrayList = list;
                } else {
                    List<CryptoModel> filteredList = new ArrayList<>();
                    for (CryptoModel row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ) {
                         //   Log.e("bookings_adapter",row.getCourse_name());
                            filteredList.add(row);
                        }
                    }

                    filteredArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredArrayList = (ArrayList<CryptoModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    @Override
    public CryptoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypto_row, parent, false);

        CryptoViewHolder viewHolder = new CryptoViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CryptoViewHolder holder, int position) {
//        Log.d("adapter", "onBindViewHolder: "+list.get(position).getDescription());
        holder.CurrencyName.setText(filteredArrayList.get(position).getName());
        holder.price.setText(filteredArrayList.get(position).getPrice_usd());

        if(filteredArrayList.get(position).getLike()){
            holder.likeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
           holder.likeButton.setClickable(false);

        }else{
            holder.likeButton.setImageResource(R.drawable.ic_thumb_up_light_24dp);

        }
    }

    @Override
    public int getItemCount() {
        return filteredArrayList.size();
    }

    public class CryptoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView CurrencyName;
        TextView price;
//        TextView like;
        ImageView likeButton;

      public CryptoViewHolder(View itemView) {
          super(itemView);
          CurrencyName =itemView.findViewById(R.id.currenncyName);
          price =itemView.findViewById(R.id.price);
          likeButton =itemView.findViewById(R.id.like_button);
//          like =itemView.findViewById(R.id.like_text);
          likeButton.setOnClickListener(this);

      }

        @Override
        public void onClick(View view) {
            likeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
            likeButton.setClickable(false);
            onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());

        }
    }
}
