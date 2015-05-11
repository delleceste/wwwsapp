package it.giacomos.android.wwwsapp.pager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import it.giacomos.android.wwwsapp.HelloWorldActivity;
import it.giacomos.android.wwwsapp.R;
import it.giacomos.android.wwwsapp.network.state.ViewType;

public class ActionBarManager
{
	
	public static final int FORECAST = 0;
	public  static final int RADAR = 1;
	public  static final int DAILY_OBS = 2;
	public  static final int LATEST_OBS = 3;
	public  static final int WEBCAM = 4;
	public static final int REPORT = 5;
	
	private ActionBarListItemNavigationListener mActionBarListItemNavigationListener;
	
	public int getType() 
	{
		return mType;
	}

	public ActionBarManager(HelloWorldActivity a)
	{
		mActivity = a;
		mActionBarListItemNavigationListener = new ActionBarListItemNavigationListener(mActivity);
	}

	/* called after restore instance state, initializes the application according
	 * to the saved state of the action bar in the previous execution.
	 */
	public void init(Bundle savedInstanceState, int forceDrawerItem) 
	{
		boolean actionBarTabs = false;
		ListView drawer = mActivity.getDrawerListView();
		int selectedDrawerItem = drawer.getCheckedItemPosition();
		if(selectedDrawerItem < 0)
			selectedDrawerItem = 0;
		
//		Log.e("ActionBarManager.init", "selected Drawer Item " + selectedDrawerItem + " force " + forceDrawerItem);
		/* avoid calling drawerItemChanged if selectedDrawerItem is 0 because
		 * drawerItemChanged has already been called by HelloWorldActivity.init at 
		 * this point.
		 */
		if(forceDrawerItem < 0 && selectedDrawerItem != 0) /* otherwise call drawerItemChange afterwards */
			drawerItemChanged(selectedDrawerItem);
		if(selectedDrawerItem == 0)
			actionBarTabs = true;
		else if(savedInstanceState == null)
			actionBarTabs = true;
		if(actionBarTabs)
		{
		//	ForecastTabbedFragment stl = (ForecastTabbedFragment) mActivity.getForecastFragment();
		//	int selectedTabIndex = stl.getSelectedPage();
		//    /* switch to correct tab */
		//	stl.setSelectedPage(selectedTabIndex);
			/* check the first item of the drawer */
		//	drawer.setItemChecked(0, true);
		}
		else
		{
			Spinner spinner = (Spinner) mActivity.findViewById(R.id.toolbar_spinner);
			boolean observations = (selectedDrawerItem == 2 || selectedDrawerItem == 3);
			if(observations)
			{
				int selected = savedInstanceState.getInt("spinnerPosition");
				mActionBarListItemNavigationListener.onItemSelected(null, null, selected, -1);
				spinner.setSelection(selected);
			} 
		}
		if(forceDrawerItem > 0) 
		{
			drawer.setItemChecked(forceDrawerItem, true);
			drawerItemChanged(forceDrawerItem);
		}
	}
	
	public void drawerItemChanged(int id)
	{
//		Log.e("ActionBarManager.drawerItemChanged", " id " + id);
		mSpinnerAdapter = null;
		Spinner spinner = (Spinner) mActivity.findViewById(R.id.toolbar_spinner);
		switch(id)
		{
		case 0:
			mType = FORECAST;
			spinner.setVisibility(View.GONE);
			mActivity.switchView(ViewType.HOME);
			break;

		case 1:
			mType = RADAR;
			mActivity.switchView(ViewType.RADAR);
			break;
			
		case 2:
			mType = DAILY_OBS;
			mSpinnerAdapter = ArrayAdapter.createFromResource(mActivity, R.array.dailyobs_text_items,
					android.R.layout.simple_spinner_dropdown_item);
			mActionBarListItemNavigationListener.setMode(ViewType.DAILY_TABLE);
			/* activity switchView is called by the listener + navigation callback */
			break;

		case 3:
			mType = LATEST_OBS;
			mSpinnerAdapter = ArrayAdapter.createFromResource(mActivity, R.array.latestobs_text_items,
					android.R.layout.simple_spinner_dropdown_item);
			mActionBarListItemNavigationListener.setMode(ViewType.LATEST_TABLE);
			/* activity switchView is called by the listener + navigation callback */
			break;
			
		case 4:
			mType = WEBCAM;
			mActivity.switchView(ViewType.WEBCAM);
			break;
		case 5:
			mType = REPORT;
			mActivity.switchView(ViewType.REPORT);
			break;
		case 6:
			mActivity.shareMeteoFVGApp();
			break;
		case 7:
			mActivity.openMeteoFVGUrl();

			break;
		}
		
		if(mType ==DAILY_OBS  || mType == LATEST_OBS)
			spinner.setVisibility(View.VISIBLE);
		else
			spinner.setVisibility(View.GONE);
		
		
		if(mSpinnerAdapter != null)
		{
			spinner.setAdapter(mSpinnerAdapter);
			spinner.setOnItemSelectedListener(mActionBarListItemNavigationListener);
		}
	}
	
	private HelloWorldActivity mActivity;
	private SpinnerAdapter mSpinnerAdapter;
	private int mType;
}
