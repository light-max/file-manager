package jysh.mf.Adapter;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import jysh.mf.*;
import jysh.mf.Util.*;

public class ZipFileList extends ArrayAdapter<ZipFileList.Data>
{
	public ZipFileList(ListView view,Context context,List<Data> data)
	{
		super(context,0,data);
		this.data = data;
		this.view = view;
		this.view.setAdapter(this);
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
			v = LayoutInflater.from(getContext())
				.inflate(R.layout.item_zip,null,false);
			holder = new ViewHolder(v);
			v.setTag(holder);
		}
		else
		{
			v = convertView;
			holder = (ViewHolder)v.getTag();
		}
		
		Data d = data.get(position);
		holder.name.setText(d.getName());
		holder.icon.setImageResource(d.getIcon());
		
		return v;
	}
	
	static public class Data
	{
		private String name;
		private int icon;
		private Bitmap bmp;
		private ZipEntry entry;

		public Data(ZipEntry entry)
		{
			this.entry = entry;
			if(entry.isDirectory())
			{
				icon = R.drawable.ic_dri;
			}
			else
			{
				icon = filetool.getFiconRes(entry.getName());
			}
			name = new File(entry.getName()).getName();
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
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
		}}
}

class ViewHolder
{
	public TextView name;
	public ImageView icon;
	
	public ViewHolder(View v)
	{
		name = (TextView)v.findViewById(R.id.item_zip_name);
		icon = (ImageView)v.findViewById(R.id.item_zip_icon);
	}
}
