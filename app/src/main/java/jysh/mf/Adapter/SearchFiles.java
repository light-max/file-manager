package jysh.mf.Adapter;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AbsListView.*;
import java.io.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Activity.*;
import jysh.mf.Dialog.*;
import jysh.mf.Util.*;
import jysh.mf.Widget.*;
import android.util.*;

public class SearchFiles extends ArrayAdapter<SearchFiles.Data> implements OnScrollListener
{
	public SearchFiles(Context context,ListView view,List<Data> data)
	{
		super(context,0,data);
		this.view = view;
		this.view.setAdapter(this);
		this.data = data;
		this.setSelect(false);
		this.setScroll(0);
		this.view.setOnScrollListener(this);
		activity = (Search)context;
	}
	
	public List<Data> data;
	public ListView view;
	private boolean select;
	private int scroll;

	public void setScroll(int scroll)
	{
		this.scroll = scroll;
	}

	public int getScroll()
	{
		return scroll;
	}

	public void setSelect(boolean select)
	{
		this.select = select;
	}

	public boolean isSelect()
	{
		return select;
	}
	
	private Search activity;
	public Search getActivity()
	{
		return activity;
	}
	
	@Override
	public View getView(final int position, View convertView,final ViewGroup parent)
	{
		View v;
		ViewHolder holder;
		if(convertView==null)
		{
			v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_filedataview,null,false);
			holder = new ViewHolder(v);
			v.setTag(holder);
		}
		else
		{
			v = convertView;
			holder = (ViewHolder)v.getTag();
		}
		
		final Data d = data.get(position);
		
