package prof.magnitos.speedytask.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/*import com.rechild.advancedtaskkillerpro.R;*/import prof.magnitos.speedytask.R;
import prof.magnitos.speedytask.components.ProcessDetailInfo;

public class TaskListAdapters{
	public static class ListViewItem{
		public ProcessDetailInfo detailProcess;
		public ImageView icon;
		public ImageView iconCheck;
		public TextView text_name;
	}

	public static final class ProcessListAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		private boolean mIsCheckBoxEnable = true;
		private ArrayList<ProcessDetailInfo> mList;

		public ProcessListAdapter(Context paramContext, ArrayList<ProcessDetailInfo> paramArrayList){
			mInflater = LayoutInflater.from(paramContext);
			mList = paramArrayList;
		}

		public boolean getCheckBoxEnable(){
			return mIsCheckBoxEnable;
		}

		public int getCount(){
			return mList.size();
		}

		public Object getItem(int paramInt){
			return mList.get(paramInt);
		}

		public long getItemId(int paramInt){
			return paramInt;
		}

		public View getView(int paramInt, View paramView, ViewGroup paramViewGroup){
			ProcessDetailInfo localProcessDetailInfo = (ProcessDetailInfo)mList.get(paramInt);
			TaskListAdapters.ListViewItem localListViewItem;
			if (paramView == null){
				paramView = this.mInflater.inflate(R.layout.list_main, null);
				localListViewItem = new TaskListAdapters.ListViewItem();
				localListViewItem.icon = ((ImageView)paramView.findViewById(R.id.list_icon));
				localListViewItem.text_name = ((TextView)paramView.findViewById(R.id.list_name));
				paramView.setTag(localListViewItem);
			}else{
				localListViewItem = (TaskListAdapters.ListViewItem)paramView.getTag();
			}
			
			localListViewItem.detailProcess = localProcessDetailInfo;
			if (mIsCheckBoxEnable)
				localListViewItem.iconCheck = ((ImageView)paramView.findViewById(R.id.list_iconCheck));
			paramView.setVisibility(View.VISIBLE);
			paramView.setMinimumHeight(/*Setting.ITEM_HEIGHT*/36);
			Drawable localDrawable = localProcessDetailInfo.getIcon();
			if (localDrawable == null)
				localListViewItem.icon.setImageResource(android.R.drawable.ic_menu_info_details);
			localListViewItem.icon.setImageDrawable(localDrawable);
			localListViewItem.text_name.setText(localProcessDetailInfo.getLabel());
			if (mIsCheckBoxEnable){
				localListViewItem.iconCheck.setVisibility(View.VISIBLE);
				if (localListViewItem.detailProcess.Importance > 300)
					localListViewItem.text_name.setTextColor(Color.BLACK);
				else
					localListViewItem.text_name.setTextColor(Color.BLUE);
				if (!localProcessDetailInfo.getSelected())
					localListViewItem.iconCheck.setImageResource(R.drawable.btn_check_off);
				else
					localListViewItem.iconCheck.setImageResource(R.drawable.btn_check_on);
			}

			return paramView;
		}

		public void setCheckBoxEnable(boolean paramBoolean){
			mIsCheckBoxEnable = paramBoolean;
		}
	}
}
