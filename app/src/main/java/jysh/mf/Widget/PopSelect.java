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
import android.graphics.drawable.*;

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
				new MessageBox(getContext())
					.setTitle("复制文件")
					.setMessage("你确定把文件复制在这个目录吗")
					.setLeft("取消")
					.setRight("确认复制")
					.setRight(new MessageBox.onButton(){
						@Override
						public void onClick()
						{
							uitool.progerss.show();
							filetool.copyFile();
						}
					})
					.show();
			}
		};
		move = new OnClick(){
			@Override
			public void onClick()
			{
				new MessageBox(getContext())
					.setTitle("移动文件")
					.setMessage("你确定要把这些文件移动在这个\n目录吗")
					.setLeft("取消")
					.setRight("确认移动")
					.setRight(new MessageBox.onButton(){
						@Override
						public void onClick()
						{
							filetool.moveFile();
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
					if(f.getFp().isDirectory())
					{
						continue;
					}
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
				PopSelectMenu menu = null;
				PopupWindow pop = new PopupWindow
				(
					menu = new PopSelectMenu(getContext(),null),
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT
				);
				pop.setBackgroundDrawable(new BitmapDrawable());
				pop.setFocusable(true);
				pop.showAsDropDown(findViewById(id[5]),0,-500);
				menu.setPop(pop);
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
}
