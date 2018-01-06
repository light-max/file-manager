package jysh.mf.Activity;

import android.app.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.GridLayout.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import jysh.mf.*;
import jysh.mf.Adapter.*;
import jysh.mf.Dialog.*;
import jysh.mf.Util.*;
import jysh.mf.Widget.*;

public class ZipLook extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		fp = new File(getIntent().getStringExtra("path"));
		if(!fp.canRead()||!fp.exists())
		{
			uitool.toos(this,"文件不可读");
			finish();
		}
		setContentView(R.layout.activity_ziplook);
		path = (TextView)findViewById(R.id.activity_ziplook_path);
		menu = (ImageView)findViewById(R.id.activity_ziplook_menu);
		menu.setOnClickListener(new onMenu());
		popmenu = new ZipLookMenu(this,null);
		pop = new PopupWindow(popmenu,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		popmenu.b1.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				pop.dismiss();
				finish();
				new ZipDecompression(uitool.mainThis).setFp(fp).show();
			}
		});
		popmenu.b2.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				pop.dismiss();
				finish();
			}
		});
		list = new ZipFileList
		(
			(ListView)findViewById(R.id.activity_ziplook_list),
			this,
			new ArrayList<ZipFileList.Data>()
		);
		try
		{
			createZipObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		list.notifyDataSetChanged();
	}
	
	private TextView path;
	private ImageView menu;
	private ZipLookMenu popmenu;
	private PopupWindow pop;
	private ZipFileList list;
	
	private File fp;
	private ZipInputStream inputStream = null;
	private ZipEntry entry = null;
	private ZipFile zipFile = null;
	private List<ZipEntry> eList = new ArrayList<>();
	private int stack = 1;
	
	class onMenu implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			pop.showAsDropDown(menu);
		}
	}
	
	private void createZipObject()throws Exception
	{
		inputStream = new ZipInputStream(new FileInputStream(fp));
		zipFile = new ZipFile(fp);
		while((entry = inputStream.getNextEntry())!=null)
		{
			eList.add(entry);
		}
		for(ZipEntry e:eList)
		{
		//	if(getPathStack(e)==stack)
			{
				list.data.add(new ZipFileList.Data(e));
			}
		}
	}
	
	//获取栈的深度,目录尾部还会有一个"/"
	private int getPathStack(ZipEntry entry)
	{
		int stack = 0;
		String name = entry.getName();
		for(int i = 0;i < name.length();i++)
		{
			if(name.charAt(i)!='/')
			{
				continue;
			}
			stack++;
		}
		if(entry.isDirectory())
		{
			stack--;
		}
		return stack;
	}
}
