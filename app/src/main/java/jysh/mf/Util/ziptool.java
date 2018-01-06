package jysh.mf.Util;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import android.util.*;

public class ziptool
{
	// 压缩单个选项
	// 目标路径，带文件名称
	// 数据源
	public static void zipCompress(File to,File fp)throws Exception
	{
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(to));
		zipCompress(out,fp,"");
		out.close();
	}
	
	// 压缩多个选项
	// 目标路径，带文件名称
	// 数据源(在这个程序中多选只允许文件，不允许目录)
	public static void zipCompress(File to,List<File> fp)throws Exception
	{
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(to));
		for(File f:fp)
		{
			zipCompress(out,f,f.getName());
		}
		out.close();
	}

	private static void zipCompress(ZipOutputStream out,File f,String base)throws Exception
	{
		if(f.isDirectory())
		{
			File[] fl = f.listFiles();
			if(base.length()!=0){
				out.putNextEntry(new ZipEntry(base+"/"));
			}
			for(int i=0;i<fl.length;i++)
			{
				zipCompress(out,fl[i],base+"/"+fl[i].getName());
			}
		}
		else
		{
			if(base.length()==0)
			{
				out.putNextEntry(new ZipEntry(f.getName()));
			}
			else
			{
				out.putNextEntry(new ZipEntry(base));
			}
			FileInputStream in = new FileInputStream(f);
			int b;
			byte[] byt = new byte[1024 * 1024];
			while((b=in.read(byt))!=-1)
			{
				out.write(byt,0,b);
			}
			in.close();
		}
	}
	
	// 文件解压，调用函数前文件夹未创建
	// to保存路径，带文件夹名
	// fp源文件
	public static void zipDecompression(File to,File fp)throws Exception
	{
		ZipInputStream inputStream = new ZipInputStream(new FileInputStream(fp));
		ZipEntry entry = inputStream.getNextEntry();
		ZipFile zipFile = new ZipFile(fp);
		to.mkdir();
		do{
		//	Log.e("Entry","文件路径:"+entry.getName());
			if(entry.isDirectory())
			{
				new File(to,entry.getName()).mkdir();
				continue;
			}
			FileOutputStream out = new FileOutputStream(new File(to,entry.getName()));
			InputStream in = zipFile.getInputStream(entry);
			int count = 0;
			byte[] byt = new byte[1024 * 1024];
			while ((count = in.read(byt)) != -1)
			{
				out.write(byt, 0, count);
			}
			out.flush();
			out.close();
			in.close();
		}while((entry = inputStream.getNextEntry()) != null);
		inputStream.close();
		zipFile.close();
	}
}
