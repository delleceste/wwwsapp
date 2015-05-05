package it.giacomos.android.wwwsapp.trial;

public interface InAppEventListener 
{	
	public void onTrialDaysRemaining(int days);
	
	public void onAppPurchased(boolean ok);
}