		if(d.getIcon()!=R.drawable.ic_image)
		{
			holder.icon.setImageResource(d.getIcon());
		}
		else if(position > getScroll() - 2 && position < getScroll() + 12)
		{
			if(d.getBmp()==null)
			{
				holder.icon.setImageResource(d.getIcon());
				final Search activity =  (Search)(parent.getContext());
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						d.setBmp(filetool.getBitmap(d.getFp()));
						Message msg = new Message();
						msg.what = Search.FILES_BMP;
						activity.UpdateUi.sendMessage(msg);
					}
				}).start();
			}
			else
			{
				holder.icon.setImageBitmap(d.getBmp());
			}
		}
		else
		{
			holder.icon.setImageResource(d.getIcon());
		}
		
		holder.name.setText(d.getName());
		holder.date.setText(d.getDate());
		holder.rb.setText(d.getRb());
		holder.size.setText(d.getSize());
		
		v.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				filetool.fileOpens(parent.getContext(),d.getFp());
			}
		});
		
		v.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v)
			{
				LongClick(d,position);
				return true;
			}
		});
		
		if(isSelect())
		{
			holder.check.setVisibility(View.VISIBLE);
			holder.check.setChecked(d.isCheck());
			holder.check.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					d.setCheck(!d.isCheck());
					((CheckBox)v).setChecked(d.isCheck());
				}
			});
		}
		else
		{
			holder.check.setVisibility(View.GONE);
		}
		
		return v;
	}

	@Override
	public void onScroll(AbsListView p1, int p2, int p3, int p4)
	{
		// TODO: Implement this method
	}

	@Override
	public void onScrollStateChanged(AbsListView p1, int p2)
	{
		switch(p2)
		{
		case SCROLL_STATE_IDLE:
			this.setScroll(view.getFirstVisiblePosition());
			notifyDataSetChanged();
			break;
		}
	}
	
	public void addAll(List<File> fp)
	{
		List<Data> datas = new ArrayList<>();
		for(File f:fp)
		{
			datas.add(new Data(f));
		}
		data.addAll(datas);
	}
	
	private void LongClick(final Data d,final int position)
	{
		new OperateFile(getContext(),d.getFp())
			.setSelect(new OperateFile.OnClick(){
				@Override
				public void onClick()
				{
					setSelect(true);
					notifyDataSetChanged();
				}
			})
			.setReanme(new OperateFile.OnClick(){
				@Override
				public void onClick()
				{
					final EditBox edit = new EditBox(getContext());
					edit
						.setTitle("重命名")
						.setMessage(d.getName())
						.setRight("确认")
						.setRight(new EditBox.onButton(){
							@Override
							public void onClick()
							{
								File newFile = new File(d.getFp().getParentFile(),edit.getMessage());
								if(!d.getFp().renameTo(newFile))
								{
									uitool.toos(getContext(),"文件已存在");
									return;
								}
								uitool.toos(getContext(),"重命名成功");
								d.setFp(newFile);
								d.setName(edit.getMessage());
								notifyDataSetChanged();
							}
						})
						.show();
				}
			})
			.setDelete(new OperateFile.OnClick(){
				@Override
				public void onClick()
				{
					new MessageBox(getContext())
						.setTitle("(你确定要删除这个文件吗)")
						.setMessage("此操作无法逆转")
						.setRight("确认")
						.setRight(new MessageBox.onButton(){
							@Override
							public void onClick()
							{
								if(d.getFp().delete())
								{
									uitool.toos(getContext(),"删除成功");
									data.remove(position);
									notifyDataSetChanged();
								}
								else
								{
									uitool.toos(getContext(),"删除失败");
								}
							}
						})
						.show();
				}
			})
			.setMove(new OperateFile.OnClick(){
				@Override
				public void onClick()
				{
					final SelectToPath select = new SelectToPath(getContext());
					select.bt1.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v)
						{
							select.dismiss();
							new RootBook(getContext()){
								@Override
								public void select(String path)
								{
									File newFile = new File(path,d.getFp().getName());
									if(d.getFp().renameTo(newFile))
									{
										uitool.toos(getContext(),"移动成功");
										d.setFp(newFile);
										return;
									}
									uitool.toos(getContext(),"移动失败");
								}
							}.show();
						}
					});
					select.bt2.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v)
						{
							select.dismiss();
							new pathList(getContext()){
								@Override
								public void select()
								{
									File newFile = new File(this.root.getFp(),d.getFp().getName());
									if(d.getFp().renameTo(newFile))
									{
										uitool.toos(getContext(),"移动成功");
										d.setFp(newFile);
										return;
									}
									uitool.toos(getContext(),"移动失败");
								}
							}.show();
						}
					});
					select.show();
				}
			})
			.setCopy(new OperateFile.OnClick(){
				@Override
				public void onClick()
				{
					final SelectToPath select = new SelectToPath(getContext());
					select.bt1.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v)
						{
							select.dismiss();
							new RootBook(getContext()){
								@Override
								public void select(String path)
								{
									File newFile = new File(path,d.getFp().getName());
									new copyThread().set(SearchFiles.this.getActivity(),d.getFp(),newFile).start();
								}
							}.show();
						}
					});
					select.bt2.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v)
						{
							select.dismiss();
							new pathList(getContext()){
								@Override
								public void select()
								{
									File newFile = new File(this.root.getFp(),d.getFp().getName());
									new copyThread().set(getActivity(),d.getFp(),newFile).start();
								}
							}.show();
						}
					});
					select.show();
				}
			})
			.setZip(new OperateFile.OnClick(){
				@Override
				public void onClick()
				{
					new ZipDialog(getActivity()).setFp(d.getFp()).show();
				}
			})
			.show();
	}
	
	class SelectToPath extends OperateBook
	{
		public SelectToPath(Context context)
		{
			super(context);
			((TextView)bt1).setText("目录书签");
			((TextView)bt2).setText("新的目录");
		}
	}
	
	static public class Data
	{
		private File fp;
		private int icon;
		private Bitmap bmp;
		private String name,date,rb,size;
		private boolean check;

		public Data(File fp)
		{
			this.fp = fp;
			if(fp==null)
			{
				return;
			}
			if(fp.isDirectory())
			{
				icon = R.drawable.ic_dri;
			}
			else
			{
				icon = filetool.getFiconRes(fp.getName());
			}
			name = fp.getName();
			date = filetool.getFileDate(fp.lastModified());
			rb = filetool.getFileRw(fp);
			size = filetool.getFileSize(fp.length());
			bmp = null;
			this.setCheck(false);
		}

		public void setCheck(boolean check)
		{
			this.check = check;
		}

		public boolean isCheck()
		{
			return check;
		}

		public void setFp(File fp)
		{
			this.fp = fp;
		}

		public File getFp()
		{
			return fp;
		}

		public void setIcon(int icon)
		{
			this.icon = icon;
		}

		public int getIcon()
		{
			return icon;
		}

		public void setBmp(Bitmap bmp)
		{
			this.bmp = bmp;
		}

		public Bitmap getBmp()
		{
			return bmp;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}

		public void setDate(String date)
		{
			this.date = date;
		}

		public String getDate()
		{
			return date;
		}

		public void setRb(String rb)
		{
			this.rb = rb;
		}

		public String getRb()
		{
			return rb;
		}

		public void setSize(String size)
		{
			this.size = size;
		}

		public String getSize()
		{
			return size;
		}
	}
		
	static public class ViewHolder
	{
		public ImageView icon;
		public TextView name,rb,size;
		public CheckBox check;
		public TextView date;
		public ViewHolder(View v)
		{
			icon = (ImageView)v.findViewById(R.id.item_filedataview_ficon);
			name = (TextView)v.findViewById(R.id.item_filedataview_fname);
			date = (TextView)v.findViewById(R.id.item_filedataview_fdate);
			rb = (TextView)v.findViewById(R.id.item_filedataview_frb);
			size = (TextView)v.findViewById(R.id.item_filedataview_fsize);
			check = (CheckBox)v.findViewById(R.id.itemfiledataview_checkbox);
		}
	}
}

