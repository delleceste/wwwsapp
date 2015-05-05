package it.giacomos.android.wwwsapp.widgets.map.animation;

import it.giacomos.android.wwwsapp.network.DownloadStatus;
import android.util.Log;

public class Interrupted extends ProgressState 
{
	private String mUrlList;
	
	Interrupted(RadarAnimation radarAnimation, AnimationTask at,
			 State previousState, String urlList) 
	{
		
		super(radarAnimation, at, previousState);
		
		Log.e("Interrupted.Interrupted", "constructor");
		
		mUrlList = urlList;
		if(previousState.getStatus() == RadarAnimationStatus.RUNNING && dFrameNo > 0)
			dFrameNo--;
	}
	
	Interrupted(RadarAnimation radarAnimation, 
			int savedFrameNo, 
			int savedDownloadStep,
			String urlList)
	{
		super(radarAnimation, null, null);
		dDownloadStep = savedDownloadStep;
		dFrameNo = savedFrameNo;
		mUrlList = urlList;
	}

	@Override
	public RadarAnimationStatus getStatus() {
		return RadarAnimationStatus.INTERRUPTED;
	}

	@Override
	public void enter() 
	{
		if(dAnimationTask != null && !dAnimationTask.isCancelled())
		{
			Log.e("Interrupted.enter", "cancelling the animation task");
			dAnimationTask.cancel(false);
		}
	}

	@Override
	public boolean isRunnable()
	{
		return false;
	}

	@Override
	public boolean isProgressState()
	{
		return true;
	}

	public String getUrlList() 
	{
		return mUrlList;
	}

}
