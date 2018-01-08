package jysh.mf.Activity;

import android.app.*;
import android.os.*;
import android.view.*;
import jysh.mf.*;
import android.content.*;
import android.widget.*;

public class AppSet extends Activity implements View.OnClickListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appset);
		for(int i:id)
		{
			findViewById(i).setOnClickListener(this);
		}
		check = new CheckBox[]{
			(CheckBox)findViewById(R.id.activity_appset_check1),
			(CheckBox)findViewById(R.id.activity_appset_check2),
			(CheckBox)findViewById(R.id.activity_appset_check3),
		};
		for(CheckBox c:check)
		{
			c.setOnClickListener(this);
		}
		check[0].setChecked(isShowImages());
		check[1].setChecked(isShowDriUp());
		check[2].setChecked(isShowHidden());
		
		text = new TextView[]{
			(TextView)findViewById(R.id.activity_appset_message1),
			(TextView)findViewById(R.id.activity_appset_message2),
			(TextView)findViewById(R.id.activity_appset_message3),
		};
		setText();
	}
	
	private void setText()
	{
		if(isShowImages())
		{
			text[0].setText("图片文件的图标将会显示该图片的缩略图");
		}
		else
		{
			text[0].setText("图片文件都会已默认的方式显示图标");
		}
		if(isShowDriUp())
		{
			text[1].setText("所有文件夹将会在文件的上方");
		}
		else
		{
			text[1].setText("文件列表按默认方式显示");
		}
		if(isShowHidden())
		{
			text[2].setText("以.开头的文件会显示出来");
		}
		else
		{
			text[2].setText("以.开头的文件将会隐藏");
		}
	}

	@Override
	public void onClick(View v)
	{
		int i = v.getId();
		if(i==id[0]||i==check[0].getId())
		{
			showImages = ! isShowImages();
			check[0].setChecked(isShowImages());
		}
		if(i==id[1]||i==check[1].getId())
		{
			showDriUp = ! isShowDriUp();
			check[1].setChecked(isShowDriUp());
		}
		if(i==id[2]||i==check[2].getId())
		{
			showHidden = ! isShowHidden();
			check[2].setChecked(isShowHidden());
		}
		SetIni(this);
		setText();
	}
	
	private static final int[] id = new int[]{
		R.id.activity_appset_layout1,
		R.id.activity_appset_layout2,
		R.id.activity_appset_layout3,
	};
	private CheckBox check[];
	private TextView text[];
	
	// 是否显示缩略图
	public static boolean showImages;
	public static boolean isShowImages()
	{
		return showImages;
	}
	// 文件夹是否置顶
	public static boolean showDriUp;
	public static boolean isShowDriUp()
	{
		return showDriUp;
	}
	// 是否显示隐藏目录
	public static boolean showHidden;
	public static boolean isShowHidden()
	{
		return showHidden;
	}
	
	public static void InitIni(Activity context)
	{
		SharedPreferences pref = context.getSharedPreferences("setini",MODE_PRIVATE);
		showImages = pref.getBoolean("showImages",true);
		showDriUp = pref.getBoolean("showDriUp",true);
		showHidden = pref.getBoolean("showHidden",false);
	}
	
	public static void SetIni(Activity context)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences("setini",MODE_PRIVATE).edit();
		editor.putBoolean("showImages",showImages)
			.putBoolean("showDriUp",showDriUp)
			.putBoolean("showHidden",showHidden)
			.apply();
	}
}
