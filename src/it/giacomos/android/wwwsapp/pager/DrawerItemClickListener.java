package it.giacomos.android.wwwsapp.pager;


import it.giacomos.android.wwwsapp.WWWsAppActivity;
import it.giacomos.android.wwwsapp.R;
import it.giacomos.android.wwwsapp.observations.MapMode;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DrawerItemClickListener implements ListView.OnItemClickListener 
{
	public DrawerItemClickListener(WWWsAppActivity a)
	{
		super();
		mOsmerActivity = a;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		ListView drawerListView = mOsmerActivity.getDrawerListView();
		//Log.e("onItemClick drawer listener", "WAS clicekd at pos " + drawerListView.getCheckedItemPosition());
		String[] drawerItems = mOsmerActivity.getDrawerItems();
		if(position < 6) /* share and meteo.fvg link excluded */
		{
			drawerListView.setItemChecked(position, true);
			mOsmerActivity.setTitle(drawerItems[position]);
		}
		else
		{
			int pos = 0;
			if(mOsmerActivity.getDisplayedFragment() == 0)
				pos = 0;
			else
			{
				MapMode mm = mOsmerActivity.getMapFragment().getMode().currentMode;
				if(mm == MapMode.DAILY_OBSERVATIONS)
					pos = 2;
				else if(mm == MapMode.RADAR)
					pos = 1;
				else if(mm == MapMode.LATEST_OBSERVATIONS)
					pos = 3;
				else if(mm == MapMode.WEBCAM)
					pos = 4;
				else if(mm == MapMode.REPORT)
					pos = 5;
			}
			drawerListView.setItemChecked(pos, true);
			mOsmerActivity.setTitle(drawerItems[pos]);
		}

		DrawerLayout drawerLayout = (DrawerLayout) mOsmerActivity.findViewById(R.id.drawer_layout);
		drawerLayout.closeDrawer(drawerListView);
		/* calls switchView on WWWsAppActivity with the position passed */
		mOsmerActivity.getActionBarManager().drawerItemChanged(position);
	}

	private WWWsAppActivity mOsmerActivity;
}