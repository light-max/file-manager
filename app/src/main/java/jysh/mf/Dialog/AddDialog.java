package jysh.mf.Dialog;
import android.app.*;
import android.content.*;
import jysh.mf.R;
import android.view.*;

public class AddDialog extends Dialog implements View.OnClickListener
{
	public AddDialog(Context context)
	{
		super(context,R.style.Dialog);
		View view = LayoutInflater.from(context)
			.inflate(R.layout.dialog_new,null);
		super.setContentView(view);
		for(int i:btid)
		{
			findViewById(i).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v)
	{
		click.onClick(v.getId());
		dismiss();
	}
	
	static public interface OnClick
	{
		public void onClick(int viewId);
	}
	
	static public final int btid[] = new int[]{
		R.id.dialognew_newfile,
		R.id.dialognew_newdri,
		R.id.dialognew_drititle,
		R.id.dialognew_savetitle
	};
	private OnClick click;
	
	public AddDialog setOnClick(OnClick click)
	{
		this.click = click;
		return this;
	}
}
