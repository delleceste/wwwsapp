package it.giacomos.android.wwwsapp.rainAlert.interfaces;

import it.giacomos.android.wwwsapp.rainAlert.RainDetectResult;

public interface ImgCompareI 
{
	public RainDetectResult compare(ImgOverlayInterface imgOI1, 
			ImgOverlayInterface imgOI2, 
			ImgParamsInterface img_params_interface);
}
