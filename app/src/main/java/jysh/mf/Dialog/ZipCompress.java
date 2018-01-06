package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import android.widget.GridLayout.*;
import java.io.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Util.*;
import jysh.mf.Widget.*;
import android.os.*;

public class ZipCompress extends Dialog implements View.OnClickListener
{
	public ZipCompress(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_zipcompress,null,false);
		setContentView(v);
		for(int i:id)
		{
			findViewById(i).setOnClickListener(this);
		}
		isRadioButton(onAt);
		findViewById(exit).setOnClickListener(this);
		findViewById(start).setOnClickListener(this);
		
		path = (EditText)findViewById(id_path);
		name = (EditText)findViewById(id_name);
		
		setDri();
	}
	
	public File fp = null;
	
	public ZipCompress setFp(File fp)
	{
		this.fp = fp;
		setDri();
		return this;
	}
	
	private static final int[] id = new int[]{
		R.id.dialog_zipcompress_radio1,
		R.id.dialog_zipcompress_radio2,
		R.id.dialog_zipcompress_radio3,
	};
	private int onAt = 0;
	private int exit = R.id.dialog_zipcompress_exit;
	private int start = R.id.dialog_zipcompress_start;
	private int id_path = R.id.dialog_zipcompress_path;
	private int id_name = R.id.dialog_zipcompress_name;
	
	public EditText path,name;
	
	@Override
	public void onClick(View v)
	{
		if(v.getId()==exit)
		{
			dismiss();
			return;
		}
		if(v.getId()==start)
		{
			start();
			return;
		}
		isRadioButton(v.getId());
		if(v.getId()==id[0])
		{
			setDri();
			return;
		}
		if(v.getId()==id[1])
		{
			setOpenDri();
			return;
		}
		if(v.getId()==id[2])
		{
			setSelectDri();
			return;
		}
	}
	
	private void isRadioButton(int id)
	{
		RadioButton radio[] = new RadioButton[4];
		for(int i = 0;i < this.id.length;i++)
		{
			if(id==this.id[i])
			{
				onAt = i;
			}
			radio[i] = (RadioButton)findViewById(this.id[i]);
			radio[i].setChecked(false);
		}
		radio[onAt].setChecked(true);
	}
	
	public void setDri()
	{
		LayoutFileList listview = uitool.pagerAdapter.get(uitool.add.getPosition());
		File fp = listview.listadp.getFp();
		String fname = "新建压缩包";
		if(this.fp!=null)
		{
			fname = this.fp.getName();
		}
		File to = new File(fp,fname+".zip");
		path.setText(fp.getPath());
		name.setText(to.getName());
		if(to.exists())
		{
			int i = 1;
			File[] listFiles = fp.listFiles();
			for(int j = 0;j < listFiles.length;j++)
			{
				if((fname+"("+i+").zip").equals(listFiles[j].getName()))
				{
					i++;
					j = 0;
				}
			}
			name.setText(fname+"("+i+").zip");
		}
	}
	
	private void setOpenDri()
	{
		new RootSelect(getContext()).setRootDialog(this).show();
	}
	
	private void setSelectDri()
	{
		new DriBookSelect(getContext()).setRootDialog(this).show();
	}
	
	public void setPathAndName(File dri)
	{
		String fname = "新建压缩包";
		if(this.fp!=null)
		{
			fname = this.fp.getName();
		}
		File fp = dri;
		File to = new File(fp,fname+".zip");
		path.setText(fp.getPath());
		name.setText(to.getName());
		if(to.exists())
		{
			int i = 1;
			File[] listFiles = fp.listFiles();
			for(int j = 0;j < listFiles.length;j++)
			{
				if((fname+"("+i+").zip").equals(listFiles[j].getName()))
				{
					i++;
					j = 0;
				}
			}
			name.setText(fname+"("+i+").zip");
		}
	}
	
	public File to = null;
	public void start()
	{
		to = new File(path.getText().toString());
		if(!to.isDirectory())
		{
			uitool.toos(uitool.mainThis,"无效的路径");
			return;
		}
		to = new File(path.getText().toString(),name.getText().toString());
		if(to.exists())
		{
			uitool.toos(uitool.mainThis,"已有同名文件");
			return;
		}
		if(fp!=null)
		{
			for(File _to = to; !_to.getPath().equals("/"); _to = _to.getParentFile())
			{
				if(!_to.equals(fp))
				{
					continue;
				}
				uitool.toos(uitool.mainThis,"保存路径不能在源文件夹内");
				return;
			}
			uitool.progerss.show();
			new Thread(new Runnable(){
				@Override
				public void run()
				{
					try
					{
						ziptool.zipCompress(to, fp);
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
					clos.obj = ZipCompress.this;
					uitool.mainThis.UpdateUi.sendMessage(clos);
					
					Message toas = new Message();
					toas.what = uitool.TOAS;
					toas.obj = "已完成";
					uitool.mainThis.UpdateUi.sendMessage(toas);
				}
			}).start();
			return;
		}
		final List<File> flist = new ArrayList<>();
		String error = "";
		int e = 0;
		for(SelectLayout.Data d:uitool.drawlayout.select.data)
		{
			if(d.getFp().isDirectory() || !d.getFp().canRead() || !d.getFp().exists())
			{
				error += d.getFp().getName()+"\n";
				e++;
				continue;
			}
			flist.add(d.getFp());
		}
		uitool.drawlayout.select.data.clear();
		if(e!=0)
		{
			error = "发生了"+e+"个错误\n"+error;
			error = error + "不可读或者不存在";
		}
		uitool.progerss.show();
		new Thread(new Runnable(){
			@Override
			public void run()
			{
				try
				{
					ziptool.zipCompress(to, flist);
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
				clos.obj = ZipCompress.this;
				uitool.mainThis.UpdateUi.sendMessage(clos);

				Message toas = new Message();
				toas.what = uitool.TOAS;
				toas.obj = "已完成";
				uitool.mainThis.UpdateUi.sendMessage(toas);
			}
		}).start();
	}
	
	// start函数完成任务会调用这个接口
	public onClose close = null;
	public ZipCompress setClose(onClose o)
	{
		close = o;
		return this;
	}
	static public interface onClose
	{
		public void close();
	}
}
