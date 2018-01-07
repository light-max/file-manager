package jysh.mf.Widget;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Dialog.*;
import jysh.mf.Util.*;

public class FileLayout extends RecyclerView.Adapter<FileLayout.ViewHolder>
{
	public FileLayout(RecyclerView view)
	{
		this.view = view;
		LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		this.view.setLayoutManager(layoutManager);
		this.view.setAdapter(this);
	}
	
	public void loadDatabase()
	{
		data = dbtool.initFile();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup pager,int position)
	{
		final View v = LayoutInflater.from(pager.getContext())
			.inflate(R.layout.item_filebook,pager,false);
		final ViewHolder holder = new ViewHolder(v);
		v.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view)
			{
				final Data d = data.get(holder.getPosition());
				final File fp = new File(d.getPath());
				if(!fp.isFile())
				{
					new MessageBox(v.getContext())
						.setTitle("是否删除")
						.setMessage("这个文件是无效的")
						.setLeft("取消")
						.setRight("确认")
						.setRight(new MessageBox.onButton(){
							@Override
							public void onClick()
							{
								dbtool.deleteFile(d);
								data.remove(holder.getPosition());
								notifyDataSetChanged();
							}
						})
						.show();
					return;
				}
				uitool.toos(uitool.mainThis,"正在跳转...");
				LayoutFileList listview = new LayoutFileList(v.getContext(),null);
				listview.listadp.setFp(fp.getParentFile());
				listview.listadp.loadList();
				int s = 0;
				for(LayoutFileList.ViewData vd:listview.listadp.data)
				{
					if(fp.equals(vd.getFp()))
					{
						break;
					}
					s++;
				}
				listview.listadp.list.setSelection(s);
				uitool.add.opentitle(listview);
				uitool.mainDrawer.closeDrawers();
			}
		});
		v.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v)
			{
				final OperateBook d = new OperateBook(uitool.mainThis);
				d.bt1.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v)
					{
						final Data dat = data.get(holder.getPosition());
						final EditBox edit = new EditBox(uitool.mainThis);
						edit.setTitle("重命名,输入新的备注")
							.setMessage(dat.getName())
							.setLeft("取消")
							.setRight("确认")
							.setRight(new EditBox.onButton(){
								@Override
								public void onClick()
								{
									d.dismiss();
									dbtool.updateFileName(dat,edit.getMessage());
									dat.setName(edit.getMessage());
									notifyItemChanged(holder.getPosition());
								}
							})
							.show();
					}
				});
				d.bt2.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v)
					{
						d.dismiss();
						dbtool.deleteFile(data.get(holder.getPosition()));
						data.remove(holder.getPosition());
						notifyDataSetChanged();
					}
				});
				d.show();
				return true;
			}
		});
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int p2)
	{
		Data d = data.get(holder.getPosition());
		holder.name.setText(d.getName());
		holder.path.setText(d.getPath());
	}

	@Override
	public int getItemCount()
	{
		return data.size();
	}
	
	public RecyclerView view;
	public List<Data> data;
	
	static public class Data
	{
		private long time;
		private String name,path;

		public Data(long time, String name, String path)
		{
			this.time = time;
			this.name = name;
			this.path = path;
		}

		public void setTime(long time)
		{
			this.time = time;
		}

		public long getTime()
		{
			return time;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}

		public void setPath(String path)
		{
			this.path = path;
		}

		public String getPath()
		{
			return path;
		}
	}
	
	static public class ViewHolder extends RecyclerView.ViewHolder
	{
		public TextView name,path;
		
		public ViewHolder(View v)
		{
			super(v);
			name = (TextView)v.findViewById(R.id.item_filebook_name);
			path = (TextView)v.findViewById(R.id.item_filebook_path);
		}
	}
}
