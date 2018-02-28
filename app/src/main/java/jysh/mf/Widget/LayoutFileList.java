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
import jysh.mf.Dialog.*;
import android.widget.CompoundButton.*;
import jysh.mf.Activity.*;

public class LayoutFileList extends LinearLayout
{
	public LayoutFileList(final Context context,AttributeSet attr)
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
		listadp.path.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				ClipboardManager cmb = (ClipboardManager)getContext()
					.getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setPrimaryClip(ClipData.newPlainText(null,listadp.path.getText()));
				uitool.toos(context,"路径已复制到剪切板");
			}
		});
	}
	
	public FileList listadp;
	public static final int UPDATE = 1;
	
	public boolean Backpressed()
	{
		return listadp.returnDri();
	}
	
	public class FileList extends ArrayAdapter<ViewData>
	implements OnItemClickListener,OnScrollListener,OnItemLongClickListener
	{
		public FileList(Context context,ListView list,List<ViewData> obj)
		{
			super(context,resId,obj);
			data = obj;
			fp = new File("/storage/emulated/0");
			data.addAll(load());
			this.list = list;
			this.list.setAdapter(this);
			this.list.setOnItemClickListener(this);
			this.list.setOnScrollListener(this);
			this.list.setOnItemLongClickListener(this);
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
			else if(AppSet.isShowImages())
			{
				holder.icon.setImageResource(f.getIcon());
			}
			else if(f.getBmp()==null)
			{
				// 设置默认图标
				holder.icon.setImageResource(f.getIcon());
				// 加载缩略图
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
			
			if(SelectLayout.isSelectFile()&&!f.getFp().isDirectory())
			{
				final SelectLayout select = uitool.drawlayout.select;
				holder.select.setVisibility(View.VISIBLE);
				holder.select.setChecked(f.isSelect());
				holder.select.setOnClickListener(
				new View.OnClickListener(){
					@Override
					public void onClick(View v)
					{
						f.setSelect(!f.isSelect());
						if(f.isSelect())
						{
							select.data.add(new SelectLayout.Data(f.getFp()));
						}
						else
						{
							select.data.remove((Object)new SelectLayout.Data(f.getFp()));
						}
						uitool.popselect.show();
					}
				});
			}
			else
			{
				holder.select.setVisibility(View.GONE);
			}
			
			return v;
		}

		@Override
		public void onItemClick(AdapterView<?> p1, View p2, int position, long p4)
		{
			File f = data.get(position).getFp();
			if(!f.isDirectory())
			{
				filetool.fileOpens(getContext(),f);
				return;
			}
			stackScroll.add(new Float(list.getFirstVisiblePosition()));
			setFp(data.get(position).getFp());
			loadList();
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> p1, View view, int position, long p4)
		{
			final ViewData d = getItem(position);
			final int posit = position;
			new OperateFile(getContext(),d.getFp())
				// 多选
				.setSelect(new OperateFile.OnClick(){
					@Override
					public void onClick()
					{
						SelectLayout.setSelectFile(true);
						d.setSelect(true);
						final SelectLayout select = uitool.drawlayout.select;
						if(!d.getFp().isDirectory())
						{
							select.data.remove((Object) new SelectLayout.Data(d.getFp()));
							select.data.add(new SelectLayout.Data(d.getFp()));
						}
						notifyDataSetChanged();
						uitool.popselect.show();
					}
				})
				// 打开方式
				// 重命名
				.setReanme(new OperateFile.OnClick(){
					@Override
					public void onClick()
					{
						final EditBox edit = new EditBox(getContext());
							edit
							.setMessage(d.getFp().getName())
							.setTitle("重命名")
							.setLeft("取消")
							.setRight("确认")
							.setRight(new EditBox.onButton(){
								@Override
								public void onClick()
								{
									File fp = d.getFp();
									if(new File(fp.getParent(),edit.getMessage()).exists())
									{
										uitool.toos(uitool.mainThis,"冲突的命名");
										return;
									}
									File newFname = new File(fp.getParent(),edit.getMessage());
									if(fp.renameTo(newFname))
									{
										uitool.toos(uitool.mainThis,"重命名成功");
										d.setFp(newFname);
										d.setName(newFname.getName());
										return;
									}
									uitool.toos(uitool.mainThis,"重命名失败");
								}
							})
							.show();
					}
				})
				// 删除
				.setDelete(new OperateFile.OnClick(){
					@Override
					public void onClick()
					{
						new MessageBox(getContext())
							.setTitle("正在删除")
							.setMessage("文件删除，此操作无法逆转")
							.setLeft("取消")
							.setRight("删除")
							.setRight(new MessageBox.onButton(){
								@Override
								public void onClick()
								{
									filetool.deleteFile(d.getFp());
									if(d.getFp().exists())
									{
										uitool.toos(uitool.mainThis,"删除失败");
										return;
									}
									data.remove(posit);
									notifyDataSetChanged();
									uitool.toos(uitool.mainThis,"删除成功");
								}
							})
							.show();
					}
				})
				// 移动
				.setMove(new OperateFile.OnClick(){
					@Override
					public void onClick()
					{
						new Buttons()
							.setCall(R.drawable.ic_move,new Buttons.OnClick(){
								@Override
								public void OnClick()
								{
									uitool.progerss.show();
									new Thread(new Runnable(){
										@Override
										public void run()
										{
											List<LayoutFileList> v = uitool.pagerAdapter.view;
											File fileTo = v.get(uitool.add.getPosition()).listadp.getFp();
											File fp = d.getFp();
											if(filetool.moveFile(fileTo,fp))
											{
												Message toas = new Message();
												toas.what = uitool.TOAS;
												toas.obj = "移动成功";
												uitool.mainThis.UpdateUi.sendMessage(toas);
											}
											else
											{
												Message toas = new Message();
												toas.what = uitool.TOAS;
												toas.obj = "移动失败";
												uitool.mainThis.UpdateUi.sendMessage(toas);
											}
											
											Message msg = new Message();
											msg.what = Progeress.DISMISS;
											msg.obj = v;
											uitool.mainThis.UpdateUi.sendMessage(msg);
										}
									}).start();
								}
							})
							.setCancel(R.drawable.ic_exit,null);
					}
				})
				// 复制
				.setCopy(new OperateFile.OnClick(){
					@Override
					public void onClick()
					{
						new Buttons()
							.setCall(R.drawable.ic_copy,new Buttons.OnClick(){
								@Override
								public void OnClick()
								{
									uitool.progerss.show();
									new Thread(new Runnable(){
										@Override
										public void run()
										{
											List<LayoutFileList> view = uitool.pagerAdapter.view;
											File fileTo = view.get(uitool.add.getPosition()).listadp.getFp();
											File fp = d.getFp();
											if(new File(fileTo,fp.getName())!=null&&new File(fileTo,fp.getName()).exists())
											{
												Message msg = new Message();
												msg.what = uitool.TOAS;
												msg.obj = "已有同名文件";
												uitool.mainThis.UpdateUi.sendMessage(msg);
											}
											else
											{
												filetool.copyFile(fileTo,fp);
											}
											Message msg = new Message();
											msg.what = Progeress.DISMISS;
											msg.obj = view;
											uitool.mainThis.UpdateUi.sendMessage(msg);
										}
									}).start();
								}
							})
							.setCancel(R.drawable.ic_exit,null);
					}
				})
				// 添加到书签
				// 压缩
				.setZip(new OperateFile.OnClick(){
					@Override
					public void onClick()
					{
						new ZipCompress(getContext()).setFp(d.getFp()).show();
					}
				})
				// 发送
				// 属性
				.show();
			return true;
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
		
		public void loadList()
		{
			if(fp==null||!fp.exists())
			{
				fp = new File("/storage/emulated/0");
			}
			data.clear();
			data.addAll(load());
			path.setText(fp.getPath());
			list.setSelection(0);
			notifyDataSetChanged();
		}
		
		private List<ViewData> load()
		{
			List<ViewData> data = new ArrayList<>();
			// 文件夹置顶 & 不显示.开头的目录
			if(AppSet.isShowDriUp() && AppSet.isShowHidden())
			{
				List<ViewData> dri = new ArrayList<>();
				List<ViewData> file = new ArrayList<>();
				for(File f:getFp().listFiles())
				{
					if(f.getName().charAt(0)=='.')
					{
						continue;
					}
					if(f.isDirectory())
					{
						dri.add(new ViewData(f));
					}
					else
					{
						file.add(new ViewData(f));
					}
				}
				data.addAll(dri);
				data.addAll(file);
			}
			// 文件夹置顶
			else if(AppSet.isShowDriUp())
			{
				List<ViewData> dri = new ArrayList<>();
				List<ViewData> file = new ArrayList<>();
				for(File f:getFp().listFiles())
				{
					if(f.isDirectory())
					{
						dri.add(new ViewData(f));
					}
					else
					{
						file.add(new ViewData(f));
					}
				}
				data.addAll(dri);
				data.addAll(file);
			}
			// 隐藏.开头的文件
			else if(!AppSet.isShowHidden())
			{
				for(File f:getFp().listFiles())
				{
					if(f.getName().charAt(0)=='.')
					{
						continue;
					}
					data.add(new ViewData(f));
				}
			}
			// 默认显示
			else
			{
				for(File f:getFp().listFiles())
				{
					data.add(new ViewData(f));
				}
			}
			return data;
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
		private long size_t;

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
				size_t = fp.lastModified();
			}
			name = fp.getName();
			date = filetool.getFileDate(fp.lastModified());
			rb = filetool.getFileRw(fp);
			
			if(SelectLayout.isSelectFile())
			{
				for(SelectLayout.Data f:uitool.drawlayout.select.data)
				{
					if(fp.equals((Object)f.getFp()))
					{
						select = true;
						return;
					}
				}
			}
			select = false;
		}

		public void setSize_t(long size_t)
		{
			this.size_t = size_t;
		}

		public long getSize_t()
		{
			return size_t;
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
