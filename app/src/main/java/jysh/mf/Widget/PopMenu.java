package jysh.mf.Widget;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import jysh.mf.R;
import jysh.mf.Util.*;

public class PopMenu extends LinearLayout implements View.OnClickListener
{
	public PopMenu(Context context,AttributeSet attr)
	{
		super(context,attr);
		LayoutInflater.from(context).inflate(R.layout.pop_menu,this);
		for(int i:id)
		{
			findViewById(i).setOnClickListener(this);
		}
	}
	
	private PopupWindow pop;
	
	private static final int id[] = new int[]{
		R.id.pop_menu_search,
		R.id.pop_menu_sort,
		R.id.pop_menu_savedri,
		R.id.pop_menu_re,
		R.id.pop_menu_exit,
	};

	@Override
	public void onClick(View v)
	{
		if(v.getId()==id[4])
			uitool.mainThis.finish();
		pop.dismiss();
	}
	
	void setPop(PopupWindow pop)
	{
		this.pop = pop;
	}
}
