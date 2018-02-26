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
import android.util.*;
import jysh.mf.Util.*;;

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
		path = (TextView)findViewById(R.id.activity_search_path);
		number = (TextView)findViewById(R.id.activity_search_number);
	}

	@Override
	public void onBackPressed()
	{
		if(search!=null)
		{
			search.start_button = false;
			search = null;
		}
		super.onBackPressed();
	}
	
	public SearchFiles list;
	public TextView message;
	public TextView path;
	public TextView number;
	public SearchThread search;
	public Progeress dialog;
	
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
	public static final int SEARCH_DATA = 1;
	public static final int SEARCH_END = 2;
	public static final int FILES_BMP = 3;
	public static final int CLOSE_DIALOG = 4;
	public static final int TOAS = 5;
	public static final int OPEN_DIALOG = 6;
	public static final int THREAD_UI = 7;
	public Handler UpdateUi = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case FILE_LIST:
				((SearchThread)(msg.obj)).updateUi();
				break;
			case SEARCH_DATA:
				path.setText((String)msg.obj);
				number.setText("已找到"+msg.arg1+"个文件");
				break;
			case SEARCH_END:
				((SearchThread)(msg.obj)).updateUi();
				path.setText("搜索完成!");
				break;
			case FILES_BMP:
				list.notifyDataSetChanged();
				break;
			case CLOSE_DIALOG:
				if(dialog!=null)
				{
					dialog.dismiss();
					dialog = null;
				}
				break;
			case TOAS:
				uitool.toos(Search.this,(String)(msg.obj));
				break;
			case OPEN_DIALOG:
				dialog = new Progeress(Search.this);
				dialog.show();
				break;
			case THREAD_UI:
				((Thread_Ui)(msg.obj)).onRun();
				break;
			}
		}
	};
	static public interface Thread_Ui
	{
		public void onRun()
	}
	
	public static class SearchThread extends Thread 
	{
		public SearchFiles view;
		public SearchThread setAdapterView(SearchFiles view)
		{
			this.view = view;
			return this;
		}

		public Search context;
		public SearchThread setActivity(Search context)
		{
			this.context = context;
			return this;
		}

		public String expression[];

		@Override
		public void run()
		{
			initExpression_s();
			File fp = new File("/storage/emulated/0");
			search(fp);
			try
			{
				sleep(50);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			view.addAll(files);
			files.clear();
			Message msg = new Message();
			msg.what = Search.SEARCH_END;
			msg.obj = this;
			context.UpdateUi.sendMessage(msg);
		}

		private List<File> files = new ArrayList<>();
		private long time[] = new long[]{
			System.currentTimeMillis(),
			System.currentTimeMillis(),
			System.currentTimeMillis(),
			System.currentTimeMillis(),
		};
		public boolean start_button = true;
		private void search(File fp)
		{
			if(!start_button)
			{
				return;
			}
			for (File f:fp.listFiles())
			{
				if(!start_button)
				{
					return;
				}
				if (f.isDirectory())
				{
					search(f);
				}
				else if (isExpression(f))
				{
					files.add(f);
				}
			}
			sendMessage(fp);
			sendMessage();
		}
		
		private void sendMessage()
		{
			if((time[1] = System.currentTimeMillis()) - time[0] < 1000)
			{
				return;
			}
			time[0] = System.currentTimeMillis();
			
			view.addAll(files);
			files.clear();
			Message msg = new Message();
			msg.what = Search.FILE_LIST;
			msg.obj = this;
			context.UpdateUi.sendMessage(msg);
		}
		
		private void sendMessage(File fp)
		{
			if((time[3] = System.currentTimeMillis()) - time[2] < 150)
			{
				return;
			}
			time[2] = System.currentTimeMillis();
			
			Message msg = new Message();
			msg.what = Search.SEARCH_DATA;
			msg.obj = fp.getPath();
			msg.arg1 = view.data.size();
			context.UpdateUi.sendMessage(msg);
		}

		public void updateUi()
		{
			view.notifyDataSetChanged();
		}
		
		public void initExpression_s()
		{
			String strarray[] = context.expression.split("/");
			int length = 0;
			for(String s:strarray)
			{
				if(s.trim().length()!=0)
					length++;
			}
			expression = new String[length];
			int i = 0;
			for(String s:strarray)
			{
				if(s.trim().length()!=0)
					expression[i++] = s;
			}
		}

		public boolean isExpression(File fp)
		{
			return true;
		}
	}
}

