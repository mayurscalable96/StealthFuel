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
import com.stealthfuel.app.stealthfuelon.geterseter.NotificationListItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> implements Filterable {

        List<NotificationListItem> notificationListItems;
        List<NotificationListItem> notificationListItemsFilterable;
        Context context;

    public NotificationAdapter(Context context, List<NotificationListItem> notificationListItems) {
        this.notificationListItems = notificationListItems;
        this.context = context;
        this.notificationListItemsFilterable = notificationListItems;

    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_row, parent, false);

            NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(view);
            return viewHolder;
            }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, int position) {
            NotificationListItem notificationListItem = notificationListItemsFilterable.get(position);
            holder.notification_text.setText(notificationListItem.getNotification_text());

            }

    @Override
    public int getItemCount() {
            return notificationListItemsFilterable.size();
            }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notification_text;


        public ViewHolder(View view) {
            super(view);

            notification_text = view.findViewById(R.id.notification_text);


        }
    }


        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        notificationListItemsFilterable = notificationListItems;
                    } else {
                        List<NotificationListItem> filteredList = new ArrayList<>();
                        for (NotificationListItem row : notificationListItems) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getNotification_text().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        notificationListItemsFilterable = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = notificationListItemsFilterable;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    notificationListItemsFilterable = (ArrayList<NotificationListItem>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }
