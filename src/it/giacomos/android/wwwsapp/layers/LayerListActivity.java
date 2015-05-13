package it.giacomos.android.wwwsapp.layers;

import java.util.ArrayList;
import java.util.Locale;

import it.giacomos.android.wwwsapp.MyAlertDialogFragment;
import it.giacomos.android.wwwsapp.R;
import it.giacomos.android.wwwsapp.R.id;
import it.giacomos.android.wwwsapp.R.layout;
import it.giacomos.android.wwwsapp.network.NetworkStatusMonitor;
import it.giacomos.android.wwwsapp.network.NetworkStatusMonitorListener;
import it.giacomos.android.wwwsapp.network.Data.DataPoolCacheUtils;
import it.giacomos.android.wwwsapp.network.state.Urls;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

/**
 * An activity representing a list of Layers. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link LayerDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link LayerListFragment} and the item details (if present) is a
 * {@link LayerDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link LayerListFragment.Callbacks} interface to listen for item selections.
 */
public class LayerListActivity extends Activity implements
LayerListFragment.Callbacks, LayerFetchTaskListener,
NetworkStatusMonitorListener
{

	public static final String CACHE_LIST_DIR = "layerlistcache/";
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private DataPoolCacheUtils mDataCache;
	private LayerFetchTask mLayerFetchTask;
	private NetworkStatusMonitor m_networkStatusMonitor;
	LayerListAdapter mLayerListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mDataCache = new DataPoolCacheUtils();
		mDataCache.initDir("layerlistcache", this);
		mLayerListAdapter = new LayerListAdapter(this);

		setContentView(R.layout.activity_layer_list);
		
		((LayerListFragment) getFragmentManager().findFragmentById(
				R.id.layer_list)).setActivateOnItemClick(true);


		/* load the layers cached locally */
		Loader loader = new Loader();
		ArrayList<LayerItemData> cachedData = loader.getCachedList(this);
		LayerListFragment layerListFrag = (LayerListFragment) getFragmentManager().findFragmentById(
				R.id.layer_list);

		for(LayerItemData lid : cachedData)
			mLayerListAdapter.add(lid);

		Log.e("onCreate", "setting list adapter");
		layerListFrag.setListAdapter(mLayerListAdapter);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (findViewById(R.id.layer_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((LayerListFragment) getFragmentManager().findFragmentById(
					R.id.layer_list)).setActivateOnItemClick(true);

		}
		// TODO: If exposing deep links into your app, handle intents here.
	}

	@Override
	public void onResume()
	{
		super.onResume();
		/* monitor network status change */
		m_networkStatusMonitor = new NetworkStatusMonitor(this);
		registerReceiver(m_networkStatusMonitor, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	@Override
	public void onPause()
	{
		super.onPause();

		unregisterReceiver(m_networkStatusMonitor);
		/* cancel async task if running */
		if(mLayerFetchTask != null && mLayerFetchTask.getStatus() != AsyncTask.Status.FINISHED)
		{
			Log.e("LayerListActivity.onPause", "cancelling Layer Fetch Task");
			mLayerFetchTask.cancel(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link LayerListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(int position) 
	{
		LayerItemData d = mLayerListAdapter.getItem(position);
		/* fetch raw xml from cache. Each time a new xml file is downloaded, it is saved on cache */
		if (mTwoPane) 
		{
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(LayerDetailFragment.ARG_ITEM_DATA, d.name);
			LayerDetailFragment fragment = new LayerDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
			.replace(R.id.layer_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, LayerDetailActivity.class);
			detailIntent.putExtra(LayerDetailFragment.ARG_ITEM_DATA, d.name);
			startActivity(detailIntent);
		}
	}

	@Override
	public void onLayersUpdated(ArrayList<LayerItemData> data,
			String errorMessage) 
	{
		/* everything should be done */
		if(!errorMessage.isEmpty())
		{
			MyAlertDialogFragment.MakeGenericError(errorMessage, this);
		}
	}

	@Override
	public void onLayerFetchProgress(int progress, int total) 
	{
		ArrayList<LayerItemData> partialData = mLayerFetchTask.getData();
		if(partialData.size() >= progress && progress > 0)
		{
			LayerItemData d = mLayerFetchTask.getData().get(progress - 1);
			mLayerListAdapter.update(d);
		}
	}

	@Override
	public void onLayerFetchCancelled(int size, int mTotal) 
	{

	}

	@Override
	public void onNetworkBecomesAvailable() 
	{
		PackageInfo pi;
		try {
			pi = getPackageManager().getPackageInfo(getPackageName(), 0);
			Log.e("LayerListActivity.onNetworkBecomesAvailable", "net available: executing task version code " + pi.versionCode);
			if(mLayerFetchTask != null && mLayerFetchTask.getStatus() != AsyncTask.Status.FINISHED)
				mLayerFetchTask.cancel(true);
			mLayerFetchTask = new LayerFetchTask(this, pi.versionCode, Locale.getDefault().getLanguage(), this);
			mLayerFetchTask.execute();
		}
		catch (NameNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onNetworkBecomesUnavailable() 
	{
		if(mLayerFetchTask != null && mLayerFetchTask.getStatus() == AsyncTask.Status.FINISHED)
		{
			Log.e("LayerListActivity.onNetworkBecomesUnavailable", "net available: cancel task");
			mLayerFetchTask.cancel(true);
		}

	}
}
