package it.giacomos.android.wwwsapp.layers;

import android.graphics.drawable.BitmapDrawable;

public class LayerItemData 
{	
	public LayerItemData(String ti, 
			String sd, 
			String au, 
			float ver, 
			String d,
			String _rawXml)
	{
		name = ti;
		short_desc = sd;
		author = au;
		version = ver;
		date = d;
		rawXml = _rawXml;
	}
	
	public LayerItemData()
	{
		
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
	public String name, title, short_desc, long_desc, author, rawXml;
	public float version;
	public String date;
	public BitmapDrawable icon;
}
