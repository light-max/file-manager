package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import jysh.mf.R;
import android.view.*;

public class Progeress extends Dialog
{
	public Progeress(Context context)
	{
		super(context,R.style.DialogProgresses);
		setContentView
		(
			LayoutInflater.from(context)
			.inflate(R.layout.dialogprogress,null)
		);
	}
	
	public static final int DISMISS = 2;
}
