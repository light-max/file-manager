package jysh.mf.ThreadTool;

import android.os.*;
import java.io.*;
import jysh.mf.Activity.*;
import jysh.mf.Adapter.*;

class searchThread extends Thread
{
	public SearchFiles view;
	public searchThread setAdapterView(SearchFiles view)
	{
		this.view = view;
		return this;
	}

	public Search context;
	public searchThread setActivity(Search context)
	{
		this.context = context;
		return this;
	}

	private String expression;
	private String expression_s[];

	@Override
	public void run()
	{
	//	initExpression_s();
	//	fun(new File("/storage/emulated/0"));
	}

	private void fun(File fp)
	{
		for(File f:fp.listFiles())
		{
			if(f.isDirectory())
			{
				fun(fp);
				continue;
			}
			if(isExpression(f))
			{
				view.data.add(new SearchFiles.Data(f));
				Message msg = new Message();
				msg.what = Search.FILE_LIST;
				msg.obj = this;
				context.UpdateUi.sendMessage(msg);
			}
		}
	}

	public void updateUi()
	{
		view.notifyDataSetChanged();
	}

	public void initExpression_s()
	{

	}

	public boolean isExpression(File fp)
	{
		return true;
	}
}