class pathList extends Dialog
{
	public pathList(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_rootselect,null);
		super.setContentView(v);
		button = new View[]{
			findViewById(R.id.dialog_rootselect_return),
			findViewById(R.id.dialog_rootselect_select),
		};
		root = new rootList
		(
			context,
			(ListView)findViewById(R.id.dialog_rootselect_list),
			new ArrayList<File>()
		);
		button[0].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				root.returnUpRoot();
			}
		});
		button[1].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				dismiss();
				select();
			}
		});
	}
	
	public View[] button;
	public rootList root;
	
	public void setPath(File fp)
	{
		((TextView)findViewById(R.id.dialog_rootselect_path)).setText(fp.getPath());
	}
	
	public void select()
	{
	}
	
	class rootList extends ArrayAdapter<File>
	{
		public rootList(Context context,ListView view,List<File> data)
		{
			super(context,0,data);
			this.view = view;
			this.data = data;
			this.view.setAdapter(this);
			fp = new File("/storage/emulated/0");
			setPath(fp);
			loadRoot();
			notifyDataSetChanged();
		}
		
		public ListView view;
		public List<File> data;
		public File fp;

		public void setFp(File fp)
		{
			this.fp = fp;
		}

		public File getFp()
		{
			return fp;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView v;
			if(convertView==null)
			{
				v = (TextView)LayoutInflater.from(parent.getContext())
					.inflate(R.layout.textview_dri,null,false);
			}
			else
			{
				v = (TextView)convertView;
			}
			
			final File fp = getItem(position);
			v.setText(fp.getName());
			
			v.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					setFp(fp);
					setPath(fp);
					loadRoot();
					notifyDataSetChanged();
				}
			});
			
			return v;
		}
		
		private void returnUpRoot()
		{
			if(fp.getPath().equals("/"))
			{
				uitool.toos(getContext(),"没有上一级了");
				return;
			}
			setFp(fp.getParentFile());
			setPath(fp);
			loadRoot();
			notifyDataSetChanged();
		}
		
		private void loadRoot()
		{
			data.clear();
			for(File f:fp.listFiles())
			{
				if(f.isDirectory()&&f.canRead()&&f.canWrite())
					data.add(f);
			}
		}
	}
	
}

class RootBook extends Dialog
{
	public RootBook(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.listview_popbook,null);
		super.setContentView(v);
		rootlist = new RootList(context,(ListView)v,dbtool.initDri());
	}
	
	public void select(String path)
	{
	}
	
	public RootList rootlist;
	
	class RootList extends ArrayAdapter<DriLayout.Data>
	{
		public RootList(Context context,ListView view,List<DriLayout.Data> data)
		{
			super(context,0,data);
			this.view = view;
			this.data = data;
			this.view.setAdapter(this);
		}
		
		public ListView view;
		public List<DriLayout.Data> data;

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v;
			if(convertView==null)
			{
				v = LayoutInflater.from(getContext())
					.inflate(R.layout.item_dribook,null,false);
			}
			else
			{
				v = convertView;
			}
			
			final DriLayout.Data d = getItem(position);
			
			v.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					dismiss();
					select(d.getPath());
				}
			});
			((TextView)v.findViewById(R.id.item_dribook_name)).setText(d.getName());
			((TextView)v.findViewById(R.id.item_dribook_path)).setText(d.getPath());
			
			return v;
		}
	}
}

