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
	
	private FileList listadp;
	
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
			fp = new File("/storage/emulated/0");
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
			
			ViewData f = data.get(position);
			holder.icon.setImageResource(f.getIcon());
			holder.name.setText(f.getName());
			holder.date.setText(f.getDate());
			holder.size.setText(f.getSize());
			holder.rb.setText(f.getRb());
			
			return v;
		}

		@Override
		public void onItemClick(AdapterView<?> p1, View p2, int position, long p4)
		{
			File f = data.get(position).getFp();
			if(!f.isDirectory())
			{
				return;
			}
			stackScroll.add(new Float(list.getFirstVisiblePosition()));
			setFp(data.get(position).getFp());
			loadList();
		}

		@Override
		public void onScroll(AbsListView p1, int p2, int p3, int p4)
		{
			// TODO: Implement this method
		}

		@Override
		public void onScrollStateChanged(AbsListView p1, int p2)
		{
			// TODO: Implement this method
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
			notifyDataSetChanged();
		}
		
		public List<ViewData> data;
		private File fp;
		private TextView path;
		private List<Float> stackScroll;
		private static final int resId = R.layout.item_filedataview;
		private ListView list;

		public void setPath(TextView path)
		{
			this.path = path;
			this.path.setText(fp.getPath());
		}

		public void setFp(File fp)
		{
			this.fp = fp;
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
		}
		public ImageView icon;
		public TextView name,date,size,rb;
	}
	
	static public class ViewData
	{
		private int icon;
		private String name,size,date,rb;
		private File fp;

		public ViewData(File fp)
		{
			this.fp = fp;
			if(fp.isDirectory())
				icon = R.drawable.ic_dri;
			else
				icon = filetool.getFiconRes(fp.getName());
			name = fp.getName();
			date = "时间";
			size = fp.length()+"字节";
			if(fp.canRead())
				rb = "R";
			else
				rb = "_";
			if(fp.canWrite())
				rb+="W";
			else
				rb+="_";
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
}
