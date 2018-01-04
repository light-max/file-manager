package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import android.view.*;
import java.io.*;
import jysh.mf.*;
import android.widget.*;
import jysh.mf.Util.*;

public class ZipCompress extends Dialog implements View.OnClickListener
{
	public ZipCompress(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_zipcompress,null,false);
		setContentView(v);
		for(int i:id)
		{
			findViewById(i).setOnClickListener(this);
		}
		isRadioButton(onAt);
		findViewById(exit).setOnClickListener(this);
		findViewById(start).setOnClickListener(this);
		
		path = (EditText)findViewById(id_path);
		name = (EditText)findViewById(id_name);
	}
	
	private File fp = null;
	
	public ZipCompress setFp(File fp)
	{
		this.fp = fp;
		return this;
	}
	
	private static final int[] id = new int[]{
		R.id.dialog_zipcompress_radio1,
		R.id.dialog_zipcompress_radio2,
		R.id.dialog_zipcompress_radio3,
	};
	private int onAt = 0;
	private int exit = R.id.dialog_zipcompress_exit;
	private int start = R.id.dialog_zipcompress_start;
	private int id_path = R.id.dialog_zipcompress_path;
	private int id_name = R.id.dialog_zipcompress_name;
	
	private EditText path,name;
	
	@Override
	public void onClick(View v)
	{
		if(v.getId()==exit)
		{
			dismiss();
			return;
		}
		if(v.getId()==start)
		{
			return;
		}
		isRadioButton(v.getId());
	}
	
	private void isRadioButton(int id)
	{
		RadioButton radio[] = new RadioButton[4];
		for(int i = 0;i < this.id.length;i++)
		{
			if(id==this.id[i])
			{
				onAt = i;
			}
			radio[i] = (RadioButton)findViewById(this.id[i]);
			radio[i].setChecked(false);
		}
		radio[onAt].setChecked(true);
	}
}
