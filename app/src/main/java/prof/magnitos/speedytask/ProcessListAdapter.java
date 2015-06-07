package prof.magnitos.speedytask;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by REstoreService on 05.06.15.
 */
public class ProcessListAdapter extends RecyclerView.Adapter<ProcessListAdapter.ViewHolder> {
    private ArrayList<ProcessDetailInfo> mDetailList;

    public OnItemClickListener mItemClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView label;
        public ImageView icon;
        public CheckBox selector;
        ProcessDetailInfo localProcessDetailInfo;
        public ViewHolder(final View v) {
            super(v);

            label = (TextView) v.findViewById(R.id.list_name);
            icon = (ImageView) v.findViewById(R.id.list_icon);
            selector = (CheckBox) v.findViewById(R.id.checkBox);
            v.setOnClickListener(this);

            //selector.setChecked(localProcessDetailInfo.getSelected());

            selector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        localProcessDetailInfo.setSelected(true);
                    } else {
                        localProcessDetailInfo.setSelected(false);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition(),localProcessDetailInfo); //OnItemClickListener mItemClickListener;
            }

            if (selector.isChecked()){
                selector.setChecked(false);
            }else{
                selector.setChecked(true);
            }

        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position, ProcessDetailInfo localProcessDetailInfo);
    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
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
                .inflate(R.layout.list_main, parent, false);



        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProcessDetailInfo localProcessDetailInfo = (ProcessDetailInfo)mDetailList.get(position);

        holder.localProcessDetailInfo = localProcessDetailInfo;

        holder.label.setText(localProcessDetailInfo.getLabel());
        Drawable localDrawable = localProcessDetailInfo.getIcon();
        if (localDrawable == null)
            holder.icon.setImageResource(android.R.drawable.ic_menu_info_details);
        holder.icon.setImageDrawable(localDrawable);

        //holder.selector.clearAnimation();
        holder.selector.setChecked(localProcessDetailInfo.getSelected());


        if (localProcessDetailInfo.Importance > 300)
            holder.label.setTextColor(Color.BLACK);
        else
            holder.label.setTextColor(Color.BLUE);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDetailList.size();
    }
}