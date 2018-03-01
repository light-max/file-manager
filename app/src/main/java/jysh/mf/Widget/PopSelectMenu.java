package jysh.mf.Widget;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Dialog.*;
import jysh.mf.Util.*;

public class PopSelectMenu extends LinearLayout implements View.OnClickListener
{
	public PopSelectMenu(Context context,AttributeSet attr)
	{
		super(context,attr);
		LayoutInflater.from(context).inflate(R.layout.pop_selectmenu,this);
		for(int i:id)
		{
			findViewById(i).setOnClickListener(this);
		}
	}
	
	public PopupWindow pop;
	
	public static final int id[] = new int[]{
		R.id.pop_selectmenu_zip,
		R.id.pop_selectmenu_data,
		R.id.pop_selectmenu_send,
	};

	public void setPop(PopupWindow pop)
	{
		this.pop = pop;
	}
	
	@Override
	public void onClick(View v)
	{
		if(v.getId()==id[0])
		{
			new ZipCompress(getContext()).show();
		}
		else if(v.getId()==id[1])
		{
			new FilesData(getContext()).setFiles(uitool.drawlayout.select).show();
		}
		else if(v.getId()==id[2])
		{
			SelectLayout select = uitool.drawlayout.select;
			List<File> fp = new ArrayList<>();
			for(SelectLayout.Data d:select.data)
			{
				fp.add(d.getFp());
			}
			filetool.ShareFile(getContext(),fp);
		}
		pop.dismiss();
	}
}
