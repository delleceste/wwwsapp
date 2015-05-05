package it.giacomos.android.wwwsapp.personalMessageActivity;

import it.giacomos.android.wwwsapp.WWWsAppActivity;
import it.giacomos.android.wwwsapp.R;
import it.giacomos.android.wwwsapp.service.ServiceManager;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class PersonalMessageManager 
{
	public PersonalMessageManager(WWWsAppActivity wWWsAppActivity, PersonalMessageData data) 
	{
		/* stop service */
		ServiceManager serviceManager = new ServiceManager();
//		Log.e("PersonalMessageManager", "created: is service running: " 
//				+ serviceManager.isServiceRunning(osmerActivity));
		boolean ret = serviceManager.setEnabled(wWWsAppActivity, false);
		if(ret)
			Toast.makeText(wWWsAppActivity, R.string.service_stopped_app_blocked, Toast.LENGTH_LONG).show();
		
		Intent i = new Intent(wWWsAppActivity, PersonalMessageActivity.class);
		i.putExtra("title", data.title);
		i.putExtra("message", data.message);
		i.putExtra("date", data.date);
		wWWsAppActivity.startActivity(i);
		
		if(data.blocking)
			wWWsAppActivity.finish();
	}
}
