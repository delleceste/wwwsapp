package it.giacomos.android.wwwsapp.layers;

import android.graphics.Bitmap;

public class LayerItemData 
{
	public LayerItemData(String ti, String sd, String au, float ver, String d)
	{
		title = ti;
		short_desc = sd;
		author = au;
		version = ver;
		date = d;
	}
	
	public void setIcon(Bitmap b)
	{
		if(icon != null)
			icon.recycle();
		icon = b;
	}
	
	
	public String title, short_desc, long_desc, author;
	public float version;
	public String date;
	public Bitmap icon;
}
