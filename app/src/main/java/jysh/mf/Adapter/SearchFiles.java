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
				filetool.fileOpens(getContext(),d.getFp());
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
							new pathList(getContext()){
								@Override
								public void select()
								{
									
								}
							}.show();
						}
					});
					select.bt2.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v)
						{}
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
						{}
					});
					select.bt2.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v)
						{}
					});
					select.show();
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
			new ArrayList<String>()
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
	
	class rootList extends ArrayAdapter<String>
	{
		public rootList(Context context,ListView view,List<String> data)
		{
			super(context,0,data);
			this.view = view;
			this.data = data;
			fp = new File("/storage/emulated/0");
			setPath(fp);
		}
		
		public ListView view;
		public List<String> data;
		public File fp;
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO: Implement this method
			return super.getView(position, convertView, parent);
		}
		
		private void returnUpRoot()
		{
			
		}

	}
	
}
