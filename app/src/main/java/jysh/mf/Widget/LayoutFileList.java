package jysh.mf.Widget;
import android.widget.*;
import android.content.*;
import jysh.mf.R;
import android.view.*;
import java.util.*;
import java.io.*;
import jysh.mf.Util.*;
import android.util.*;
import android.graphics.*;
import android.widget.AbsListView.*;
import android.app.Activity;
import android.os.*;
import jysh.mf.Adapter.*;

public class LayoutFileList extends LinearLayout
{
	public LayoutFileList(Context context,AttributeSet attr)
	{
		super(context,attr);
		LayoutInflater.from(context).inflate(R.layout.widget_flist,this);
		listadp = new FileList
		(
			getContext(),
			(ListView)findViewById(R.id.widget_flist_list),
			new ArrayList<ViewData>()
		);
		listadp.setPath((TextView)findViewById(R.id.widget_flist_path));
	}
	
	public FileList listadp;
	public static final int UPDATE = 1;
	
	public boolean Backpressed()
	{
		return listadp.returnDri();
	}
	
	public class FileList extends ArrayAdapter<ViewData>
		implements OnItemClickListener,OnScrollListener
	{
		public FileList(Context context,ListView list,List<ViewData> obj)
		{
			super(context,resId,obj);
			data = obj;
			fp = new File("/storage/emulated/0/tencent/QQLite/head/_hd");
			if(fp==null)
			{
				fp = new File("/storage/emulated/0/DCIM/Camera");
			}
			for(File f:fp.listFiles())
			{
				data.add(new ViewData(f));
			}
			this.list = list;
			this.list.setAdapter(this);
			this.list.setOnItemClickListener(this);
			this.list.setOnScrollListener(this);
			stackScroll = new ArrayList<>();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v;
			ViewHolder holder;
			if(convertView==null)
			{
				v = LayoutInflater.from(getContext())
					.inflate(resId,parent,false);
				holder = new ViewHolder(v);
				v.setTag(holder);
			}
			else
			{
				v = convertView;
				holder = (ViewHolder)v.getTag();
			}
			
			final ViewData f = data.get(position);
			
			if(f.getIcon()!=R.drawable.ic_image)
			{
				holder.icon.setImageResource(f.getIcon());
			}
			else if(f.getBmp()==null)
			{
				holder.icon.setImageResource(f.getIcon());
				// 这里有个if
				if(position < scrollsEndItem + 12 && position > scrollsEndItem - 2)
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						f.setBmp(filetool.getBitmap(f.getFp()));
						Message mes = new Message();
						mes.what = UPDATE;
						uitool.mainThis.UpdateUi.sendMessage(mes);
					}
				}).start();
			}
			else
			{
				holder.icon.setImageBitmap(f.getBmp());
			}
			
			holder.name.setText(f.getName());
			holder.date.setText(f.getDate());
			holder.size.setText(f.getSize());
			holder.rb.setText(f.getRb());
			
			if(SelectLayout.isSelectFile())
			{
				holder.select.setChecked(f.isSelect());
			}
				
			return v;
		}

		@Override
		public void onItemClick(AdapterView<?> p1, View p2, int position, long p4)
		{
			File f = data.get(position).getFp();
			ViewData vd = data.get(position);
			if(!f.isDirectory())
			{
				if(SelectLayout.isSelectFile())
				{
					vd.setSelect(!vd.isSelect());
					notifyDataSetChanged();
				}
				return;
			}
			stackScroll.add(new Float(list.getFirstVisiblePosition()));
			setFp(data.get(position).getFp());
			loadList();
		}

		@Override
		public void onScroll(AbsListView p1, int p2, int p3, int p4)
		{
		}

		@Override
		public void onScrollStateChanged(AbsListView p1, int p2)
		{
			switch(p2)
			{
				case SCROLL_STATE_IDLE:
					scrollsEndItem = list.getFirstVisiblePosition();
					for(int i = 0;i < data.size();i++)
					{
						if(i < scrollsEndItem - 2 || i > scrollsEndItem + 12)
						{
							data.get(i).setBmp(null);
						}
					}
					Message msg = new Message();
					msg.what = UPDATE;
					uitool.mainThis.UpdateUi.sendMessage(msg);
					break;
			}
		}
		
		public boolean returnDri()
		{
			if("/".equals(fp.getPath()))
				return true;
			String path = fp.getParent();
			fp = new File(path);
			loadList();
			if(stackScroll.size()!=0)
			{
				Float srcll = stackScroll.get(stackScroll.size()-1);
				stackScroll.remove(stackScroll.size()-1);
				this.list.setSelection(srcll.intValue());
			}
			return false;
		}
		
		private void loadList()
		{
			data.clear();
			for(File f:fp.listFiles())
			{
				data.add(new ViewData(f));
			}
			path.setText(fp.getPath());
			list.setSelection(0);
			notifyDataSetChanged();
		}
		
		public List<ViewData> data;
		private File fp;
		private TextView path;
		private List<Float> stackScroll;
		private static final int resId = R.layout.item_filedataview;
		public ListView list;
		private int scrollsEndItem = 0;

		public void setPath(TextView path)
		{
			this.path = path;
			this.path.setText(fp.getPath());
		}

		public void setFp(File fp)
		{
			this.fp = fp;
		}
		
		public File getFp()
		{
			return fp;
		}
	}
	
	public class ViewHolder
	{
		public ViewHolder(View v)
		{
			icon = (ImageView)v.findViewById(R.id.item_filedataview_ficon);
			name = (TextView)v.findViewById(R.id.item_filedataview_fname);
			date = (TextView)v.findViewById(R.id.item_filedataview_fdate);
			size = (TextView)v.findViewById(R.id.item_filedataview_fsize);
			rb = (TextView)v.findViewById(R.id.item_filedataview_frb);
			select = (CheckBox)v.findViewById(R.id.itemfiledataview_checkbox);
		}
		public ImageView icon;
		public TextView name,date,size,rb;
		public CheckBox select;
	}
	
	static public class ViewData
	{
		private int icon;
		private String name,size,date,rb;
		private File fp;
		private Bitmap bmp;
		private boolean select;

		public ViewData(File fp)
		{
			this.fp = fp;
			if(fp.isDirectory())
			{
				icon = R.drawable.ic_dri;
				size = "";
			}
			else
			{
				icon = filetool.getFiconRes(fp.getName());
				size = filetool.getFileSize(fp.length());
			}
			name = fp.getName();
			date = filetool.getFileDate(fp.lastModified());
			rb = filetool.getFileRw(fp);
			select = false;
		}

		public void setSelect(boolean select)
		{
			this.select = select;
		}

		public boolean isSelect()
		{
			return select;
		}

		public void setBmp(Bitmap bmp)
		{
			this.bmp = bmp;
		}

		public Bitmap getBmp()
		{
			return bmp;
		}

		public void setIcon(int icon)
		{
			this.icon = icon;
		}

		public int getIcon()
		{
			return icon;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}

		public void setSize(String size)
		{
			this.size = size;
		}

		public String getSize()
		{
			return size;
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

		public void setFp(File fp)
		{
			this.fp = fp;
		}

		public File getFp()
		{
			return fp;
		}
	}
	
	public ViewData get(int position)
	{
		return listadp.data.get(position);
	}

	public void set(int position,ViewData obj)
	{
		listadp.data.set(position,obj);
	}

	public void Add(ViewData obj)
	{
		listadp.data.add(obj);
	}
	
	public void clear()
	{
		listadp.data.clear();
	}
	
	public void notifyDataSetChanged()
	{
		listadp.notifyDataSetChanged();
	}
}