class copyThread extends Thread
{
	private Search context;
	private File fp,to;

	public copyThread set(Search context, File fp, File to)
	{
		this.context = context;
		this.fp = fp;
		this.to = to;
		return this;
	}
	
	@Override
	public void run()
	{
		if(to.exists())
		{
			Message msg = new Message();
			msg.what = Search.TOAS;
			msg.obj = "已有同名文件";
			context.UpdateUi.sendMessage(msg);
			return;
		}
		if(!fp.canRead())
		{
			Message msg = new Message();
			msg.what = Search.TOAS;
			msg.obj = "文件不可读";
			context.UpdateUi.sendMessage(msg);
			return;
		}
		Message msg = new Message();
		msg.what = Search.OPEN_DIALOG;
		context.UpdateUi.sendMessage(msg);
		
		filetool.copyFile(to.getParentFile(),fp);
	
		msg = new Message();
		msg.what = Search.TOAS;
		msg.obj = "复制完成";
		context.UpdateUi.sendMessage(msg);
		
		msg = new Message();
		msg.what = Search.CLOSE_DIALOG;
		context.UpdateUi.sendMessage(msg);
	}
}

class ZipDialog extends Dialog
{
	public ZipDialog(final Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_zipcompress,null);
		super.setContentView(v);
		findViewById(R.id.dialog_zipcompress_radio1).setVisibility(View.GONE);
		radio = new RadioButton[]{
			(RadioButton)findViewById(R.id.dialog_zipcompress_radio2),
			(RadioButton)findViewById(R.id.dialog_zipcompress_radio3),
		};
		radio[0].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				new pathList(context){
					@Override
					public void select()
					{
						ZipDialog.this.setPath(root.getFp().getPath());
					}
				}.show();
				radio[0].setChecked(true);
				radio[1].setChecked(false);
			}
		});
		radio[1].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				new RootBook(context){
					@Override
					public void select(String path)
					{
						setPath(path);
					}
				}.show();
				radio[1].setChecked(true);
				radio[0].setChecked(false);
			}
		});
		findViewById(R.id.dialog_zipcompress_exit)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});
		findViewById(R.id.dialog_zipcompress_start)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				dismiss();
				start();
			}
		});
		path = (EditText)findViewById(R.id.dialog_zipcompress_path);
		name = (EditText)findViewById(R.id.dialog_zipcompress_name);
		setPath("/storage/emulated/0");
		setName("新建压缩包.zip");
		activity = (Search)context;
	}
	
	private List<File> fp = null;
	private EditText path,name;
	private RadioButton radio[];
	private Search activity;

	private void setPath(String path)
	{
		this.path.setText(path);
	}
	
	private void setName(String name)
	{
		this.name.setText(name);
	}
	
	public ZipDialog setFp(File fp)
	{
		this.fp = new ArrayList<>();
		this.fp.add(fp);
		setName(fp.getName()+".zip");
		return this;
	}
	
	public ZipDialog setFp(List<File> fp)
	{
		this.fp = fp;
		setName("新建压缩包.zip");
		return this;
	}
	
	private File to;
	public void start()
	{
		to = new File(path.getText().toString(),name.getText().toString());
		if(to.exists())
		{
			uitool.toos(getContext(),"已有同名文件");
			return;
		}
		String error = "";
		for(File f:fp)
		{
			if(f.canRead())
			{
				error = error + f.getName() + "不可读取\n";
			}
		}
		if(error.length()==0)
		{
			uitool.toos(getContext(),error);
		}
		activity.dialog = new Progeress(activity);
		activity.dialog.show();
		new Thread(new Runnable(){
			@Override
			public void run()
			{
				if(fp.size()==1)
				{
					try
					{
						ziptool.zipCompress(to, fp.get(0));
					}
					catch (Exception e)
					{}
				}
				else
				{
					try
					{
						ziptool.zipCompress(to, fp);
					}
					catch (Exception e)
					{}
				}
				Message msg = new Message();
				msg.what = Search.THREAD_UI;
				msg.obj = new Search.Thread_Ui(){
					@Override
					public void onRun()
					{
						activity.dialog.dismiss();
						activity.dialog = null;
						uitool.toos(activity,"压缩完成");
					}
				};
				activity.UpdateUi.sendMessage(msg);
			}
		}).start();
	}
}
