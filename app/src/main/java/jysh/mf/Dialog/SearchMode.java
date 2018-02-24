package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import jysh.mf.*;
import jysh.mf.Activity.*;
import jysh.mf.Util.*;

public class SearchMode extends Dialog implements View.OnClickListener
{
	public SearchMode(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_searchmode,null,false);
		setContentView(v);
		for(int i = 0;i < id.length;i++)
		{
			radio[i] = (RadioButton)findViewById(id[i]);
			radio[i].setOnClickListener(this);
		}
		edit = (EditText)findViewById(R.id.dialog_searchmode_expression);
		findViewById(R.id.dialog_searchmode_button)
		.setOnClickListener(new Start());
	}
	
	@Override
	public void onClick(View v)
	{
		for(int i = 0;i < id.length;i++)
		{
			if(v.getId()==id[i])
			{
				context.mode = i;
				showRadio(i);
			}
		}
	}
	
	private static final int[] id = new int[]{
		R.id.dialog_searchmode_radio1,
		R.id.dialog_searchmode_radio2,
		R.id.dialog_searchmode_radio3,
		R.id.dialog_searchmode_radio4,
	};
	private RadioButton[] radio = new RadioButton[id.length];
	private EditText edit;
	private Search context;
	
	public SearchMode setActivity(Search context)
	{
		this.context = context;
		showRadio(context.mode);
		edit.setText(context.expression);
		return this;
	}
	
	private void showRadio(int i)
	{
		for(RadioButton radio:this.radio)
		{
			radio.setChecked(false);
		}
		radio[i].setChecked(true);
	}
	
	class Start implements View.OnClickListener
	{
		@Override
		public void onClick(View p1)
		{
			context.expression = edit.getText().toString();
			if(context.expression.trim().length()==0)
			{
				uitool.toos(context,"输入有误");
				return;
			}
			context.message.setVisibility(View.GONE);
			context.list.data.clear();
			dismiss();
			if(context.search!=null)
			{
				context.search.start_button = false;
				context.search = null;
			}
			switch(context.mode)
			{
			case 0:
				context.search = new Search.SearchThread(){
					public boolean isExpression(File fp)
					{
						for(String s:expression)
						{
							if(fp.getName().length() - s.length() < 6
							   &&s.equalsIgnoreCase(fp.getName()))
								return true;
						}
						return false;
					}
				};
				break;
			case 1:
				context.search = new Search.SearchThread(){
					public boolean isExpression(File fp)
					{
						for(String s:expression)
						{
							if(fp.getName().length() - s.length() < 6
								&&fp.getName().toLowerCase().indexOf(s.toLowerCase())==0)
								return true;
						}
						return false;
					}
				};
				break;
			case 2:
				context.search = new Search.SearchThread(){
					public void initExpression_s()
					{
						super.initExpression_s();
						for(String s:expression)
						{
							if(s.charAt(0)!='.')
								s = "." + s;
						}
					}
					public boolean isExpression(File fp)
					{
						for(String s:expression)
						{
							if(fp.getName().endsWith(s))
								return true;
						}
						return false;
					}
				};
				break;
			case 3:
				context.search = new Search.SearchThread(){
					public void initExpression_s()
					{
						expression = new String[]{context.expression.replace("\\\\","\\")};
					}
					public boolean isExpression(File fp)
					{
						if(fp.getName().matches(expression[0]))
							return true;
						return false;
					}
				};
				break;
			}
			context.search.setActivity(context).setAdapterView(context.list).start();
		}
	}
}
