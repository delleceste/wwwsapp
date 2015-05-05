package it.giacomos.android.wwwsapp.rainAlert.interfaces;

public interface ImgParamsInterface 
{
	public double getDbzForColor(int[] array_rgb);
	
	public String getUnit();
	
	public double getThreshold();

	public double getBigIncreaseValue();
}
