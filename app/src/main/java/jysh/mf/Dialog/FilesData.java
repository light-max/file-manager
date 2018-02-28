package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Util.*;
import jysh.mf.Widget.*;

public class FilesData extends Dialog
{
	public FilesData(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_filesdata,null,false);
		setContentView(v);
		findViewById(R.id.dialog_filesdata_exit).
		setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});
		number = (TextView)findViewById(R.id.dialog_filesdata_number);
		len = (TextView)findViewById(R.id.dialog_filesdata_len);
		size = (TextView)findViewById(R.id.dialog_filesdata_size);
	}
	
	private TextView number,len,size;

	public FilesData setFiles(SelectLayout files)
	{
		number.setText("共有: "+files.data.size()+"个文件");
		long len = 0;
		for(SelectLayout.Data f:files.data)
		{
			len += f.getFp().length();
		}
		this.len.setText("长度: "+len);
		size.setText("大小: "+filetool.getFileSize(len));
		return this;
	}
	
	public FilesData setFiles(List<File> fp)
	{
		number.setText("共有: "+fp.size()+"个文件");
		long len = 0;
		for(File f:fp)
		{
			len += f.length();
		}
		this.len.setText("长度: "+len);
		size.setText("大小: "+filetool.getFileSize(len));
		return this;
	}
}
