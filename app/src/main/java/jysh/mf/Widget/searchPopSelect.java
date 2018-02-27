package jysh.mf.Widget;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Activity.*;

public class searchPopSelect extends LinearLayout
{
	public searchPopSelect(final Context context,AttributeSet attr)
	{
		super(context,attr);
		LayoutInflater.from(context).inflate(R.layout.pop_select,this);
		findViewById(R.id.popselect_exit)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				setVisibility(GONE);
				fp.clear();
				((Search)context).list.setSelect(false);
			}
		});
		findViewById(R.id.popselect_copy)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				copy();
			}
		});
		findViewById(R.id.popselect_move)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				move();
			}
		});
		findViewById(R.id.popselect_delete)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				delete();
			}
		});
		findViewById(R.id.popselect_qx)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				qx();
			}
		});
		findViewById(R.id.popselect_menu)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				
			}
		});
		number = (TextView)findViewById(R.id.popselect_size);
		number.setText("0");
	}
	
	private List<File> fp = new ArrayList<>();
	private TextView number;
	
	public void addFp(File fp)
	{
		this.fp.add(fp);
		number.setText(this.fp.size()+"");
	}
	
	public void removeFp(File fp)
	{
		this.fp.remove(fp);
		number.setText(this.fp.size()+"");
	}
	
	public void copy()
	{
	}
	
	public void move()
	{
	}
	
	public void delete()
	{
	}
	
	public void qx()
	{
	}
}
