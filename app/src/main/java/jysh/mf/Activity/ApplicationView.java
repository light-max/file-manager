package jysh.mf.Activity;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import jysh.mf.*;
import android.content.pm.PackageManager.*;
import jysh.mf.Util.*;

public class ApplicationView extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_applist);
		uitool.toos(this,"这个不会做，不干了");
		list = new ApplicationList
		(
			this,
			(ListView)findViewById(R.id.activity_applist_applist),
			new ArrayList<ApplicationList.Data>()
		){
			@Override
			public void onClick(ApplicationList.Data data)
			{
				clickActivity(data);
			}
		};
		thread = new loadApplicationItemThread();
		thread.start();
		check = (CheckBox)findViewById(R.id.activity_applist_check);
	}
	
	@Override
	public void onBackPressed()
	{
		thread.isExit = true;
		super.onBackPressed();
	}
	
	private void clickActivity(ApplicationList.Data data)
	{
		onClickCheckBox(data);
		finish();
	}
	
	private void onClickCheckBox(ApplicationList.Data data)
	{
		if(!check.isChecked())
			return;
	}
	
	private ApplicationList list;
	private loadApplicationItemThread thread;
	private final int ADD_APPITEM = 0;
	private CheckBox check;
	
	private Handler updateUi = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case ADD_APPITEM:
					list.notifyDataSetChanged();
					break;
			}
		}
	};
	
	class loadApplicationItemThread extends Thread
	{
		public boolean isExit = false;
		private List<ApplicationList.Data> data = new ArrayList<>();
		
		@Override
		public void run()
		{
		/*	PackageManager pm = getPackageManager();
			List<PackageInfo> Package = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
			for(PackageInfo f:Package)
			{
				if(isExit)
					break;
				String package_ = f.packageName;
				String name = f.applicationInfo.loadLabel(pm).toString();
				Drawable icon = f.applicationInfo.loadIcon(pm);
				data.add(new ApplicationList.Data(package_,name,"",icon));
				updateItemUi();
				
				ActivityInfo array[] = f.activities;
			//	Log.e("length",array.length+"");
			}*/
			
			// 获取PackageManager对象
			PackageManager pm = getPackageManager();
			// 设置<intent-filter>标签内需要满足的条件
			Intent intent = new Intent(Intent.ACTION_VIEW);
			//intent.addCategory(Intent.CATEGORY_DEFAULT);

			// 通过queryIntentActivities获取ResolveInfo对象
			List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent,
																	  0);

			// 调用系统排序，根据name排序
			// 该排序很重要，否则只能显示系统应用，不能显示第三方应用
			// 其实我测试发现有没有其实是一样的，就是输出的顺序是乱的
			Collections.sort(resolveInfos,
							 new ResolveInfo.DisplayNameComparator(pm));

			for (ResolveInfo resolveInfo : resolveInfos) {
				String appName = resolveInfo.loadLabel(pm).toString();// 获取应用名称
				String packageName = resolveInfo.activityInfo.packageName;// 包名
				String className = resolveInfo.activityInfo.name;// 入口类名
				System.out.println("程序名：" + appName + " 包名:" + packageName
								   + " 入口类名：" + className);
				data.add(new ApplicationList.Data("",appName,"",resolveInfo.loadIcon(pm)));
				updateItemUi();
			}
			
			updateItemUiEnd();
		}
		
		private void updateItemUi()
		{
			if(data.size()>5)
			{
				list.data.addAll(data);
				data.clear();
				Message msg = new Message();
				msg.what = ADD_APPITEM;
				updateUi.sendMessage(msg);
			}
		}
		
		private void updateItemUiEnd()
		{
			list.data.addAll(data);
			Message msg = new Message();
			msg.what = ADD_APPITEM;
			updateUi.sendMessage(msg);
		}
	}
}

class ApplicationList extends ArrayAdapter<ApplicationList.Data>
{
	public ApplicationList(Context context,ListView list,List<ApplicationList.Data> data)
	{
		super(context,0,data);
		this.list = list;
		this.data = data;
		this.list.setAdapter(this);
	}
	
	public ListView list;
	public List<Data> data;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v;
		if(convertView==null)
		{
			v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_application,null);
		}
		else
		{
			v = convertView;
		}
		
		Data d = getItem(position);
		TextView name = (TextView)v.findViewById(R.id.item_application_name);
		ImageView icon = (ImageView)v.findViewById(R.id.item_application_icon);
		name.setText(d.getName());
		icon.setImageDrawable(d.getIcon());
		
		return v;
	}
	
	public void onClick(Data d)
	{
	}
	
	public static class Data
	{

		public Data(String package_, String name, String packageName, Drawable icon)
		{
			this.package_ = package_;
			this.name = name;
			this.packageName = packageName;
			this.icon = icon;
		}
		
		public Data(){}
		
		private String package_,name,packageName;
		private Drawable icon;

		public void setPackage_(String package_)
		{
			this.package_ = package_;
		}

		public String getPackage_()
		{
			return package_;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}

		public void setPackageName(String packageName)
		{
			this.packageName = packageName;
		}

		public String getPackageName()
		{
			return packageName;
		}

		public void setIcon(Drawable icon)
		{
			this.icon = icon;
		}

		public Drawable getIcon()
		{
			return icon;
		}

		
	}
}
