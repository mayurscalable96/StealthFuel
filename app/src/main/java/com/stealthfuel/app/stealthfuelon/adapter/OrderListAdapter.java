package com.stealthfuel.app.stealthfuelon.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.stealthfuel.app.stealthfuelon.activity.OrderList;
import com.stealthfuel.app.stealthfuelon.geterseter.OrderListItem;

import java.util.ArrayList;
import java.util.List;
import com.stealthfuel.app.stealthfuelon.R;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> implements Filterable {

    List<OrderListItem> orderListItems;
    List<OrderListItem> orderListItemFilterable;
    Context context;

    public OrderListAdapter(Context context, List<OrderListItem> orderListItems) {
        this.orderListItems = orderListItems;
        this.context = context;
        this.orderListItemFilterable = orderListItems;
    }

    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_row, parent, false);

        OrderListAdapter.ViewHolder viewHolder = new OrderListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.ViewHolder holder, int position) {
        OrderListItem orderListItem = orderListItemFilterable.get(position);
        holder.order_id.setText("#"+orderListItem.getOrder_id());
        holder.order_status.setText(orderListItem.getOrder_status());
        holder.order_address.setText(orderListItem.getOrder_address());
        holder.order_date.setText(orderListItem.getOrder_date() + " " + orderListItem.getOrder_time());
        holder.order_vehical.setText(orderListItem.getVehicle_make() + " " + orderListItem.getVehicle_model() + " "
                + orderListItem.getVehicle_color() + " " + orderListItem.getVehicle_plateno());
        holder.order_gastype.setText(orderListItem.getGas_type());

        if(orderListItem.getExtra_amount().equals("0")){
            holder.order_timeslot.setText(orderListItem.getTime_slot());
        }
        else{
            holder.order_timeslot.setText(orderListItem.getTime_slot()+" Extra amount is $"+orderListItem.getExtra_amount());
        }


    }

    @Override
    public int getItemCount() {
        return orderListItemFilterable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView order_id, order_status, order_address, order_date, order_vehical, order_gastype,order_timeslot;


        public ViewHolder(View view) {
            super(view);

            order_id = view.findViewById(R.id.order_id);
            order_status = view.findViewById(R.id.order_status);
            order_address = view.findViewById(R.id.order_address);
            order_date = view.findViewById(R.id.order_date);
            order_vehical = view.findViewById(R.id.order_vehical);
            order_gastype = view.findViewById(R.id.order_gastype);
            order_timeslot = view.findViewById(R.id.order_timeslot);

            view.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    ((OrderList) context).recycleClick(orderListItemFilterable.get(getAdapterPosition()));
                }
            });


        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    orderListItemFilterable = orderListItems;
                } else {
                    List<OrderListItem> filteredList = new ArrayList<>();
                    for (OrderListItem row : orderListItems) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getOrder_id().toLowerCase().contains(charString.toLowerCase())
                                || row.getOrder_status().toLowerCase().contains(charString.toLowerCase())
                                || row.getOrder_address().toLowerCase().contains(charString.toLowerCase())
                                || row.getOrder_date().toLowerCase().contains(charString.toLowerCase())
                                || row.getVehicle_make().toLowerCase().contains(charString.toLowerCase())
                                || row.getVehicle_model().toLowerCase().contains(charString.toLowerCase())
                                || row.getVehicle_color().toLowerCase().contains(charString.toLowerCase())
                                || row.getVehicle_plateno().toLowerCase().contains(charString.toLowerCase())
                                || row.getGas_type().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    orderListItemFilterable = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = orderListItemFilterable;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                orderListItemFilterable = (ArrayList<OrderListItem>) filterResults.values;


                notifyDataSetChanged();
            }
        };
    }
}
