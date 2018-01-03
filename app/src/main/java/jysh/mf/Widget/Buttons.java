package jysh.mf.Widget;
import android.app.*;
import java.io.*;
import android.view.*;
import jysh.mf.R;
import jysh.mf.Util.*;
import android.widget.*;

public class Buttons implements View.OnClickListener
{
	public Buttons()
	{
		v_call = (ImageView)uitool.mainThis.findViewById(R.id.main_call);
		v_cancel = (ImageView)uitool.mainThis.findViewById(R.id.main_cancel);
		v_call.setVisibility(View.VISIBLE);
		v_cancel.setVisibility(View.VISIBLE);
		v_call.setOnClickListener(this);
		v_cancel.setOnClickListener(this);
	}
	
	private ImageView v_call,v_cancel;
	private OnClick call,cancel;

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.main_call:
				if(call!=null) call.OnClick();
				break;
			case R.id.main_cancel:
				if(cancel!=null) cancel.OnClick();
				break;
		}
		setVisible(View.GONE);
	}
	
	public static interface OnClick
	{
		public void OnClick();
	}
	
	public Buttons setCall(int res,OnClick on)
	{
		v_call.setImageResource(res);
		call = on;
		return this;
	}
	
	public Buttons setCancel(int res,OnClick on)
	{
		v_cancel.setImageResource(res);
		cancel = on;
		return this;
	}
	
	public Buttons setVisible(int v)
	{
		v_call.setVisibility(v);
		v_cancel.setVisibility(v);
		return this;
	}
}
