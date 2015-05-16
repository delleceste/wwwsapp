/**
 * 
 */
package it.giacomos.android.wwwsapp.layers;

import it.giacomos.android.wwwsapp.layers.installService.InstallTaskState;
import it.giacomos.android.wwwsapp.network.state.Urls;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * @author giacomo
 *
 */
public class LayerListDownloadService extends Service {

	private String mErrorMsg;
	private boolean mIsCancelled;
	private String mAppLang;
	private float mAppVersionCode;
	private LayerListDownloadServiceState mState;

	/**
	 * @param name
	 */
	public LayerListDownloadService() {
		super();
		mErrorMsg = "";
		mIsCancelled = false;
	}

	/** If wi fi network is enabled, I noticed that turning on 3G network as well 
	 * produces this method to be invoked another times. That is, the ConnectivityChangedReceiver
	 * triggers a Service start command. In this case, we must avoid that the handler schedules
	 * another execution of the timer.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		mAppVersionCode = intent.getFloatExtra("version", -1.0f);
		mAppLang = intent.getStringExtra("lang");
		if(intent.hasExtra("download"))
			mDownloadLayersList();
		else if(intent.hasExtra("cancel"))
			mIsCancelled = true;
		
		if(mIsCancelled)
			mState = LayerListDownloadServiceState.CANCELLED;			
		
		return Service.START_STICKY;
	}

	private void mDownloadLayersList()
	{
		mState = LayerListDownloadServiceState.DOWNLOADING;
		try{
			int nRead;
			int progress = 0;
			int total = 0;
			int percent = 0;
			byte[] bytes;
			FileUtils cache = new FileUtils();
			Urls myUrls = new Urls();
			URL url = new URL(myUrls.layersListUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);

			String xml = "";
			
			String data = URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode (mAppLang, "UTF-8");
			data += "&" + URLEncoder.encode("app_version", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(mAppVersionCode), "UTF-8");

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String currentLine;
			while((currentLine = in.readLine()) != null)
				xml += currentLine + "\n";
			
			/* check if layer list did not change */
			String prevXml = cache.loadFromStorage(LayerListActivity.CACHE_LIST_DIR + "layerlist.xml", this);
//			if(prevXml.compareTo(xml) == 0)
//			{
//				Log.e("LayerFetchTask.doInBackground", "* xml layer list didn't change since last check");
//				return null;
//			}
			cache.saveToStorage(xml.getBytes(), LayerListActivity.CACHE_LIST_DIR + "layerlist.xml", this);
			in.close();

			if(!mIsCancelled)
			{
				XmlParser parser = new XmlParser();
				ArrayList<LayerItemData> d = parser.parseLayerList(xml);
				total = d.size();
				Log.e("LayerFetchtask", "detected " + total + " layers from " + xml + " query " + data);
				url = new URL(myUrls.layerDescUrl());
				for(LayerItemData i : d)
				{
					if(mIsCancelled)
						break;
					xml = "";
					String layer_name = i.name;
					/* get layer description */
					conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					data = URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(mAppLang, "UTF-8");
					data += "&" + URLEncoder.encode("layer", "UTF-8") + "=" + URLEncoder.encode(layer_name, "UTF-8");
					Log.e("LayerFetchTask.doInBacgkruod", "fetching" + data);
					wr = new OutputStreamWriter(conn.getOutputStream());
					wr.write(data);
					wr.flush();
					wr.close();
					in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					while((currentLine = in.readLine()) != null)
						xml += currentLine + "\n";
						
					cache.saveToStorage(xml.getBytes(), LayerListActivity.CACHE_LIST_DIR + layer_name + ".xml", this);
					Log.e("LayerFetchTask.doInBacgkruod", "parsing layer XML" + xml);
					LayerItemData itemData = parser.parseLayerDescription(xml);
					if(itemData.available_version < 0)
						itemData.available_version = i.available_version;
					
					/* get icon */
					conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					data = URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(mAppLang, "UTF-8");
					data += "&" + URLEncoder.encode("layer", "UTF-8") + "=" + URLEncoder.encode(layer_name, "UTF-8");
					data += "&" + URLEncoder.encode("icon", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
					wr = new OutputStreamWriter(conn.getOutputStream());
					wr.write(data);
					wr.flush();
					wr.close();
					
					InputStream inputStream = conn.getInputStream();
	        		/* get bytes from input stream */
	        		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
	        		bytes = new byte[1024];
	        		while ((nRead = inputStream.read(bytes, 0, bytes.length)) != -1) {
	        			byteBuffer.write(bytes, 0, nRead);
	        		}
	        		byte[] mBitmapBytes = byteBuffer.toByteArray();
	        		BitmapFactory.Options o2 = new BitmapFactory.Options();
	                o2.inPreferredConfig = Bitmap.Config.ARGB_8888;
	        		Bitmap bitmap = BitmapFactory.decodeByteArray(mBitmapBytes, 0, mBitmapBytes.length, o2);
	        		/* a network error may determine decodeByteArray to return a null bitmap (for instance connecting
	        		 * to a unauthenticated wireless network... it happened at Elettra...)
	        		 */
	        		if(bitmap == null) /* prevent from calling onBitmapBytesUpdate */
	        			Log.e("LayerFetchTask.doInBackground", "Error decoding bitmap for layer " + layer_name);
	        		else
	        		{
	        			cache.saveBitmapToStorage(mBitmapBytes, LayerListActivity.CACHE_LIST_DIR + layer_name + ".bmp", this);
	        			Log.e("LayerFetchTask.doInBackground", " creating bitmap drawable for " + bitmap + 
	        					": " + bitmap.getWidth() + "x" + bitmap.getHeight());
	        		}
	        		progress++;
	        		
					conn.disconnect();
	        		byteBuffer.flush();
	        		inputStream.close();
	        		percent = (int) Math.round((float) progress / (float) total * 100.0);
	        		mNotifyStateChanged(i.name, i.available_version, percent);
				}
				/* everything successful */
				mState = LayerListDownloadServiceState.COMPLETE;
			}
			if(mIsCancelled)
			{
				
			}
		}
		catch (UnsupportedEncodingException e) 
		{
			mState = LayerListDownloadServiceState.ERROR;
			mErrorMsg = e.getLocalizedMessage();
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			mState = LayerListDownloadServiceState.ERROR;
			mErrorMsg = e.getLocalizedMessage();
			e.printStackTrace();
		}
		Log.e("LayerListDownloadService", " Done!error : " + mErrorMsg);
		mNotifyStateChanged("", 100, -1);
	}
	
	private void mNotifyStateChanged(String layer, float version, int percent)
	{
//		Log.e("GreenDisplayService.mNotifyTutorialActivityStateChanged", " notifying state changed to " + mState.getType());
		Intent stateChangedNotif = new Intent(LayerListActivity.LIST_DOWNLOAD_SERVICE_STATE_CHANGED_INTENT);
		stateChangedNotif.putExtra("listDownloadServiceState", mState);
		stateChangedNotif.putExtra("percent", percent);
		stateChangedNotif.putExtra("error", mErrorMsg);
		if(layer.length() > 0)
		{
			stateChangedNotif.putExtra("layerName", layer);
			stateChangedNotif.putExtra("version", version);
		}

		LocalBroadcastManager.getInstance(this).sendBroadcast(stateChangedNotif);		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
