package jysh.mf.Util;

import java.io.*;
import java.util.zip.*;

public class ziptool
{
	// 压缩单个选项
	public void zipCompress()throws Exception
	{
		
	}
	
	public void zipCompress(String zipFileName)throws Exception
	{
	/*	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		File fp = new File(projectpath,"文件夹");
		File f2 = new File(projectpath,"文件夹2");
		zipCompress(out,fp,fp.getName());
		zipCompress(out,f2,f2.getName());
		out.close();*/
	}

	public void zipCompress(ZipOutputStream out,File f,String base)throws Exception
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
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			byte[] byt = new byte[1024*1024];
			while((b=in.read(byt))!=-1)
			{
				out.write(byt,0,b);
			}
			in.close();
		}
	}
	
	
}
