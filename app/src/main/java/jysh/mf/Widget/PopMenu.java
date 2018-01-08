package jysh.mf.Widget;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import jysh.mf.*;
import jysh.mf.Dialog.*;
import jysh.mf.Util.*;

public class PopMenu extends LinearLayout implements View.OnClickListener
{
	public PopMenu(Context context,AttributeSet attr)
	{
		super(context,attr);
		LayoutInflater.from(context).inflate(R.layout.pop_menu,this);
		for(int i:id)
		{
			findViewById(i).setOnClickListener(this);
		}
	}
	
	private PopupWindow pop;
	
	private static final int id[] = new int[]{
		R.id.pop_menu_search,
		R.id.pop_menu_sort,
		R.id.pop_menu_savedri,
		R.id.pop_menu_re,
		R.id.pop_menu_exit,
	};

	@Override
	public void onClick(View v)
	{
		if(v.getId()==id[0])
		{
			
		}
		else if(v.getId()==id[1])
		{
			new FilesSort(getContext()).show();
		}
		else if(v.getId()==id[2])
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
		else if(v.getId()==id[3])
		{
			for(LayoutFileList f:uitool.pagerAdapter.view)
			{
				f.listadp.loadList();
			}
		}
		else if(v.getId()==id[4])
		{
			uitool.mainThis.finish();
		}
		pop.dismiss();
	}
	
	void setPop(PopupWindow pop)
	{
		this.pop = pop;
	}
}
