package jysh.mf.Util;

import android.graphics.*;
import java.io.*;
import jysh.mf.R;
import java.text.*;
import java.util.*;

public class filetool
{
	public static final String txt = "(.*)\\.txt$";
	public static final String mp3 = "(.*)\\.(?:mp3|wav)$";
	public static final String code ="(.*)\\.(?:cpp|c|html|java|xml|js|css|php|sh|s|h)$";
	public static final String img = "(.*)\\.(?:jpg|bmp|png|gif|jpeg|ico)$";
	public static final String mp4 = "(.*)\\.(?:mp4|mkv)$";
	public static final String sys = "(.*)\\.(?:ini)$";
	public static final String zip = "(.*)\\.(?:zip|rar)$";
	public static final String database = "(.*)\\.(?:log|sql)$";
	public static final String apk = "(.*)\\.apk$";
	public static final String ppt = "(.*)\\.(?:ppt|pdf)$";
	public static final String doc = "(.*)\\.(?:doc|dox|docx)$";
	
	public static int getFiconRes(String fname)
	{
		if(fname.matches(txt)) return R.drawable.ic_text;
		if(fname.matches(mp3)) return R.drawable.ic_null;
		if(fname.matches(code)) return R.drawable.ic_code;
		if(fname.matches(img)) return R.drawable.ic_image;
		if(fname.matches(mp4)) return R.drawable.ic_null;
		if(fname.matches(sys)) return R.drawable.ic_system;
		if(fname.matches(zip)) return R.drawable.ic_zip;
		if(fname.matches(database)) return R.drawable.ic_database;
		if(fname.matches(apk)) return R.drawable.ic_null;
		if(fname.matches(ppt)) return R.drawable.ic_null;
		if(fname.matches(doc)) return R.drawable.ic_null;
		return R.drawable.ic_null;
	}
	
	public static Bitmap getBitmap(File fp)
	{
		BitmapFactory.Options outOptions;
		int width,height;
		Bitmap temBitmap = null;
		outOptions = new BitmapFactory.Options();
		// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中。
		outOptions.inJustDecodeBounds = true;
		// 加载获取图片的宽高
		BitmapFactory.decodeFile(fp.getPath(), outOptions);
		height = outOptions.outHeight;
		width = outOptions.outWidth;
		// 根据宽设置缩放比例
		if(width>=100){
			outOptions.inSampleSize = width/100;
			outOptions.outWidth = 100;
			outOptions.outHeight = (int)(100*((float)height/(float)width));
		}else{
			outOptions.inSampleSize = 1;
			outOptions.outWidth = 100;
			outOptions.outHeight = (int)(100*((float)height/(float)width));
		}
		// 重新设置该属性为false，加载图片返回
		outOptions.inJustDecodeBounds = false;
		outOptions.inPurgeable = true;
		outOptions.inInputShareable = true;

		try
		{
			// 2017-11-26 网友倚楼听风雨提供的方法
			FileInputStream is = new FileInputStream(fp.getPath());
			temBitmap = BitmapFactory
				.decodeFileDescriptor
			(is.getFD(), null, outOptions);
		}
		catch (IOException e)
		{
			
		}
		return temBitmap;
	}
	
	public static String getFileDate(long time)
	{
		SimpleDateFormat font = new SimpleDateFormat("y-M-d H:m:s");
		return font.format(new Date(time));
	}
	
	public static String getFileSize(long size)
	{
		String c = new String();
		final long G = 1024*1024*1024;
		final int M = 1024*1024;
		final short K = 1024;
		if(size/G>=1){
			c = size/G + "." + size%G/M + " G";
		}else if(size/M>=1){
			c = size/M + "." + size%M/K + " M";
		}else if(size/K>=1){
			c = size/K + "." + size%K + "K";
		}else{
			c = size + " B";
		}
		return c;
	}
	
	public static String getFileRw(File fp)
	{
		String rb;
		if(fp.canRead())
			rb = "R";
		else
			rb = "_";
		if(fp.canWrite())
			rb+="W";
		else
			rb+="_";
		return rb;
	}
}
