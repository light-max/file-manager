package jysh.mf.Widget;

import android.content.*;
import android.util.*;
import android.widget.*;
import android.view.*;
import jysh.mf.R;
import android.support.v7.widget.*;

public class MyDrawLayout extends LinearLayout implements View.OnClickListener
{
	public MyDrawLayout(Context context,AttributeSet attr)
	{
		super(context,attr);
		LayoutInflater.from(context).inflate(R.layout.drawlayout,this);
		setOnClickListener(this);
		for(int i = 0;i < id.length;i++)
		{
			view[i] = (TextView)findViewById(id[i]);
			view[i].setOnClickListener(this);
		}
		select = new SelectLayout
		(
			(RecyclerView)findViewById(R.id.drawlayout_selectlist)
		);
	}
	
	private static final int id[] = new int[]{
		R.id.drawlayout_select,
		R.id.drawlayout_dri,
		R.id.drawlayout_file,
		R.id.drawlayout_set
	};
	private static final int textId[] = new int[]{
		R.id.drawlayout_selecttext
	};
	private TextView view[] = new TextView[id.length];
	public SelectLayout select;
	public int showView = 0;
	
	@Override
	public void onClick(View v)
	{
		showText(v.getId());
	}
	
	private void showText(int id)
	{
		showView = id;
		for(int i = 0;i < textId.length;i++)
		{
			findViewById(textId[i]).setVisibility(View.GONE);
		}
		select.view.setVisibility(View.GONE);
		switch(id)
		{
			case R.id.drawlayout_select:
				if(!SelectLayout.isSelectFile())
				{
					findViewById(textId[0]).setVisibility(View.VISIBLE);
				}
				else
				{
					select.view.setVisibility(View.VISIBLE);
					select.notifyDataSetChanged();
				}
		}
	}
	
	public void Open()
	{
		showText(showView);
	}
}
