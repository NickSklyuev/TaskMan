package com.example.taskerapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by REstoreService on 05.06.15.
 */
public class ProcessListAdapter extends RecyclerView.Adapter<ProcessListAdapter.ViewHolder> {
    private ArrayList<ProcessDetailInfo> mDetailList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.textView2);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProcessListAdapter(ArrayList<ProcessDetailInfo> mDetailList) {
        this.mDetailList = mDetailList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProcessListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.process_list_ellement, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProcessDetailInfo localProcessDetailInfo = (ProcessDetailInfo)mDetailList.get(position);

        holder.mTextView.setText(localProcessDetailInfo.getLabel());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDetailList.size();
    }
}