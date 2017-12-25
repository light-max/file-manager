package jysh.mf.Util;

import android.graphics.*;
import java.io.*;
import jysh.mf.R;

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
		if(fname.matches(txt)) return R.drawable.ic_file_text;
		if(fname.matches(mp3)) return R.drawable.ic_file_null;
		if(fname.matches(code)) return R.drawable.ic_file_code;
		if(fname.matches(img)) return R.drawable.ic_file_image;
		if(fname.matches(mp4)) return R.drawable.ic_file_null;
		if(fname.matches(sys)) return R.drawable.ic_file_system;
		if(fname.matches(zip)) return R.drawable.ic_file_zip;
		if(fname.matches(database)) return R.drawable.ic_file_database;
		if(fname.matches(apk)) return R.drawable.ic_file_null;
		if(fname.matches(ppt)) return R.drawable.ic_file_null;
		if(fname.matches(doc)) return R.drawable.ic_file_null;
		return R.drawable.ic_file_null;
	}
	
	public static Bitmap getBitmap(File fp)
	{
		Bitmap bmp = null;
		
		return bmp;
	}
}
