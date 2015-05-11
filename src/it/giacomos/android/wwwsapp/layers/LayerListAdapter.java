package it.giacomos.android.wwwsapp.layers;

import java.util.ArrayList;

import it.giacomos.android.wwwsapp.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class LayerListAdapter extends ArrayAdapter<LayerItemData> 
{
	private final Context context;


	static class ViewHolder {
		public TextView title, desc;
		public Button button;
		public ImageView image;
	}

	public LayerListAdapter(Context context) {
		super(context, R.layout.layer_list_item, new ArrayList<LayerItemData>());
		this.context = context;
	}
 
	public void addData(LayerItemData d)
	{
		Log.e("LayerListAdapter.addData", "added data " + d.title);
		this.add(d);
		this.notifyDataSetChanged();
	}
	
	public void setData(LayerItemData d, int pos)
	{
		if(pos < this.getCount())
		{
			this.setData(d, pos);
			this.notifyDataSetChanged();
		}
	}
	
	@Override
	public View getView(int position, View itemView, ViewGroup parent) 
	{
		Log.e("LayerListAdapter.getView", "enter@!");
		ViewHolder  mViewHolder = null; 

		if(itemView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = inflater.inflate(R.layout.layer_list_item, parent, false);
			Log.e("LayerListAdapter.getView", "inflated!");
			mViewHolder = new ViewHolder();
			mViewHolder.title  = (TextView) itemView.findViewById(R.id.title);
			mViewHolder.desc = (TextView) itemView.findViewById(R.id.description);
			mViewHolder.image = (ImageView) itemView.findViewById(R.id.icon);
			mViewHolder.button = (Button) itemView.findViewById(R.id.button);

			itemView.setTag(mViewHolder);


		}
		else
		{
			mViewHolder  = (ViewHolder) itemView.getTag();
		}

		/* updates, if present */

		return itemView;
	}
}
