package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import android.view.*;
import jysh.mf.*;
import java.io.*;

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
		dismiss();
	}
}
