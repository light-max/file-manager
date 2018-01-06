package jysh.mf.Dialog;

import android.content.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import jysh.mf.*;
import jysh.mf.Util.*;
import jysh.mf.Widget.*;

// 继承保存压缩文件的类
public class ZipDecompression extends ZipCompress
{
	public ZipDecompression(Context context)
	{
		super(context);
		TextView title = (TextView)findViewById(R.id.dialog_zipcompress_title);
		title.setText("zip文件解压...");
	}

	@Override
	public void setDri()
	{
		LayoutFileList view = uitool.pagerAdapter.get(uitool.add.getPosition());
		path.setText(view.listadp.getFp().getPath());
		if(fp!=null)
		{
			setPathAndName(view.listadp.getFp());
		}
	}

	@Override
	public void setPathAndName(File dri)
	{
		String fname = this.fp.getName();
		fname = fname.replace(".zip","");
		File fp = dri;
		File to = new File(fp,fname);
		path.setText(fp.getPath());
		name.setText(to.getName());
		if(to.exists())
		{
			int i = 1;
			File listFiles[] = fp.listFiles();
			for(int j = 0;j < listFiles.length;j++)
			{
				if(("("+i+")"+fname).equals(listFiles[j].getName()))
				{
					i++;
					j = 0;
				}
			}
			name.setText("("+i+")"+fname);
		}
	}

	@Override
	public ZipCompress setFp(File fp)
	{
		super.setFp(fp);
		LayoutFileList view = uitool.pagerAdapter.get(uitool.add.getPosition());
		setPathAndName(view.listadp.getFp());
		return this;
	}

	@Override
	public void start()
	{
		// to目标路径，带文件名 fp源路径
		to = new File(path.getText().toString(),name.getText().toString());
		if(to.exists())
		{
			uitool.toos(uitool.mainThis,"已有同名文件");
			return;
		}
		uitool.progerss.show();
		new Thread(new Runnable(){
			@Override
			public void run()
			{
				try
				{
					ziptool.zipDecompression(to, fp);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				if(close!=null)
				{
					close.close();
				}
				
				Message msg = new Message();
				msg.what = Progeress.DISMISS;
				msg.obj = uitool.pagerAdapter.view;
				uitool.mainThis.UpdateUi.sendMessage(msg);

				Message clos = new Message();
				clos.what = uitool.CLOSE_DIALOG;
				clos.obj = ZipDecompression.this;
				uitool.mainThis.UpdateUi.sendMessage(clos);

				Message toas = new Message();
				toas.what = uitool.TOAS;
				toas.obj = "已完成";
				uitool.mainThis.UpdateUi.sendMessage(toas);
			}
		}).start();
	}
}
