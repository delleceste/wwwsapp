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
 
	int findItemData(String title)
	{
		for(int i = 0; i < getCount(); i++)
		{
			if(title.compareTo(this.getItem(i).name) == 0)
				return i;
		}
		return -1;
	}
	
	public void update(LayerItemData d)
	{
		int i = findItemData(d.name);
		if(i > -1)
			setData(d, i);
		else
			add(d);
		notifyDataSetChanged();
	}
	
	public void setData(LayerItemData d, int pos)
	{
		if(pos < this.getCount())
		{
			setData(d, pos);
			notifyDataSetChanged();
		}
	}
	
	@Override
	public View getView(int position, View itemView, ViewGroup parent) 
	{
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
		LayerItemData d = this.getItem(position);
		mViewHolder.title.setText(d.name);
		mViewHolder.desc.setText(d.short_desc);
		mViewHolder.image.setBackgroundDrawable(d.icon);
		if(d.flags == LayerItemFlags.LAYER_INSTALLED)
			mViewHolder.button.setText(R.string.delete);
		else if(d.flags == LayerItemFlags.LAYER_NOT_INSTALLED)
			mViewHolder.button.setText(R.string.install);
		else if(d.flags == LayerItemFlags.LAYER_UPGRADABLE)
			mViewHolder.button.setText(R.string.update);
			
			
		return itemView;
	}
}
