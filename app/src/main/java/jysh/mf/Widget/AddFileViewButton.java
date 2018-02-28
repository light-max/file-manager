package jysh.mf.Widget;
import android.view.*;
import android.widget.*;
import android.app.*;
import jysh.mf.R;
import jysh.mf.Util.*;
import jysh.mf.Dialog.*;
import java.io.*;

public class AddFileViewButton implements View.OnClickListener
{
	public AddFileViewButton(Activity context)
	{
		this.context = context;
		button = (ImageButton)context.findViewById(R.id.main_add);
		button.setOnClickListener(this);
	}
	
	public void setPosition(int position)
	{
		this.position = position;
	}

	public int getPosition()
	{
		return position;
	}
	
	@Override
	public void onClick(View v)
	{
		new AddDialog(context)
			.setOnClick(new OnAddClick())
			.show();
	}
	
	private ImageButton button;
	private Activity context;
	private int position = 0;
	
	private class OnAddClick implements AddDialog.OnClick
	{
		@Override
		public void onClick(int viewId)
		{
			switch(viewId)
			{
				case R.id.dialognew_newfile:
					newfile();
					break;
				case R.id.dialognew_newdri:
					newdri();
					break;
				case R.id.dialognew_drititle:
					opentitle();
					break;
				case R.id.dialognew_savetitle:
					savetitle();
					break;
			}
		}
	}
	
	private void newfile()
	{
		final EditBox edit = new EditBox(uitool.mainThis);
			edit
			.setTitle("新建文件")
			.setLeft("取消")
			.setRight("创建")
			.setRight(new EditBox.onButton(){
				@Override
				public void onClick()
				{
					try
					{
						LayoutFileList f = uitool.pagerAdapter.get(uitool.add.getPosition());
						File fp = new File(f.listadp.getFp(),edit.getMessage());
						if(fp.getName().trim().length()==0)
						{
							uitool.toos(uitool.mainThis,"文件名有误");
							return;
						}
						if(fp.exists())
						{
							uitool.toos(uitool.mainThis,"已有同名文件");
							return;
						}
						fp.createNewFile();
						f.listadp.data.add(new LayoutFileList.ViewData(fp));
						f.notifyDataSetChanged();
						f.listadp.list.setSelection(f.listadp.data.size()-1);
						uitool.toos(uitool.mainThis,"创建成功");
					}
					catch (IOException e)
					{
						e.printStackTrace();
						uitool.toos(uitool.mainThis,"创建失败!");
					}
				}
			});
		edit.show();
	}
	
	private void newdri()
	{
		final EditBox edit = new EditBox(uitool.mainThis);
			edit
			.setTitle("新建文件夹")
			.setLeft("取消")
			.setRight("创建")
			.setRight(new EditBox.onButton(){
				@Override
				public void onClick()
				{
					LayoutFileList f = uitool.pagerAdapter.get(uitool.add.getPosition());
					File fp = new File(f.listadp.getFp(),edit.getMessage());
					if(fp.getName().trim().length()==0)
					{
						uitool.toos(uitool.mainThis,"文件名有误");
						return;
					}
					if(fp.exists())
					{
						uitool.toos(uitool.mainThis,"已有同名文件");
						return;
					}
					if(fp.mkdir())
					{
						f.listadp.data.add(new LayoutFileList.ViewData(fp));
						f.notifyDataSetChanged();
						f.listadp.list.setSelection(f.listadp.data.size()-1);
						uitool.toos(uitool.mainThis,"创建成功");
					}
					else
						uitool.toos(uitool.mainThis,"创建失败!");
				}
			});
		edit.show();
	}
	
	public void opentitle()
	{
		uitool.apptitle.add(AppTitile.DRITITLE);
		uitool.pagerAdapter.addView(new LayoutFileList(context,null));
		this.setPosition(uitool.apptitle.listAdapt.data.size() - 1);
		uitool.apptitle.listAdapt.notifyDataSetChanged();
		uitool.pagerAdapter.notifyDataSetChanged();
		uitool.apptitle.setPosition(getPosition());
		uitool.pagerAdapter.setPosition(getPosition());
	}
	
	public void opentitle(LayoutFileList view)
	{
		uitool.apptitle.add(AppTitile.DRITITLE);
		uitool.pagerAdapter.addView(view);
		this.setPosition(uitool.apptitle.listAdapt.data.size() - 1);
		uitool.apptitle.listAdapt.notifyDataSetChanged();
		uitool.pagerAdapter.notifyDataSetChanged();
		uitool.apptitle.setPosition(getPosition());
		uitool.pagerAdapter.setPosition(getPosition());
	}
	
	private void savetitle()
	{
		final EditBox edit = new EditBox(uitool.mainThis);
		edit.setTitle("请输入备注")
			.setMessage("新建书签")
			.setLeft("取消")
			.setRight("保存")
			.setRight(new EditBox.onButton(){
				@Override
				public void onClick()
				{
					DriLayout.Data data = new DriLayout.Data
					(
						System.currentTimeMillis(),
						edit.getMessage(),
						uitool.pagerAdapter.get(uitool.add.getPosition()).listadp.getFp().getPath()
					);
					uitool.drawlayout.dri.data.add(data);
					dbtool.addDri(data);
				}
			})
			.show();
	}
}
