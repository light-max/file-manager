package jysh.mf.Widget;

import android.support.v7.widget.*;
import android.view.*;
import java.util.*;
import android.widget.*;
import jysh.mf.R;
import java.io.*;
import android.graphics.*;
import jysh.mf.Util.*;

public class SelectLayout extends RecyclerView.Adapter<ViewHolder>
{
	public SelectLayout(RecyclerView view)
	{
		this.view = view;
		LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		this.view.setLayoutManager(layoutManager);
		this.view.setAdapter(this);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int p)
	{
		View v = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.item_select,parent,false);
		final ViewHolder holder = new ViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int p)
	{
		Data d = data.get(p);
		holder.name.setText(d.getName());
		holder.icon.setImageResource(d.getIcon());
	}

	@Override
	public int getItemCount()
	{
		return data.size();
	}
	
	static public class Data
	{
		private File fp;
		private String name;
		private int icon;
		
		public Data(File fp)
		{
			this.fp = fp;
			icon = filetool.getFiconRes(fp.getName());
			name = fp.getName();
		}

		public void setFp(File fp)
		{
			this.fp = fp;
		}

		public File getFp()
		{
			return fp;
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
	}
	
	public RecyclerView view;
	public List<Data> data = new ArrayList<>();
	private static boolean selectFile = true;

	public static void setSelectFile(boolean selectFile)
	{
		SelectLayout.selectFile = selectFile;
	}

	public static boolean isSelectFile()
	{
		return SelectLayout.selectFile;
	}
	
}

class ViewHolder extends RecyclerView.ViewHolder
{
	public ViewHolder(View view)
	{
		super(view);
		icon = (ImageView)view.findViewById(R.id.itemselect_icon);
		name = (TextView)view.findViewById(R.id.itemselect_name);
	}
	
	public ImageView icon;
	public TextView name;
}
