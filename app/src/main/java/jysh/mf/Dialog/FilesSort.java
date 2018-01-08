package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import jysh.mf.R;
import android.view.*;
import java.util.*;
import jysh.mf.Widget.*;
import jysh.mf.Util.*;

public class FilesSort extends Dialog implements View.OnClickListener
{
	public FilesSort(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_filessort,null,false);
		setContentView(v);
		for(int id:this.id)
		{
			findViewById(id).setOnClickListener(this);
		}
	}
	
	private static final int[] id = new int[]{
		R.id.dialog_filessort_date,
		R.id.dialog_filessort_name,
		R.id.dialog_filessort_size,
		R.id.dialog_filessort_null
	};
	
	@Override
	public void onClick(View v)
	{
		int chaAt = 0;
		for(int id:this.id)
		{
			if(id==v.getId())
			{
				ascending = -ascending;
				function = chaAt;
				FilesSort.setFilesSort(uitool.pagerAdapter.view);
				break;
			}
			chaAt++;
		}
		dismiss();
	}
	
	// 是否使用排序 -1降序 0无 1升序
	public static int ascending = 0;
	// 使用哪种方法排序
	public static int function;
	public static final int FUN_DATE = 0;
	public static final int FUN_NAME = 1;
	public static final int FUN_SIZE = 2;
	
	public static void setFilesSort(List<LayoutFileList> list)
	{
		LayoutFileList.ViewData temp;
		if(function==FUN_DATE)
		{
			for(LayoutFileList view:list)
			{
				view.listadp.data.clear();
				LayoutFileList.ViewData[] data = (LayoutFileList.ViewData[])(view.listadp.data.toArray());
				for(int i = 0;i < data.length - 1;i++)
				{
					for(int j = i + 1;j < data.length;j++)
					{
						// 大的排前面
						if(data[i].getSize_t() < data[j].getSize_t())
						{
							temp = data[i];
							data[j] = data[i];
							data[i] = temp;
						}
					}
					// 大的排前面
					if(ascending==-1)
					{
						view.listadp.data.add(data[i]);
					}
					else
					{
						view.listadp.data.add(0,data[i]);
					}
				}
				//
			}
		}
		else if(function==FUN_NAME)
		{
			
		}
		else if(function==FUN_NAME)
		{
			
		}
		
	}
}
