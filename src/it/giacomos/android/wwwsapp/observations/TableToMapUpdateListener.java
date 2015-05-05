package it.giacomos.android.wwwsapp.observations;

import it.giacomos.android.wwwsapp.network.state.ViewType;

import java.util.HashMap;

public interface TableToMapUpdateListener {

	void onTableUpdate(HashMap<String, ObservationData> map,
			ViewType mStringType);

}
