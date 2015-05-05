package it.giacomos.android.wwwsapp.webcams;
import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;


public class WebcamXMLAssetLoader 
{
	public WebcamXMLAssetLoader(Context ctx)
	{
		mText = "";
		AssetManager assetManager = ctx.getAssets();
        InputStream input;
        try {
            input = assetManager.open("webcams.xml");
             
             int size = input.available();
             byte[] buffer = new byte[size];
             input.read(buffer);
             input.close();
             mText = new String(buffer);
        } 
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	public String getText()
	{
		return mText;
	}
	
	private String mText;
}
