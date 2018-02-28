package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import jysh.mf.R;
import android.view.*;

public class DialogAbout extends Dialog
{
	public DialogAbout(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_about,null);
		super.setContentView(v);
		findViewById(R.id.dialog_about_exit)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});
	}
}
