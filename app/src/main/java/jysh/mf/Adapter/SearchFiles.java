package jysh.mf.Adapter;

import android.content.*;
import android.widget.*;
import java.util.*;
import android.view.*;
import jysh.mf.R;
import java.io.*;
import android.graphics.*;
import jysh.mf.Util.*;

public class SearchFiles extends ArrayAdapter<SearchFiles.Data>
{
	public SearchFiles(Context context,ListView view,List<Data> data)
	{
		super(context,0,data);
		this.view = view;
		this.view.setAdapter(this);
		this.data = data;
		for(File f:new File("/storage/emulated/0/AppProjects").listFiles())
		{
			this.data.add(new Data(f));
		}
	}
	
	public List<Data> data;
	public ListView view;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
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
		
		Data d = data.get(position);
		
		holder.icon.setImageResource(d.getIcon());
		holder.name.setText(d.getName());
		holder.date.setText(d.getDate());
		holder.rb.setText(d.getRb());
		holder.size.setText(d.getSize());
		
		return v;
	}
	
	static public class Data
	{
		private File fp;
		private int icon;
		private Bitmap bmp;
		private String name,date,rb,size;

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

