package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import android.view.*;
import java.io.*;
import jysh.mf.*;
import jysh.mf.Util.*;
import jysh.mf.Widget.*;

public class OperateFile extends Dialog implements View.OnClickListener
{
	public OperateFile(final Context context,final File fp)
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
			findViewById(id[8]).setVisibility(View.GONE);
		}
		attribute = new OnClick(){
			@Override
			public void onClick()
			{
				dismiss();
				new FileAttribute(context,fp).show();
			}
		};
		open = new OperateFile.OnClick(){
			@Override
			public void onClick()
			{
				// 我觉得把文件对象传递过去,打开操作用这个对象来确定好一点
				new OpenFileStyle(context,fp).show();
			}
		};
		send = new OperateFile.OnClick(){
			@Override
			public void onClick()
			{
				filetool.ShareFile(context,fp);
			}
		};
		addbook = new OperateFile.OnClick(){
			@Override
			public void onClick()
			{
				final EditBox edit = new EditBox(context);
				edit.setTitle("请输入备注")
					.setMessage("新建书签")
					.setLeft("取消")
					.setRight("保存")
					.setRight(new EditBox.onButton(){
						@Override
						public void onClick()
						{
							if(fp.isDirectory())
							{
								DriLayout.Data data = new DriLayout.Data
								(
									System.currentTimeMillis(),
									edit.getMessage(),
									fp.getPath()
								);
								uitool.drawlayout.dri.data.add(data);
								dbtool.addDri(data);
							}
							else
							{
								FileLayout.Data data = new FileLayout.Data
								(
									System.currentTimeMillis(),
									edit.getMessage(),
									fp.getPath()
								);
								uitool.drawlayout.file.data.add(data);
								dbtool.addFile(data);
							}
						}
					})
					.show();
			}
		};
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
		else if(id==this.id[7]&&zip!=null)
			zip.onClick();
		else if(id==this.id[8]&&send!=null)
			send.onClick();
		else if(id==this.id[9]&&attribute!=null)
			attribute.onClick();
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
		R.id.dialogopenfiledri_zip,
		R.id.dialogopenfiledri_send,
		R.id.dialogopenfiledri_attribute,
	};
	
	static public interface OnClick
	{
		public void onClick();
	}
	
	private OnClick select,open,rename,delete,move,copy,addbook,zip,send,attribute;
	
	public OperateFile setSelect(OnClick select)
	{
		this.select = select;
		return this;
	}

	public OperateFile setReanme(OnClick rename)
	{
		this.rename = rename;
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
	
	public OperateFile setZip(OnClick zip)
	{
		this.zip = zip;
		return this;
	}
	
	public OperateFile setSend(OnClick send)
	{
		this.send = send;
		return this;
	}
	
	public OperateFile setAttribute(OnClick attribute)
	{
		this.attribute = attribute;
		return this;
	}
}
