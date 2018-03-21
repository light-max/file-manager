package jysh.mf.Activity;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import jysh.mf.*;
import android.util.*;

public class StaticText extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_text);
		findViewById(R.id.activity_text_exit)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		fp = new File(getIntent().getData().getPath());
		TextView path = (TextView)findViewById(R.id.activity_text_title);
		path.setText(fp.getName());
		content = (TextView)findViewById(R.id.activity_text_content);
		new ThreadAppendContent().start();
	}
	
	private File fp;
	private TextView content;
	private static final int APPEND_TEXT = 0;
	
	class ThreadAppendContent extends Thread
	{
		private FileReader in;
		private StringBuilder str = new StringBuilder(1024*10);
		@Override
		public void run()
		{
			try
			{
				in = new FileReader(fp);
				char byt[] = new char[1024];
				int len = 0;
				do{
					try
					{
						len = in.read(byt);
						str.append(byt,0,len);
					}
					catch (Exception e)
					{}
					if(str.length()>=1024*10)
					{
						Message msg = new Message();
						msg.what = APPEND_TEXT;
						msg.obj = str.toString();
						updateUi.sendMessage(msg);
						str = new StringBuilder(1024*10);
					}
				}
				while(len != -1);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{}
				Message msg = new Message();
				msg.arg1 = 1;
				msg.what = APPEND_TEXT;
				msg.obj = str.toString();
				updateUi.sendMessage(msg);
			}
		}
		
	}
	
	private Handler updateUi = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case APPEND_TEXT:
					content.append((String)msg.obj);
					if(msg.arg1==1)
						content.setTextIsSelectable(true);
					break;
			}
		}
	};
}
