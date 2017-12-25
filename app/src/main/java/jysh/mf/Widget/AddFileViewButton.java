package jysh.mf.Widget;
import android.view.*;
import android.widget.*;
import android.app.*;
import jysh.mf.R;
import jysh.mf.Util.*;

public class AddFileViewButton implements View.OnClickListener
{
	public AddFileViewButton(Activity context)
	{
		this.context = context;
		button = (ImageButton)context.findViewById(R.id.main_add);
		button.setOnClickListener(this);
	}
	
/*	public void setPosition(int position)
	{
		this.position = position;
	}

	public int getPosition()
	{
		return position;
	}*/
	
	@Override
	public void onClick(View v)
	{
		uitool.toos(context,"添加");
		uitool.flist.add(new LayoutFileList(context,null));
		uitool.pagerAdapter.addView(uitool.flist.get(uitool.flist.size()-1));
		uitool.pagerAdapter.notifyDataSetChanged();
		
		uitool.apptitle.add("文件目录");
		uitool.apptitle.notifyDataSetChanged();
	}
	
	private ImageButton button;
	private Activity context;
//	private int position = 0;
}
