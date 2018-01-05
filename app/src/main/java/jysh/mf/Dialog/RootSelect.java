package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import jysh.mf.R;
import android.widget.*;
import android.view.*;
import java.util.*;
import java.io.*;
import android.graphics.*;
import jysh.mf.Util.*;

public class RootSelect extends Dialog
{
	public RootSelect(Context context)
	{
		super(context,R.style.Dialog);
		View v = LayoutInflater.from(context)
			.inflate(R.layout.dialog_rootselect,null,false);
		setContentView(v);
		view = new Adapter
		(
			context,
			(ListView)findViewById(R.id.dialog_rootselect_list),
			new ArrayList<File>()
		);
		findViewById(R.id.dialog_rootselect_return)
			.setOnClickListener(new onBackup());
		findViewById(R.id.dialog_rootselect_select)
			.setOnClickListener(new onSelect());
	}

	public RootSelect setRootDialog(ZipCompress rootDialog)
	{
		this.rootDialog = rootDialog;
		return this;
	}
	
	class onBackup implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			File up = view.getFp();
			if(up.getPath().equals("/"))
			{
				uitool.toos(uitool.mainThis,"没有上一级了");
				return;
			}
			view.setFp(up.getParentFile());
			view.loadListData();
			view.notifyDataSetChanged();
			setPath(view.getFp().getPath());
		}
	}
	
	class onSelect implements View.OnClickListener
	{
		@Override
		public void onClick(View p1)
		{
			rootDialog.setPathAndName(view.getFp());
			dismiss();
		}
	}
	
	private Adapter view;
	private ZipCompress rootDialog;
	
	public class Adapter extends ArrayAdapter<File>
	{
		public Adapter(Context context,ListView view,List<File> obj)
		{
			super(context,0,obj);
			this.data = obj;
			this.view = view;
			this.view.setAdapter(this);
			setFp(new File("/storage/emulated/0/"));
			loadListData();
			notifyDataSetInvalidated();
			setPath(getFp().getPath());
		}
		
		public ListView view;
		public List<File> data;
		private File fp;

		public void setFp(File fp)
		{
			this.fp = fp;
		}

		public File getFp()
		{
			return fp;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final File fp = data.get(position);
			TextView v = (TextView)LayoutInflater.from(getContext())
				.inflate(R.layout.textview_dri,parent,false);
			v.setText(fp.getName());
			v.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					setFp(fp);
					loadListData();
					notifyDataSetChanged();
					setPath(fp.getPath());
				}
			});
			return v;
		}
		
		private void loadListData()
		{
			data.clear();
			for(File f:fp.listFiles())
			{
				if(f.isDirectory()&&f.canRead()&&f.canWrite())
					data.add(f);
			}
		}
	}
	
	private void setPath(String path)
	{
		TextView text = (TextView)findViewById(R.id.dialog_rootselect_path);
		text.setText(path);
	}
}
