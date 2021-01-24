package com.stealthfuel.app.stealthfuelon.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.stealthfuel.app.stealthfuelon.R;
import com.stealthfuel.app.stealthfuelon.activity.Order;
import com.stealthfuel.app.stealthfuelon.geterseter.TimeSlotListItem;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> implements Filterable {

    private List<TimeSlotListItem> timeSlotListItems;
    private List<TimeSlotListItem> timeSlotListItemsFilterable;
    Context context;
    private View view1;

    public TimeSlotAdapter(Context context, List<TimeSlotListItem> timeSlotListItems) {
        this.timeSlotListItems = timeSlotListItems;
        this.context = context;
        this.timeSlotListItemsFilterable = timeSlotListItems;

    }

    @Override
    public TimeSlotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_row, parent, false);

        TimeSlotAdapter.ViewHolder viewHolder = new TimeSlotAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TimeSlotAdapter.ViewHolder holder, int position) {
        TimeSlotListItem timeSlotListItem = timeSlotListItemsFilterable.get(position);

        holder.timeslotdetail.setText(timeSlotListItem.getTimeSlotAmount());


    }

    @Override
    public int getItemCount() {
        return timeSlotListItemsFilterable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeslotdetail;


        public ViewHolder(View view) {
            super(view);

            timeslotdetail = view.findViewById(R.id.timeslotdetail);
            view1  = view.findViewById(android.R.id.content);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((Order) context).recycleClick(timeSlotListItemsFilterable.get(getAdapterPosition()));
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
                    timeSlotListItemsFilterable = timeSlotListItems;
                } else {
                    List<TimeSlotListItem> filteredList = new ArrayList<>();

                    for (TimeSlotListItem row : timeSlotListItems) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getDay().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }

                    }

                    timeSlotListItemsFilterable = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = timeSlotListItemsFilterable;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                timeSlotListItemsFilterable = (ArrayList<TimeSlotListItem>) filterResults.values;
                if(timeSlotListItemsFilterable.size()==0){
                    ((Order) context).showSnackbar();
                }
                notifyDataSetChanged();
            }
        };
    }
}

