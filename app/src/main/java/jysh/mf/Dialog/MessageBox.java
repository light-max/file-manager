package jysh.mf.Dialog;
import android.app.*;
import android.content.*;
import android.view.*;
import jysh.mf.R;
import android.widget.*;

public class MessageBox extends Dialog implements View.OnClickListener
{
	public MessageBox(Context context)
	{
		super(context,R.style.Dialog);
		this.context = context;
		View view = LayoutInflater.from(context)
			.inflate(R.layout.messagebox,null);
		super.setContentView(view);
		title = (TextView)findViewById(R.id.messagebox_title);
		message = (TextView)findViewById(R.id.messagebox_message);
		left = (TextView)findViewById(R.id.messagebox_btletf);
		right = (TextView)findViewById(R.id.messagebox_btright);
		left.setOnClickListener(this);
		right.setOnClickListener(this);
	}
	
	private Context context;
	private TextView title,message,left,right;
	private onButton leftButton = null,rightButton = null;

	public MessageBox setLeft(onButton left)
	{
		this.leftButton = left;
		return this;
	}

	public MessageBox setRight(onButton right)
	{
		this.rightButton = right;
		return this;
	}

	public MessageBox setTitle(String title)
	{
		this.title.setText(title);
		return this;
	}

	public MessageBox setMessage(String message)
	{
		this.message.setText(message);
		return this;
	}

	public MessageBox setLeft(String left)
	{
		this.left.setText(left);
		return this;
	}

	public MessageBox setRight(String right)
	{
		this.right.setText(right);
		return this;
	}

	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.messagebox_btletf && leftButton!=null)
			leftButton.onClick();
		if(v.getId()==R.id.messagebox_btright && rightButton!=null)
			rightButton.onClick();
		dismiss();
	}
	
	static public interface onButton
	{
		public void onClick();
	}
}
