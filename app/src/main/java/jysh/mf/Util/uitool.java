package jysh.mf.Util;
import android.app.*;
import android.content.*;
import android.support.v4.view.*;
import android.widget.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Adapter.*;
import jysh.mf.Widget.*;
import android.support.v4.widget.*;
import android.view.*;
import jysh.mf.Dialog.*;

public class uitool
{
	static public final int TOAS = 3;
	static public final int CLOSE_DIALOG = 4;
	
	static public MainActivity mainThis;
	
	static public AppTitile apptitle;
	static public MyPagerAdapter pagerAdapter;
	static public AddFileViewButton add;
	static public MyDrawLayout drawlayout;
	static public DrawerLayout mainDrawer;
	static public boolean mainDrawerOpen;
	static public PopSelect popselect;
	
	static public Progeress progerss;
	
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
		
		mainDrawer = (DrawerLayout)context.findViewById(R.id.main);
		mainDrawer.setDrawerListener(new DrawerLayoutOpenClose());
		
		popselect = (PopSelect)context.findViewById(R.id.main_popselect);
		
		progerss = new Progeress(context);
		
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
	
	static private class DrawerLayoutOpenClose implements DrawerLayout.DrawerListener
	{
		@Override
		public void onDrawerClosed(View p1)
		{
			mainDrawerOpen = false;
		}

		@Override
		public void onDrawerSlide(View p1, float p2)
		{
		}

		@Override
		public void onDrawerOpened(View p1)
		{
			mainDrawerOpen = true;
			drawlayout.Open();
			drawlayout.select.notifyDataSetChanged();
		}

		@Override
		public void onDrawerStateChanged(int p1)
		{
		}
	}
}
