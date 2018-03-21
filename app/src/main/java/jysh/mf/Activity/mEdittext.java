package jysh.mf.Activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import jysh.mf.*;
import jysh.mf.Util.*;

public class mEdittext extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_edittext);
		findViewById(R.id.activity_edittext_exit)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				if(exit())
					finish();
			}
		});
		findViewById(R.id.activity_edittext_save)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				saveContent();
				uitool.toos(mEdittext.this,"已保存");
			}
		});
		fp = new File(getIntent().getData().getPath());
		TextView title = (TextView)findViewById(R.id.activity_edittext_title);
		title.setText(fp.getName());
		content = (EditText)findViewById(R.id.activity_edittext_content);
		new ReaderFileTextThread().start();
	}

	@Override
	public void onBackPressed()
	{
		if(exit())
			super.onBackPressed();
	}
	
	private File fp;
	private EditText content;
	private String fcontent = "";
	private static final int APPEND_TEXT = 0;
	
	private void saveContent()
	{
		String edittext = content.getText().toString();
		if(fcontent.length()!=0&&
			! fcontent.equals(edittext))
		{
			fcontent = edittext;
			writeFile(fp,fcontent);
		}
	}
	
	private boolean exit()
	{
		final String edittext = content.getText().toString();
		if(fcontent.length()!=0&&
			! fcontent.equals(edittext))
		{
			new exitDialog(this){
				public void onSave()
				{
					writeFile(fp,edittext);
					finish();
				}
				public void onCancel()
				{
				}
				public void onClose()
				{
					finish();
				}
			}.show();
			return false;
		}
		return true;
	}
	
	private void writeFile(File fp,String text)
	{
		FileWriter out = null;
		try
		{
			out = new FileWriter(fp);
			out.write(text);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(out==null)
				return;
			try
			{
				out.flush();
				out.close();
			}
			catch (IOException e)
			{}
		}
	}
	
	private class ReaderFileTextThread extends Thread
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
						fcontent = content.getText().toString();
					break;
			}
		}
	};
	
	private class exitDialog extends Dialog implements View.OnClickListener
	{
		public exitDialog(Context context)
		{
			super(context,R.style.Dialog);
			View v = LayoutInflater.from(context)
				.inflate(R.layout.dialog_activity_edittext_exit_box,null);
			super.setContentView(v);
			findViewById(SAVE).setOnClickListener(this);
			findViewById(CANCEL).setOnClickListener(this);
			findViewById(CLOSE).setOnClickListener(this);
		}
		
		private final int SAVE = R.id.dialog_activity_edittext_exitbox_1;
		private final int CANCEL = R.id.dialog_activity_edittext_exitbox_2;
		private final int CLOSE = R.id.dialog_activity_edittext_exitbox_3;
		
		public void onSave()
		{
		}
		
		public void onCancel()
		{
		}
		
		public void onClose()
		{
		}

		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
				case SAVE:
					onSave();
					break;
				case CANCEL:
					onCancel();
					break;
				case CLOSE:
					onClose();
					break;
			}
			dismiss();
		}
	}
}
