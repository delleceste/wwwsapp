package it.giacomos.android.wwwsapp.layers;

import android.graphics.drawable.BitmapDrawable;

public class LayerItemData 
{	
	public LayerItemData(String ti, 
			String sd, 
			String au, 
			float ver, 
			String d)
	{
		name = ti;
		short_desc = sd;
		author = au;
		version = ver;
		date = d;
	}
	
	public boolean isValid()
	{
		return name.length() > 0;
	}
	
	public LayerItemData()
	{
		name = short_desc = author = date = "";
		version = 0.0f;
	}
	
	public LayerItemData(String ti,
			float ver)
	{
		name = ti;
		version = ver;
	}
	
	public void setIcon(BitmapDrawable b)
	{
		if(icon != null)
			icon.getBitmap().recycle();
		icon = b;
	}
	
	public void setFlags(LayerItemFlags f)
	{
		flags = f;
	}
	
	public LayerItemFlags flags;
	public String name, title, short_desc, long_desc, author;
	public float version;
	public String date;
	public BitmapDrawable icon;
}
