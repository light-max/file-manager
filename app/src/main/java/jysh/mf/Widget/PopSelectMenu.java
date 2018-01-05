package jysh.mf.Widget;

import android.view.*;
import android.widget.*;
import jysh.mf.R;
import android.content.*;
import android.util.*;
import jysh.mf.Dialog.*;

public class PopSelectMenu extends LinearLayout implements View.OnClickListener
{
	public PopSelectMenu(Context context,AttributeSet attr)
	{
		super(context,attr);
		LayoutInflater.from(context).inflate(R.layout.pop_selectmenu,this);
		for(int i:id)
		{
			findViewById(i).setOnClickListener(this);
		}
	}
	
	private PopupWindow pop;
	
	private static final int id[] = new int[]{
		R.id.pop_selectmenu_zip,
		R.id.pop_selectmenu_data,
		R.id.pop_selectmenu_send,
	};

	public void setPop(PopupWindow pop)
	{
		this.pop = pop;
	}
	
	@Override
	public void onClick(View v)
	{
		if(v.getId()==id[0])
		{
			new ZipCompress(getContext()).show();
		}
		pop.dismiss();
	}
}
