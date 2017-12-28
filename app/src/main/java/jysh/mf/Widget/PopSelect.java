package jysh.mf.Widget;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Dialog.*;
import jysh.mf.Util.*;
import android.os.*;

public class PopSelect extends LinearLayout implements View.OnClickListener
{
	public PopSelect(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.pop_select, this);
		for (int id:this.id)
		{
			findViewById(id).setOnClickListener(this);
		}
		size = (TextView)findViewById(R.id.popselect_size);
		size.setText("0个对象");
		exit = new OnClick(){
			@Override
			public void onClick()
			{
				cancel();
			}
		};
		copy = new OnClick(){
			@Override
			public void onClick()
			{
				uitool.progerss.show();
				cancel();
			}
		};
		move = new OnClick(){
			@Override
			public void onClick()
			{
				new MessageBox(getContext())
					.setTitle("移动文件")
					.setMessage("(此操作无法逆转，请慎重操作)")
					.setLeft("取消")
					.setRight("确认移动")
					.setRight(new MessageBox.onButton(){
						@Override
						public void onClick()
						{
							uitool.progerss.show();
							
							filetool.fileTo = uitool.pagerAdapter.get(uitool.add.getPosition()).listadp.getFp();

							for(SelectLayout.Data f:uitool.drawlayout.select.data)
							{
								filetool.moveFile(f.getFp());
							}
									
							for(LayoutFileList ff:uitool.pagerAdapter.view)
							{
								ff.listadp.loadList();
							}
							cancel();
						}
					})
					.show();
			}
		};
		delete = new OnClick(){
			@Override
			public void onClick()
			{
				new MessageBox(getContext())
					.setTitle("删除文件")
					.setMessage("(此操作无法逆转，请谨慎从事)")
					.setLeft("取消")
					.setRight("确认删除")
					.setRight(new MessageBox.onButton(){
						@Override
						public void onClick()
						{
							for(SelectLayout.Data f:uitool.drawlayout.select.data)
							{
								filetool.deleteFile(f.getFp());
							}
							for(LayoutFileList f:uitool.pagerAdapter.view)
							{
								f.listadp.loadList();
							}
							cancel();
						}
					})
					.show();
			}
		};
		qx = new OnClick(){
			@Override
			public void onClick()
			{
				LayoutFileList view = uitool.pagerAdapter.get(uitool.add.getPosition());
				List<LayoutFileList.ViewData> data = view.listadp.data;
				for(LayoutFileList.ViewData f:data)
				{
					Object obj = new SelectLayout.Data(f.getFp());
					uitool.drawlayout.select.data.remove(obj);
					f.setSelect(true);
					uitool.drawlayout.select.data.add(new SelectLayout.Data(f.getFp()));
				}
				view.notifyDataSetChanged();
				uitool.popselect.show();
			}
		};
		menu = new OnClick(){
			@Override
			public void onClick()
			{
				
			}
		};
	}

	private final static int id[] = new int[]{
		R.id.popselect_exit,
		R.id.popselect_copy,
		R.id.popselect_move,
		R.id.popselect_delete,
		R.id.popselect_qx,
		R.id.popselect_menu,
	};
	private TextView size;
	private OnClick exit,copy,move,delete,qx,menu;

	public PopSelect setExit(OnClick exit)
	{
		this.exit = exit;
		return this;
	}

	public PopSelect setCopy(OnClick copy)
	{
		this.copy = copy;
		return this;
	}

	public PopSelect setMove(OnClick move)
	{
		this.move = move;
		return this;
	}

	public PopSelect setDelete(OnClick delete)
	{
		this.delete = delete;
		return this;
	}

	public PopSelect setQx(OnClick qx)
	{
		this.qx = qx;
		return this;
	}

	public PopSelect setMenu(OnClick menu)
	{
		this.menu = menu;
		return this;
	}

	public PopSelect setSize(String size)
	{
		this.size.setText(size);
		return this;
	}

	@Override
	public void onClick(View v)
	{
		int vid = v.getId();
		if (vid == id[0] && exit != null)
			exit.onClick();
		else if (vid == id[1] && copy != null)
			copy.onClick();
		else if (vid == id[2] && move != null)
			move.onClick();
		else if (vid == id[3] && delete != null)
			delete.onClick();
		else if (vid == id[4] && qx != null)
			qx.onClick();
		else if (vid == id[5] && menu != null)
			menu.onClick();
	}

	public static interface OnClick
	{
		public void onClick();
	}

	public void show()
	{
		setVisibility(View.VISIBLE);
		setSize(uitool.drawlayout.select.data.size() + "个对象");
	}
	
	public void cancel()
	{
		setVisibility(View.GONE);
		LayoutFileList showView = uitool.pagerAdapter.get(uitool.add.getPosition());
		uitool.drawlayout.select.cancelSelect();
		showView.notifyDataSetChanged();
	}
	
	// 把文件按目录的深度排列,,以免被选中的文件夹中的文件先进行操作
	public List<File> getFile()
	{
		int start = 20,end = 0;
		List<File> fp = new ArrayList<>();
		List<SelectLayout.Data> select = uitool.drawlayout.select.data;
		// 先查找最短路径和最长路径
		for(SelectLayout.Data f:select)
		{
			int size = strMath(f.getFp().getPath());
			if(size<start)
			{
				start = size;
			}
			if(size>end)
			{
				end = size;
			}
		}
		// 缩短范围后就挨个添加
		for(int i = start;i <= end;i++)
		{
			for(SelectLayout.Data f:select)
			{
				if(strMath(f.getFp().getPath())==i)
				{
					fp.add(f.getFp());
				}
			}
		}
		return fp;
	}
	
	// 查看字符'/'在字符串中出现的次数,英语不好只能用这个名称了
	private int strMath(String str)
	{
		int len = 0;
		for(int i = 0;i < str.length();i++)
		{
			if(str.charAt(i)=='/')
				len++;
		}
		return len;
	}
}
