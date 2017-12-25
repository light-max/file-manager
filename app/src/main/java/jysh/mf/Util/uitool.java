package jysh.mf.Util;
import android.app.*;
import android.content.*;
import android.support.v4.view.*;
import android.widget.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Adapter.*;
import jysh.mf.Widget.*;

public class uitool
{
	static public AppTitile apptitle;
	static public MyPagerAdapter pagerAdapter;
	static public List<LayoutFileList> flist;
	static public AddFileViewButton add;
	
	static public void InitViewWidget(Activity context)
	{
		apptitle = (AppTitile)context.findViewById(R.id.main_title);
		apptitle.add("文件目录");
		apptitle.notifyDataSetChanged();
		
		pagerAdapter = new MyPagerAdapter
		(
			(ViewPager)context.findViewById(R.id.main_viewpager)
		);
		flist = new ArrayList<LayoutFileList>();
		flist.add(new LayoutFileList(context,null));
		pagerAdapter.addView(flist.get(0));
		pagerAdapter.notifyDataSetChanged();
		
		add = new AddFileViewButton(context);
	}
	
	static public void toos(Context activity,String message)
	{
		Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
	}
}
