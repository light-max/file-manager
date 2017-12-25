package jysh.mf.Adapter;
import jysh.mf.R;
import android.support.v4.view.*;
import java.util.*;
import android.view.*;
import jysh.mf.Util.*;

public class MyPagerAdapter extends PagerAdapter
	implements ViewPager.OnPageChangeListener
{
	public List<View> view;
	public ViewPager viewPager;

	public MyPagerAdapter(ViewPager viewPager)
	{
		view = new ArrayList<View>();
		this.viewPager = viewPager;
		this.viewPager.setAdapter(this);
		this.viewPager.setOnPageChangeListener(this);
	}

	public void addView(View view)
	{
		this.view.add(view);
	}

	@Override
	public boolean isViewFromObject(View view, Object obj)
	{
		return view==obj;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		((ViewPager)container).removeView(view.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		((ViewPager)container).addView(view.get(position),0);
		return view.get(position);
	}

	@Override
	public int getItemPosition(Object object)
	{
		return super.getItemPosition(object);
	}

	@Override
	public int getCount()
	{
		return view.size();
	}
	
	public void setPosition(int position)
	{
		viewPager.setCurrentItem(position);
		notifyDataSetChanged();
	}

	@Override
	public void onPageSelected(int position)
	{
		// TODO: Implement this method
	//	uitool.add.setPosition(position);
		uitool.apptitle.setPosition(position);
	}

	@Override
	public void onPageScrollStateChanged(int p1)
	{
		// TODO: Implement this method
	}

	@Override
	public void onPageScrolled(int p1, float p2, int p3)
	{
		// TODO: Implement this method
	}
}
