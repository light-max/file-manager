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
	static public MainActivity mainThis;
	
	static public AppTitile apptitle;
	static public MyPagerAdapter pagerAdapter;
	static public AddFileViewButton add;
	static public MyDrawLayout drawlayout;
	
	static public void InitViewWidget(Activity context)
	{
		apptitle = (AppTitile)context.findViewById(R.id.main_title);
		apptitle.add(AppTitile.DRITITLE);
		apptitle.notifyDataSetChanged();
		
		pagerAdapter = new MyPagerAdapter
		(
			(ViewPager)context.findViewById(R.id.main_viewpager)
		);
		pagerAdapter.addView(new LayoutFileList(context,null));
		pagerAdapter.notifyDataSetChanged();
		
		add = new AddFileViewButton(context);
		
		drawlayout = (MyDrawLayout)context.findViewById(R.id.main_drawlayout);
	}
	
	static public boolean deleteTitleList(int position)
	{
		if(uitool.pagerAdapter.size()!=0)
		{
			uitool.apptitle.remove(position);
			List<LayoutFileList> list = uitool.pagerAdapter.view;
			uitool.pagerAdapter = new MyPagerAdapter
			(
				(ViewPager)uitool.mainThis.findViewById(R.id.main_viewpager)
			);
			list.remove(position);
			uitool.pagerAdapter.view = list;
			/*	if(position==uitool.pagerAdapter.size()-1)
			 {
			 position--;
			 }*/
			position = 0;
		}
		if(uitool.pagerAdapter.size()!=0)
		{
			uitool.pagerAdapter.setPosition(position);
			uitool.apptitle.setPosition(position);
			uitool.apptitle.notifyDataSetChanged();
			uitool.pagerAdapter.notifyDataSetChanged();
			uitool.add.setPosition(position);
			return false;
		}
		return true;
	}
	
	static public void toos(Context activity,String message)
	{
		Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
	}
}
