package jysh.mf.Widget;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Activity.*;
import jysh.mf.Adapter.*;
import android.os.*;
import jysh.mf.Util.*;
import jysh.mf.Dialog.*;
import android.graphics.drawable.*;

public class searchPopSelect extends LinearLayout
{
	public searchPopSelect(final Context context,AttributeSet attr)
	{
		super(context,attr);
		LayoutInflater.from(context).inflate(R.layout.pop_select,this);
		findViewById(R.id.popselect_exit)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				cancel();
			}
		});
		findViewById(R.id.popselect_copy)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				copy();
			}
		});
		findViewById(R.id.popselect_move)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				move();
			}
		});
		findViewById(R.id.popselect_delete)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				delete();
			}
		});
		findViewById(R.id.popselect_qx)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				qx(((Search)context).list);
			}
		});
		findViewById(R.id.popselect_menu)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				menu(v);
			}
		});
		number = (TextView)findViewById(R.id.popselect_size);
		number.setText("0");
		this.context = (Search)context;
	}
	
	public void setContext(Search context)
	{
		this.context = context;
	}
	
	private List<File> fp = new ArrayList<>();
	private TextView number;
	private Search context;
	
	public void addFp(File fp)
	{
		this.fp.add(fp);
		number.setText(this.fp.size()+"");
	}
	
	public void removeFp(File fp)
	{
		this.fp.remove(fp);
		number.setText(this.fp.size()+"");
	}
	
	private void cancel()
	{
		setVisibility(GONE);
		fp.clear();
		context.list.setSelect(false);
	}
	
	public void copy()
	{
		final SearchFiles.SelectToPath path = new SearchFiles.SelectToPath(context);
		path.bt1.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				path.dismiss();
				new SearchFiles.RootBook(context){
					@Override
					public void select(String path)
					{
						cancel();
						File newPath = new File(path);
						new copyThread().set(context,fp,newPath).start();
					}
				}.show();
			}
		});
		path.bt2.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				path.dismiss();
				new SearchFiles.pathList(context){
					@Override
					public void select(File f)
					{
						cancel();
						new copyThread().set(context,fp,f).start();
					}
				}.show();
			}
		});
		path.show();
	}
	
	public void move()
	{
		final SearchFiles.SelectToPath path = new SearchFiles.SelectToPath(context);
		path.bt1.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					path.dismiss();
					new SearchFiles.RootBook(context){
						@Override
						public void select(String path)
						{
							cancel();
							File newPath = new File(path);
							moveFile(newPath);
						}
					}.show();
				}
			});
		path.bt2.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					path.dismiss();
					new SearchFiles.pathList(context){
						@Override
						public void select(File f)
						{
							cancel();
							moveFile(f);
						}
					}.show();
				}
			});
		path.show();
	}
	
	private void moveFile(File newPath)
	{
		String log = "移动成功";
		for(File f:fp)
		{
			if(!f.renameTo(new File(newPath,f.getName())))
			{
				log = log + "\n已存在:" + f.getName();
			}
		}
	}
	
	public void delete()
	{
		new MessageBox(context)
			.setTitle(fp.size()+"个文件")
			.setMessage("你确定要删除这些文件吗")
			.setRight("确认删除")
			.setRight(new MessageBox.onButton(){
				@Override
				public void onClick()
				{
					String log = "删除成功";
					for(File f:fp)
					{
						if(!f.delete())
						{
							log = log + "\n失败:" + f.getName();
							continue;
						}
						context.list.remove(f);
					}
					uitool.toos(context,log);
					context.list.notifyDataSetChanged();
					cancel();
				}
			})
			.show();
	}
	
	public void qx(SearchFiles list)
	{
		this.fp.clear();
		for(SearchFiles.Data f:list.data)
		{
			f.setCheck(true);
			this.addFp(f.getFp());
		}
		list.notifyDataSetChanged();
	}
	
	public void menu(View v)
	{
		PopSelectMenu menu = new PopSelectMenu(context,null){
			@Override
			public void onClick(View v)
			{
				if(v.getId()==id[0])
				{
					if(fp.size()==0)
						uitool.toos(context,"没有可压缩的对象");
					else if(fp.size()==1)
						new SearchFiles.ZipDialog(context).setFp(fp.get(0)).show();
					else  // 单个文件和多个文件要用不一样的名称
						new SearchFiles.ZipDialog(context).setFp(fp).show();
				}
				else if(v.getId()==id[1])
				{
					new FilesData(getContext()).setFiles(fp).show();
				}
				else if(v.getId()==id[2])
				{
					
				}
				pop.dismiss();
			}
		};
		PopupWindow pop = new PopupWindow
		(
			menu,
			GridView.LayoutParams.WRAP_CONTENT,
			GridView.LayoutParams.WRAP_CONTENT
		);
		menu.setPop(pop);
		pop.setFocusable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.showAsDropDown(v,0,-500);
	}
}

class copyThread extends Thread
{
	private Search context;
	private List<File> fp;
	private File newPath;

	public copyThread set(Search context, List<File> fp, File newPath)
	{
		this.context = context;
		this.fp = fp;
		this.newPath = newPath;
		return this;
	}

	@Override
	public void run()
	{
		Message msg = new Message();
		msg.what = Search.OPEN_DIALOG;
		context.UpdateUi.sendMessage(msg);
		
		String log = "复制完成";
		
		for(File fp:this.fp)
		{
			if(!fp.canRead())
		 	{
				log = log + "\n不可读取:" + fp.getName();
		 		continue;
		 	}
			if(new File(newPath,fp.getName()).exists())
			{
				log = log + "\n已存在:" + fp.getName();
				continue;
			}
			filetool.copyFile(newPath,fp);
		}
		
		msg = new Message();
		msg.what = Search.TOAS;
		msg.obj = log;
		context.UpdateUi.sendMessage(msg);
		
		msg = new Message();
		msg.what = Search.CLOSE_DIALOG;
		context.UpdateUi.sendMessage(msg);
	}
}
