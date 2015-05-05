package it.giacomos.android.wwwsapp.fragments;

import it.giacomos.android.wwwsapp.WWWsAppActivity;
import it.giacomos.android.wwwsapp.R;
import it.giacomos.android.wwwsapp.locationUtils.LocationService;
import it.giacomos.android.wwwsapp.network.Data.DataPool;
import it.giacomos.android.wwwsapp.network.Data.DataPoolCacheUtils;
import it.giacomos.android.wwwsapp.network.Data.DataPoolTextListener;
import it.giacomos.android.wwwsapp.network.state.ViewType;
import it.giacomos.android.wwwsapp.observations.ObservationsCache;
import it.giacomos.android.wwwsapp.preferences.Settings;
import it.giacomos.android.wwwsapp.purhcase.InAppUpgradeManager;
import it.giacomos.android.wwwsapp.purhcase.InAppUpgradeManagerListener;
import it.giacomos.android.wwwsapp.widgets.HomeTextView;
import it.giacomos.android.wwwsapp.widgets.OTextView;
import it.giacomos.android.wwwsapp.widgets.SituationImage;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SituationFragment extends Fragment implements DataPoolTextListener, InAppUpgradeManagerListener
{
	private SituationImage mSituationImage;
	private OTextView mHomeTextView;

	public SituationFragment() 
	{
		super();
		mSituationImage = null;
		mHomeTextView = null;
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		WWWsAppActivity oActivity = (WWWsAppActivity) getActivity();
		DataPoolCacheUtils dataCacheUtils = new DataPoolCacheUtils();
		/* register as a listener of DataPool */
		DataPool dataPool = oActivity.getDataPool();
		dataPool.registerTextListener(ViewType.HOME, this);
		String text = dataCacheUtils.loadFromStorage(ViewType.HOME, getActivity().getApplicationContext());
		mHomeTextView.setHtml(text);

		/* Get the reference to the observations cache */
		ObservationsCache observationsCache = ((WWWsAppActivity) getActivity()).getObservationsCache();
		/* register for latest observation cache changes.
		 * Since WWWsAppActivity.init has initialized the ObservationsCache maps with the cached
		 * observation tables, mSituationImage will be immediately notified of the observations
		 * change and so it will initialize with the cached observations.
		 */
		observationsCache.setLatestObservationCacheChangeListener(mSituationImage);
		/* register image view for location updates */
		LocationService locationService = oActivity.getLocationService();
		locationService.registerLocationServiceAddressUpdateListener(mSituationImage);
		locationService.registerLocationServiceUpdateListener(mSituationImage);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = null;
		/* Get the reference to the data pool in order to register for events */
		view = inflater.inflate(R.layout.home, null);
		mHomeTextView  = (HomeTextView)view.findViewById(R.id.homeTextView);
		mSituationImage = (SituationImage) view.findViewById(R.id.situationImageView);
		mSituationImage.setViewType(ViewType.HOME);

		return view;
	}

	private View mSetupTrialInterface()
	{
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View trialView = inflater.inflate(R.layout.trial_layout, null);

		if(getResources().getConfiguration().orientation ==
				Configuration.ORIENTATION_PORTRAIT)
		{
			View view = getActivity().findViewById(R.id.mainScrollView);
			//trialView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) (trialViewHeightDp * scale + 0.5f)));
			trialView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			ViewGroup vg = (ViewGroup) view.findViewById(R.id.homeRelativeLayout);
			vg.addView(trialView, 0);

		}
		else if(getResources().getConfiguration().orientation ==
				Configuration.ORIENTATION_LANDSCAPE)
		{
			View view = getActivity().findViewById(R.id.homeRelativeLayout);
			//			trialView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) (trialViewHeightDp * scale + 0.5f)));
			trialView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			ViewGroup vg = (ViewGroup) view.findViewById(R.id.scrollLayout);
			vg.addView(trialView, 0);
		}
		return trialView;
	}

	public void onDestroy()
	{
		super.onDestroy();
		if(mSituationImage != null)
		{
			WWWsAppActivity oActivity = ((WWWsAppActivity) getActivity());
			DataPool dataPool = oActivity.getDataPool();
			mSituationImage.cleanup();
			/* unregister text listener */
			dataPool.unregisterTextListener(mHomeTextView.getViewType());
			LocationService locationService = oActivity.getLocationService();
			locationService.removeLocationServiceAddressUpdateListener(mSituationImage);
			locationService.removeLocationServiceUpdateListener(mSituationImage);
		}
	}

	@Override
	public void onTextChanged(String txt, ViewType t, boolean fromCache) 
	{
		mHomeTextView.setHtml(txt);
	}

	@Override
	public void onTextError(String error, ViewType t) 
	{
		mHomeTextView.setHtml(error);
	}


	@Override
	public void onPurchaseComplete(boolean ok, String error, boolean purchased) 
	{
		if(ok && purchased)
			getActivity().findViewById(R.id.trialLayout).setVisibility(View.GONE);
		/* cache the information locally */
		new Settings(this.getActivity()).setApplicationPurchased(purchased && ok);
	}

	@Override
	public void onCheckComplete(boolean ok, String error, boolean purchased) 
	{
		
	}

	@Override
	public void onInAppSetupComplete(boolean success, String message) {
		// TODO Auto-generated method stub

	}

}
