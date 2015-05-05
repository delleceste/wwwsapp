package it.giacomos.android.wwwsapp.observations;

import it.giacomos.android.wwwsapp.network.state.ViewType;

import java.util.HashMap;

public interface ObservationsCacheUpdateListener {
	
	public void onObservationsCacheUpdate(HashMap<String, ObservationData> map, ViewType t);

}
