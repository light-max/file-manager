package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import android.view.*;
import jysh.mf.R;
import android.widget.*;
import java.io.*;

public class OperateFile extends Dialog implements View.OnClickListener
{
	public OperateFile(Context context,File fp)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_openfiledri,null,false);
		super.setContentView(v);
		for(int i:id)
		{
			findViewById(i).setOnClickListener(this);
		}
		if(fp.isDirectory())
		{
			findViewById(id[1]).setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if(id==this.id[0]&&select!=null)
			select.onClick();
		else if(id==this.id[1]&&open!=null)
			open.onClick();
		else if(id==this.id[2]&&rename!=null)
			rename.onClick();
		else if(id==this.id[3]&&delete!=null)
			delete.onClick();
		else if(id==this.id[4]&&move!=null)
			move.onClick();
		else if(id==this.id[5]&&copy!=null)
			copy.onClick();
		else if(id==this.id[6]&&addbook!=null)
			addbook.onClick();
		dismiss();
	}
	
	private static final int[] id = new int[]{
		R.id.dialogopenfiledri_select,
		R.id.dialogopenfiledri_open,
		R.id.dialogopenfiledri_rename,
		R.id.dialogopenfiledri_delete,
		R.id.dialogopenfiledri_move,
		R.id.dialogopenfiledri_copy,
		R.id.dialogopenfiledri_addbook,
	};
	
	static public interface OnClick
	{
		public void onClick();
	}
	
	private OnClick select,open,rename,delete,move,copy,addbook;
	
	public OperateFile setSelect(OnClick select)
	{
		this.select = select;
		return this;
	}

	public OperateFile setOpen(OnClick open)
	{
		this.open = open;
		return this;
	}

	public OperateFile setDelete(OnClick delete)
	{
		this.delete = delete;
		return this;
	}

	public OperateFile setMove(OnClick move)
	{
		this.move = move;
		return this;
	}

	public OperateFile setCopy(OnClick copy)
	{
		this.copy = copy;
		return this;
	}

	public OperateFile setAddbook(OnClick addbook)
	{
		this.addbook = addbook;
		return this;
	}
}
