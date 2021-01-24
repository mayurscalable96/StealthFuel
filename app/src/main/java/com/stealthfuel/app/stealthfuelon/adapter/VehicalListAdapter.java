package com.stealthfuel.app.stealthfuelon.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.stealthfuel.app.stealthfuelon.R;
import com.stealthfuel.app.stealthfuelon.geterseter.VehicalListItem;

import java.util.List;

public class VehicalListAdapter extends RecyclerView.Adapter<VehicalListAdapter.MyViewHolder> {

    private Context mContext;
    private List<VehicalListItem> vehicalListItems;


    public VehicalListAdapter(Context mContext, List<VehicalListItem> vehicalListItems) {
        this.mContext = mContext;
        this.vehicalListItems = vehicalListItems;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView vehicel_details;


        public MyViewHolder(View itemView) {
            super(itemView);

            vehicel_details =  itemView.findViewById(R.id.vehicel_details);

        }
    }

    @Override
    public VehicalListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_list_row, parent, false);
        return new VehicalListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VehicalListAdapter.MyViewHolder holder, int position) {
        VehicalListItem vehicalListItem = vehicalListItems.get(position);


        holder.vehicel_details.setText(vehicalListItem.getVehical_year() +" "+vehicalListItem.getVehical_make()
                + " " + vehicalListItem.getVehical_model()+ " " + vehicalListItem.getVehical_plateno() );


    }

    @Override
    public int getItemCount() {

        return vehicalListItems.size();
    }

}

