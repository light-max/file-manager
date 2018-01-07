package jysh.mf.Util;

import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import java.io.*;
import java.text.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Dialog.*;
import jysh.mf.Widget.*;

public class filetool
{
	public static final String txt = "(.*)\\.txt$";
	public static final String mp3 = "(.*)\\.(?:mp3|wav)$";
	public static final String code ="(.*)\\.(?:cpp|c|java|xml|js|css|php|sh|s|h)$";
	public static final String img = "(.*)\\.(?:jpg|bmp|png|gif|jpeg|ico)$";
	public static final String mp4 = "(.*)\\.(?:mp4|mkv)$";
	public static final String sys = "(.*)\\.(?:ini)$";
	public static final String zip = "(.*)\\.(?:zip|rar)$";
	public static final String database = "(.*)\\.(?:log|sql)$";
	public static final String apk = "(.*)\\.apk$";
	public static final String ppt = "(.*)\\.(?:ppt|pdf)$";
	public static final String doc = "(.*)\\.(?:doc|dox|docx)$";
	public static final String web = "(.*)\\.(?:htm|html)$";
	
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
		if(fname.matches(web)) return R.drawable.ic_web;
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
			temBitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, outOptions);
			is.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
	
	public static File fileTo = null;
	
	public static void copyFile() {
		final List<LayoutFileList> view = uitool.pagerAdapter.view;

		// 目标路径
		fileTo = view.get(uitool.add.getPosition()).listadp.getFp();

		new Thread(new Runnable() {
				@Override
				public void run() {
					List<File> fp = getFile();
					// 开始复制
					for (int u = 0; u < fp.size(); u++) {
						File i = fp.get(u);
						File o = new File(fileTo, i.getName());
						byte byt[] = new byte[1024 * 1024];
						BufferedInputStream inputStream = null;
						BufferedOutputStream outputStream = null;
						try {
							inputStream = new BufferedInputStream(new FileInputStream(i));
							outputStream = new BufferedOutputStream(new FileOutputStream(o));
							int length = 0;
							try
							{
								while ((length = inputStream.read(byt)) != -1)
								{
									outputStream.write(byt, 0, length);
								}
							}
							catch (IOException e)
							{}
						}
						catch (FileNotFoundException e)
						{
							e.printStackTrace();
						} finally {
							try
							{
								// 2017-12-29 15:9网友倚楼听风雨再次提供帮助
								outputStream.flush();
								outputStream.close();
								inputStream.close();
							}
							catch (IOException e)
							{}
						}
						System.out.println(u);
					}
					// 刷新界面
					Message msg = new Message();
					msg.what = Progeress.DISMISS;
					msg.obj = view;
					uitool.mainThis.UpdateUi.sendMessage(msg);
				}
			}).start();
	}
	
	public static void mkDri(File fp,File path)
	{
		File newdri = new File(path,fp.getName());
		newdri.mkdir();
		for(File f:fp.listFiles())
		{
			if(f.isDirectory())
			{
				mkDri(f,newdri);
			}
		}
	}
	
	public static void moveFile()
	{
		List<LayoutFileList> view = uitool.pagerAdapter.view;
		fileTo = view.get(uitool.add.getPosition()).listadp.getFp();
		List<File> fp = getFile();
		
		// 开始便改文件路径,也就是移动文件
		for(File f:fp)
		{
			if(!f.isDirectory())
			{
				f.renameTo(new File(fileTo,f.getName()));
			}
		}
		
		// 刷新界面
		for(LayoutFileList v:view)
		{
			v.listadp.loadList();
			v.listadp.notifyDataSetChanged();
		}
	}
	
	public static boolean renameFile(File fp,String name)
	{
		File to = new File(fp.getParent(),name);
		return fp.renameTo(to);
	}
	
	public static void deleteFile(File fp)
	{
		if(fp==null)
		{
			return;
		}
		if(fp.isDirectory())
		{
			for(File f:fp.listFiles())
			{
				deleteFile(f);
			}
		}
		fp.delete();
	}
	
	public static List<File> getFile()
	{
		List<File> fp = new ArrayList<>();
		List<SelectLayout.Data> select = uitool.drawlayout.select.data;
		for(SelectLayout.Data f:select)
		{
			fp.add(f.getFp());
		}
		return fp;
	}
	
	public static boolean moveFile(File moveTo,File fp)
	{
		return fp.renameTo(new File(moveTo,fp.getName()));
	}
	
	public static void copyFile(File to,File fp)
	{
		if(fp.isDirectory())
		{
			File toName = new File(to,fp.getName());
			toName.mkdir();
			for(File f:fp.listFiles())
			{
				copyFile(toName,f);
			}
			return;
		}
		
		byte byt[] = new byte[1024 * 1024];
		int lengh;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try
		{
			in = new BufferedInputStream(new FileInputStream(fp));
			out = new BufferedOutputStream(new FileOutputStream(new File(to,fp.getName())));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		try
		{
			while ((lengh = in.read(byt)) != -1)
			{
				out.write(byt, 0, lengh);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				out.flush();
				out.close();
				in.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void fileOpens(Context context,File fp)
	{
		String name = fp.getName();
		if(name.matches(zip))
		{
			new ZipOperateSelect(context).setFp(fp).show();
		}
		else if(name.matches(img))
		{
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(Uri.fromFile(fp), "image/*");
			uitool.mainThis.startActivity(intent);
		}
	}
}
