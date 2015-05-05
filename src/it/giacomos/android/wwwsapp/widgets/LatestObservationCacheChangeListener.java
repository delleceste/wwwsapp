package it.giacomos.android.wwwsapp.widgets;

import it.giacomos.android.wwwsapp.observations.ObservationsCache;

public interface LatestObservationCacheChangeListener {
	public void onCacheUpdate(ObservationsCache cache);
}
