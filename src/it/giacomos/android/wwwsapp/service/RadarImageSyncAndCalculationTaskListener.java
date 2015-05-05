package it.giacomos.android.wwwsapp.service;

import it.giacomos.android.wwwsapp.rainAlert.RainDetectResult;

public interface RadarImageSyncAndCalculationTaskListener 
{
	public void onRainDetectionDone(RainDetectResult rainDetectResult);
}
