package it.giacomos.android.wwwsapp;

import it.giacomos.android.wwwsapp.observations.MapMode;
import it.giacomos.android.wwwsapp.pager.FragmentType;
import it.giacomos.android.wwwsapp.widgets.map.MapViewMode;
import it.giacomos.android.wwwsapp.widgets.map.OMapFragment;

/** 
 * Updates the map contents if the currently displayed fragment is OMapFragment.
 * If the currently displayed fragment is not the map, nothing is done. 
 * @author giacomo
 *
 */
public class MapViewUpdater 
{
	public void update(WWWsAppActivity a)
	{
		int displayedChild = a.getDisplayedFragment();
		if(displayedChild == FragmentType.MAP)
		{		
			OMapFragment mapView = (OMapFragment) a.getMapFragment();
			MapViewMode mapMode = mapView.getMode();
			/* update radar if mapview is showing the radar */
			if(mapMode.currentMode == MapMode.RADAR)
			{
				a.radar();
			}
			else if(mapMode.currentMode == MapMode.REPORT)
			{
				a.updateReport(true);
			}

		}
	}

}
