package com.stealthfuel.app.stealthfuelon.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthfuel.app.stealthfuelon.R;
import com.stealthfuel.app.stealthfuelon.geterseter.PromotionsItem;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder> {

        List<PromotionsItem> promotionsItems;
        Context context;

    public PromotionAdapter(Context context, List<PromotionsItem> promotionsItems) {
            this.promotionsItems = promotionsItems;
            this.context = context;


            }

    @Override
    public PromotionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotions_list_row, parent, false);

           PromotionAdapter.ViewHolder viewHolder = new PromotionAdapter.ViewHolder(view);
            return viewHolder;
    }

    @Override
    public void onBindViewHolder(PromotionAdapter.ViewHolder holder, int position) {
            PromotionsItem promotionsItem = promotionsItems.get(position);
            holder.allrewards.setText(promotionsItem.getHistory());


      }

    @Override
    public int getItemCount() {
            return promotionsItems.size();
     }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView allrewards;


        public ViewHolder(View view) {
            super(view);

            allrewards = view.findViewById(R.id.allrewards);

        }
    }



    }

