package jysh.mf.Dialog;
import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import jysh.mf.*;

public class EditBox extends Dialog implements View.OnClickListener
{
	public EditBox(Context context)
	{
		super(context,R.style.Dialog);
		this.context = context;
		View view = LayoutInflater.from(context)
			.inflate(R.layout.editbox,null);
		super.setContentView(view);
		title = (TextView)findViewById(R.id.messagebox_title);
		message = (EditText)findViewById(R.id.messagebox_message);
		left = (TextView)findViewById(R.id.messagebox_btletf);
		right = (TextView)findViewById(R.id.messagebox_btright);
		left.setOnClickListener(this);
		right.setOnClickListener(this);
	}

	private Context context;
	private TextView title,left,right;
	private EditText message;
	private onButton leftButton = null,rightButton = null;

	public EditBox setLeft(onButton left)
	{
		this.leftButton = left;
		return this;
	}

	public EditBox setRight(onButton right)
	{
		this.rightButton = right;
		return this;
	}

	public EditBox setTitle(String title)
	{
		this.title.setText(title);
		return this;
	}

	public EditBox setLeft(String left)
	{
		this.left.setText(left);
		return this;
	}

	public EditBox setRight(String right)
	{
		this.right.setText(right);
		return this;
	}
	
	public String getMessage()
	{
		return message.getText().toString();
	}
	
	public EditBox setMessage(String message)
	{
		this.message.setText(message);
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
