package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import android.net.*;
import android.view.*;
import java.io.*;
import jysh.mf.*;
import jysh.mf.Util.*;

public class OpenFileStyle extends Dialog implements View.OnClickListener
{
	public OpenFileStyle(Context context,File fp)
	{
		super(context,R.style.Dialog);
		this.fp = fp;
		View view = LayoutInflater.from(context)
			.inflate(R.layout.dialog_openfile,null,false);
		setContentView(view);
		for(int i:id)
		{
			findViewById(i).setOnClickListener(this);
		}
		findViewById(R.id.dialog_openfile_exit).setOnClickListener(this);
	}
	
	private File fp;
	
	private static final int id[] = new int[]{
		R.id.dialog_openfile_textlook,
		R.id.dialog_openfile_image,
		R.id.dialog_openfile_web,
		R.id.dialog_openfile_textedit,
		R.id.dialog_openfile_apk,
		R.id.dialog_openfile_null,
	};

	@Override
	public void onClick(View v)
	{
		if(v.getId()==id[0])
		{
			
		}
		else if(v.getId()==id[1])
		{
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(Uri.fromFile(fp), "image/*");
			getContext().startActivity(intent);
		}
		else if(v.getId()==id[2])
		{
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("file://"+fp.getPath()));
			intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
			getContext().startActivity(intent);
		}
		else if(v.getId()==id[3])
		{
			
		}
		else if(v.getId()==id[4])
		{
			Intent intent = new Intent();
			intent.setAction(intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(fp),"application/vnd.android.package-archive");
			getContext().startActivity(intent);
		}
		else if(v.getId()==id[5])
		{
			filetool.fileAutoOpen(getContext(),fp);
		}
		dismiss();
	}
}
