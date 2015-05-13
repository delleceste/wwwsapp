package it.giacomos.android.wwwsapp.network.Data;


import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DataPoolCacheUtils 
{
	public DataPoolCacheUtils()
	{
		
	}

	/* restore all data from the storage */
	public String loadFromStorage(String filename, Context ctx) 
	{
		long startT = System.currentTimeMillis();
		String txt = "";
		int nRead;
		/* text items */
		String charset;
		charset = "ISO-8859-1";
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		try
		{
			filename = getFilePath(filename, ctx);
			byte [] buf = new byte[128];
			InputStream is = new FileInputStream(filename);
			while((nRead = is.read(buf)) != -1)
				byteBuffer.write(buf, 0, nRead);
			byteBuffer.flush();
			buf = byteBuffer.toByteArray();
			txt = new String(buf, charset);
			is.close();
		}
		catch (IOException ex) {

		}		
		Log.e("DataPoolCacheUtils.loadFromStorage", "loading string for " + filename + " took " + (System.currentTimeMillis() - startT));
		return txt;
	}

	public Bitmap loadBitmapFromStorage(String filename, Context ctx) 
	{
		long startT = System.currentTimeMillis();
//		Log.e("DataPoolCacheUtils.loadFromStorage", "loading bitmap for " + bitmapType);
		Bitmap bmp = null;
		/* Decode a file path into a bitmap. If the specified file name is null, 
		 * or cannot be decoded into a bitmap, the function returns null. 
		 */
		filename = getFilePath(filename, ctx);
		bmp = BitmapFactory.decodeFile(filename);
		Log.e("DataPoolCacheUtils.loadFromStorage", "loading bitmap for  " + filename + " took " + (System.currentTimeMillis() - startT));
		return bmp;
	}

	/* save all data to storage */
	public void saveBitmapToStorage(byte[] bytes, String filename, Context ctx)
	{
		long startT = System.currentTimeMillis();
		try
		{
			FileOutputStream fos;
			filename = getFilePath(filename, ctx);
			File fout = new File(filename);
			fos = new FileOutputStream(fout);
			fos.write(bytes);
			fos.close();
		} 
		catch (FileNotFoundException e) {
			/* nada que hacer */
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Log.e("DataPoolCacheUtils.saveToStorage", "saving bitmap for " + filename + " took " + (System.currentTimeMillis() - startT));
	}

	public void saveToStorage(byte[] bytes, String filename, Context ctx)
	{
		long startT = System.currentTimeMillis();
		String charset;
		charset = "ISO-8859-1";
		if(bytes != null)
		{
			try
			{
				filename = getFilePath(filename, ctx);
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), charset)); 
//				Log.e("saveToStorage", "writing " + filename + " type " + viewType);
				out.write(new String(bytes, charset));
				out.close();
			}
			catch (FileNotFoundException e) {
				Log.e("DataPoolCacheUtils.saveToStorage", e.getLocalizedMessage());
				/* nada que hacer */
			}
			catch (IOException e) 
			{
				Log.e("DataPoolCacheUtils.saveToStorage", e.getLocalizedMessage());
			}
			Log.e("DataPoolCacheUtils.saveToStorage", "saving string for " + filename + " took " + (System.currentTimeMillis() - startT));
		}
	}

	public boolean initDir(String dirname, Context ctx)
	{
		boolean ret = true;
		if(!dirname.endsWith("/"))
			dirname += "/";
		String filePath = getFilePath(dirname, ctx);
		File dir = new File(filePath);
		if(!dir.exists())
			ret = dir.mkdirs();
		return ret;
	}
	
	public String getFilePath(String filename, Context ctx)
	{
		File filesDir = ctx.getFilesDir();
		return filesDir.getAbsolutePath() + "/" + filename;
	}
	
}
