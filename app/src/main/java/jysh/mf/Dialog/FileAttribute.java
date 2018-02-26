package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import android.util.*;
import jysh.mf.R;
import java.io.*;
import android.view.*;
import android.widget.*;
import jysh.mf.Util.*;

public class FileAttribute extends Dialog
{
	public FileAttribute(final Context context,final File fp)
	{
		super(context,R.style.Dialog);
		this.fp = fp;
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_fileattribute,null);
		super.setContentView(v);
		View exit = v.findViewById(R.id.dialogfileattribute_exit);
		exit.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});
		TextView text[] = new TextView[]{
			(TextView)v.findViewById(R.id.dialogfileattribute_name),
			(TextView)v.findViewById(R.id.dialogfileattribute_path),
			(TextView)v.findViewById(R.id.dialogfileattribute_date),
			(TextView)v.findViewById(R.id.dialogfileattribute_size),
			(TextView)v.findViewById(R.id.dialogfileattribute_rw),
		};
		text[0].setText(fp.getName());
		text[1].setText(fp.getPath());
		text[2].setText(filetool.getFileDate(fp.lastModified()));
		text[3].setText(fp.length()+"");
		text[4].setText(filetool.getFileRw(fp));
		text[0].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				ClipboardManager cmb = (ClipboardManager)getContext()
					.getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setPrimaryClip(ClipData.newPlainText(null,fp.getName()));
				uitool.toos(context,"文件名已复制到剪切板");
			}
		});
		text[1].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				ClipboardManager cmb = (ClipboardManager)getContext()
					.getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setPrimaryClip(ClipData.newPlainText(null,fp.getPath()));
				uitool.toos(context,"文件路径已复制到剪切板");
			}
		});
	}
	
	private File fp;
}
