package jysh.mf.Util;
import android.app.*;
import android.content.*;

public class activitytool
{
	public static void startActivity(Activity activity,Class<?> clasz)
	{
		Intent intent = new Intent(activity,clasz);
		activity.startActivity(intent);
	}
	
	public static void startActivity(Activity activity,Class<?> clasz,String[] key,String[] data)
	{
		Intent intent = new Intent(activity,clasz);
		for(int i = 0;i < key.length;i++)
		{
			intent.putExtra(key[i],data[i]);
		}
		activity.startActivity(intent);
	}
}
