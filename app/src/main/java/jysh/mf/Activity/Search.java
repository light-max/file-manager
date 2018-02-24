package jysh.mf.Activity;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Adapter.*;
import jysh.mf.Dialog.*;
import jysh.mf.ThreadTool.searchThread;

public class Search extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		list = new SearchFiles
		(
			this,
			(ListView)findViewById(R.id.activity_search_listview),
			new ArrayList<SearchFiles.Data>()
		);
		findViewById(R.id.activity_search_start).setOnClickListener(new setMode());
		message = (TextView)findViewById(R.id.activity_search_message);
		showModeDialog();
	}
	
	public SearchFiles list;
	public TextView message;
	public List<File> fp;
	
	private void showModeDialog()
	{
		new SearchMode(this).setActivity(this).show();
	}
	
	public int mode = 0;
	public String expression = "";
	
	class setMode implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			showModeDialog();
		}
	}
	
	public static final int FILE_LIST = 0;
	public Handler UpdateUi = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case FILE_LIST:
				((searchThread)msg.obj).updateUi();
				break;
			}
		}
	};
}

