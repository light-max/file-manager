package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import jysh.mf.R;
import android.view.*;
import java.io.*;
import jysh.mf.Util.*;
import jysh.mf.Activity.*;

public class ZipOperateSelect extends Dialog implements View.OnClickListener
{
	public ZipOperateSelect(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_zip_operare_select,null,false);
		setContentView(v);
		findViewById(decomposition).setOnClickListener(this);
		findViewById(look).setOnClickListener(this);
		this.context = context;
	}
	
	private final int decomposition = R.id.dialog_zipoperareselect_decompression;
	private final int look = R.id.dialog_zipoperareselect_look;
	private File fp;
	private Context context;

	public ZipOperateSelect setFp(File fp)
	{
		this.fp = fp;
		return this;
	}
	
	@Override
	public void onClick(View v)
	{
		dismiss();
		if(v.getId()==decomposition)
			new ZipDecompression(context).setFp(fp).show();
		if(v.getId()==look)
			activitytool.startActivity
			(
				uitool.mainThis,ZipLook.class,
				new String[]{"path"},new String[]{fp.getPath()}
			);
	}
}
