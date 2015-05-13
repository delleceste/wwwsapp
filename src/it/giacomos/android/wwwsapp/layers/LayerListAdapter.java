package it.giacomos.android.wwwsapp.layers;

import java.util.ArrayList;

import it.giacomos.android.wwwsapp.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class LayerListAdapter extends ArrayAdapter<LayerItemData> implements OnClickListener 
{
	private final Context context;

	public static final int ACTION_DOWNLOAD = 0;
	public static final int ACTION_CANCEL_DOWNLOAD = 0;
	public static final int ACTION_REMOVE = 0;

	static class ViewHolder {
		public TextView title, desc;
		public Button button;
		public ImageView image;
	}

	public LayerListAdapter(Context context) {
		super(context, R.layout.layer_list_item, new ArrayList<LayerItemData>());
		this.context = context;
	}
 
	LayerItemData findItemData(String name)
	{
		for(int i = 0; i < getCount(); i++)
		{
			LayerItemData d = getItem(i);
			if(name.compareTo(d.name) == 0)
				return d;
		}
		return null;
	}
	
	public void update(LayerItemData d)
	{
		LayerItemData otherD = findItemData(d.name);
		if(otherD != null)
		{
			otherD = d;
			notifyDataSetChanged();
		}
		else
		{
			add(d);
		}
	}
	
	@Override
	public View getView(int position, View itemView, ViewGroup parent) 
	{
		Log.e("LayerListAdapter.getVIew", "ENTER");
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
		mViewHolder.title.setText(d.title);
		mViewHolder.desc.setText(d.short_desc);
		Log.e("LayerListAdapter.getVIew", "setBacground drawaw " + d.icon.getBitmap().getByteCount());
		mViewHolder.image.setBackgroundDrawable(d.icon);
		if(d.flags == LayerItemFlags.LAYER_INSTALLED)
			mViewHolder.button.setText(R.string.delete);
		else if(d.flags == LayerItemFlags.LAYER_NOT_INSTALLED)
			mViewHolder.button.setText(R.string.install);
		else if(d.flags == LayerItemFlags.LAYER_UPGRADABLE)
			mViewHolder.button.setText(R.string.update);
			
		mViewHolder.button.setOnClickListener(this);
			
		return itemView;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
