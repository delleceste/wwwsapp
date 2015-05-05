package it.giacomos.android.wwwsapp;

import it.giacomos.android.wwwsapp.observations.MapMode;
import it.giacomos.android.wwwsapp.observations.ObservationType;

public interface ObservationTypeSelectionDialogListener 
{
	public void onSelectionDone(ObservationType t, MapMode mapMode);
}
