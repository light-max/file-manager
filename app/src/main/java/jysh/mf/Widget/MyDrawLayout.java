package jysh.mf.Widget;

import android.content.*;
import android.util.*;
import android.widget.*;
import android.view.*;
import jysh.mf.R;
import android.support.v7.widget.*;
import jysh.mf.Util.*;
import jysh.mf.Activity.*;

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
		dri = new DriLayout
		(
			(RecyclerView)findViewById(R.id.drawlayout_drilist)
		);
		file = new FileLayout
		(
			(RecyclerView)findViewById(R.id.drawlayout_filelist)
		);
	}
	
	private static final int id[] = new int[]{
		R.id.drawlayout_select,
		R.id.drawlayout_dri,
		R.id.drawlayout_file,
		R.id.drawlayout_set
	};
	private static final int textId[] = new int[]{
		R.id.drawlayout_selecttext,
		R.id.drawlayout_dritext,
		R.id.drawlayout_filetext,
	};
	private TextView view[] = new TextView[id.length];
	public SelectLayout select;
	public DriLayout dri;
	public FileLayout file;
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
		dri.view.setVisibility(View.GONE);
		file.view.setVisibility(View.GONE);
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
				break;
			case R.id.drawlayout_dri:
				if(dri.data.size()==0)
				{
					findViewById(textId[1]).setVisibility(View.VISIBLE);
				}
				else
				{
					dri.view.setVisibility(View.VISIBLE);
					dri.notifyDataSetChanged();
				}
				break;
			case R.id.drawlayout_file:
				if(file.data.size()==0)
				{
					findViewById(textId[2]).setVisibility(View.VISIBLE);
				}
				else
				{
					file.view.setVisibility(View.VISIBLE);
					file.notifyDataSetChanged();
				}
				break;
			case R.id.drawlayout_set:
				activitytool.startActivity(uitool.mainThis,AppSet.class);
				break;
		}
	}
	
	public void Open()
	{
		if(showView==R.id.drawlayout_set)
		{
			return;
		}
		showText(showView);
	}
}
