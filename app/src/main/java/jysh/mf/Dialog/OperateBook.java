package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import jysh.mf.R;
import android.view.*;

public class OperateBook extends Dialog
{
	public OperateBook(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_operatebook,null,false);
		super.setContentView(v);
		bt1 = findViewById(R.id.dialog_operatebook_bt1);
		bt2 = findViewById(R.id.dialog_operatebook_bt2);
	}
	
	public View bt1,bt2;
}
