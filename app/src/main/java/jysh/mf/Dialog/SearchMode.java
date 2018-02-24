package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import jysh.mf.R;
import android.view.*;
import jysh.mf.Activity.*;
import android.widget.*;
import jysh.mf.Util.*;
import jysh.mf.ThreadTool.searchThread;

public class SearchMode extends Dialog implements View.OnClickListener
{
	public SearchMode(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_searchmode,null,false);
		setContentView(v);
		for(int i = 0;i < id.length;i++)
		{
			radio[i] = (RadioButton)findViewById(id[i]);
			radio[i].setOnClickListener(this);
		}
		edit = (EditText)findViewById(R.id.dialog_searchmode_expression);
		findViewById(R.id.dialog_searchmode_button)
		.setOnClickListener(new Start());
	}

	@Override
	public void onClick(View v)
	{
		for(int i = 0;i < id.length;i++)
		{
			if(v.getId()==id[i])
			{
				context.mode = i;
				showRadio(i);
			}
		}
	}
	
	private static final int[] id = new int[]{
		R.id.dialog_searchmode_radio1,
		R.id.dialog_searchmode_radio2,
		R.id.dialog_searchmode_radio3,
		R.id.dialog_searchmode_radio4,
	};
	private RadioButton[] radio = new RadioButton[id.length];
	private EditText edit;
	private Search context;
	
	public SearchMode setActivity(Search context)
	{
		this.context = context;
		showRadio(context.mode);
		edit.setText(context.expression);
		return this;
	}
	
	private void showRadio(int i)
	{
		for(RadioButton radio:this.radio)
		{
			radio.setChecked(false);
		}
		radio[i].setChecked(true);
	}
	
	class Start implements View.OnClickListener
	{
		@Override
		public void onClick(View p1)
		{
			context.expression = edit.getText().toString();
			context.message.setVisibility(View.GONE);
			dismiss();
			uitool.toos(context,context.mode+"");
			switch(context.mode)
			{
			case 0:
				searchThread t = new searchThread();/*{
					@Override
					public void run()
					{
						
					}
				}*/
			/*	.setActivity(context)
				.setAdapterView(context.list)*/t.start();
				break;
			case 1:
				
				break;
			case 2:
				
				break;
			case 3:
				
				break;
			}
		}
	}
}
