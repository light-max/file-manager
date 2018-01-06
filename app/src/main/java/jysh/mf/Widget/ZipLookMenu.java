package jysh.mf.Widget;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import jysh.mf.R;

public class ZipLookMenu extends LinearLayout
{
	public ZipLookMenu(Context context,AttributeSet attr)
	{
		super(context,attr);
		LayoutInflater.from(context).inflate(R.layout.pop_ziplook_menu,this);
		b1 = findViewById(R.id.pop_ziplookmenu1);
		b2 = findViewById(R.id.pop_ziplookmenu2);
	}
	
	public View b1,b2;
}
