package jysh.mf.Widget;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Util.*;
import jysh.mf.Dialog.*;

public class DriLayout extends RecyclerView.Adapter<DriLayout.ViewHolder>
{
	public DriLayout(RecyclerView view)
	{
		this.view = view;
		LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		this.view.setLayoutManager(layoutManager);
		this.view.setAdapter(this);
	}
	
	public void loadDatabase()
	{
		data = dbtool.initDri();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup pager,final int type)
	{
		final View v = LayoutInflater.from(pager.getContext())
			.inflate(R.layout.item_dribook,pager,false);
		final ViewHolder holder = new ViewHolder(v);
		v.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view)
			{
				Data d = data.get(holder.getPosition());
				File fp = new File(d.getPath());
				if(!fp.isDirectory())
				{
					new MessageBox(v.getContext())
						.setTitle("是否删除")
						.setMessage("这个目录是无效的")
						.setLeft("取消")
						.setRight("确认")
						.setRight(new MessageBox.onButton(){
							@Override
							public void onClick()
							{
								dbtool.deleteDri(data.get(holder.getPosition()));
								data.remove(holder.getPosition());
								notifyDataSetChanged();
							}
						})
						.show();
					return;
				}
				uitool.toos(uitool.mainThis,"正在打开...");
				LayoutFileList listview = new LayoutFileList(v.getContext(),null);
				listview.listadp.setFp(fp);
				listview.listadp.loadList();
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
									dbtool.updateDriName(dat,edit.getMessage());
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
						dbtool.deleteDri(data.get(holder.getPosition()));
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
	
	public static class Data
	{
		private String name,path;
		private long time;
		public Data(long time,String name, String path)
		{
			this.name = name;
			this.path = path;
			this.time = time;
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
	
	static class ViewHolder extends RecyclerView.ViewHolder
	{
		public TextView name,path;
		public ViewHolder(View view)
		{
			super(view);
			name = (TextView)view.findViewById(R.id.item_dribook_name);
			path = (TextView)view.findViewById(R.id.item_dribook_path);
		}
	}
}
