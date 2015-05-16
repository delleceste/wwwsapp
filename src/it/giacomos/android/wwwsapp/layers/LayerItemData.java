package it.giacomos.android.wwwsapp.layers;

import it.giacomos.android.wwwsapp.layers.installService.InstallTaskState;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class LayerItemData 
{	
	public void selectiveCopyFrom(LayerItemData other)
	{
		if(other.name != null && !other.name.isEmpty())
			name = other.name;
		if(other.title != null && !other.title.isEmpty())
			title = other.title;
		if(other.short_desc != null && !other.short_desc.isEmpty())
			short_desc = other.short_desc;
		if(other.author != null && !other.author.isEmpty())
			author = other.author;
	//	if(other.installed_version != 0)
	//		installed_version = other.installed_version;
		if(other.available_version != 0)
			available_version = other.available_version;
		if(other.date != null && !other.date.isEmpty())
			date = other.date;
		
		
		if(other.install_date != null && !other.install_date.isEmpty())
			install_date = other.install_date;
		
		install_progress = other.install_progress;
		
		installState = other.installState;
		
	}
	
	public boolean isValid()
	{
		return name.length() > 0;
	}
	
	public void restoreProgressInformation(String [] progressInformation)
	{
		if(progressInformation.length == 5)
		{
			name = progressInformation[0];
			installState = InstallTaskState.valueOf(progressInformation[4]);
			
			try{
				install_progress = Integer.parseInt(progressInformation[1]);
				installed = Boolean.parseBoolean(progressInformation[2]);
				online = Boolean.parseBoolean(progressInformation[3]);
			}
			catch (NumberFormatException e)
			{
				Log.e("LayerItemData: constructor from progress", 
						"NumberFormatException: " + e.getLocalizedMessage());
			}
		}
	}
	
	public LayerItemData()
	{
		name = short_desc = author = date = install_date = "";
		installed_version = available_version = -1.0f;
		install_progress = 100;
		installed = online = false;
		installState = InstallTaskState.NONE;
	}
	
	public void setIcon(BitmapDrawable b)
	{
		if(icon != null)
			icon.getBitmap().recycle();
		icon = b;
	}
	
	public String[] progressInformationToStringArray()
	{
		String [] data = new String[5];
		data[0] = name;
		data[1] = String.valueOf(install_progress);
		data[2] = String.valueOf(installed);
		data[3] = String.valueOf(online);
		data[4] = installState.name();
		return data;
	}
	
	public boolean installed, online;
	public String name, title, short_desc, long_desc, author;
	public float installed_version, available_version;
	public int install_progress;
	public String date, install_date;
	public BitmapDrawable icon;
	public InstallTaskState installState;
}
