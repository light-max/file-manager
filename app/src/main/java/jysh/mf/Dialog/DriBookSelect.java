package jysh.mf.Dialog;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import jysh.mf.*;
import jysh.mf.Util.*;
import jysh.mf.Widget.*;

public class DriBookSelect extends Dialog
{
	public DriBookSelect(Context context)
	{
		super(context,R.style.Dialog);
		ListView view = (ListView)LayoutInflater.from(context)
			.inflate(R.layout.listview_popbook,null,false);
		setContentView(view);
		adapter = new bookList(context,view,dbtool.initDri());
		if(adapter.data.size()==0)
		{
			dismiss();
			uitool.toos(uitool.mainThis,"你还没有添加目录书签");
		}
	}
	
	private ZipCompress rootDialog;
	private bookList adapter;

	public DriBookSelect setRootDialog(ZipCompress rootDialog)
	{
		this.rootDialog = rootDialog;
		return this;
	}
	
	class bookList extends ArrayAdapter<DriLayout.Data>
	{
		public bookList(Context context,ListView view,List<DriLayout.Data> data)
		{
			super(context,0,data);
			this.view = view;
			this.view.setAdapter(this);
			this.data = data;
		}

		public ListView view;
		public List<DriLayout.Data> data;

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v;
			final DriLayout.Data d = data.get(position);
			if(convertView==null)
			{
				v = LayoutInflater.from(getContext())
					.inflate(R.layout.item_dribook,null,false);
			}
			else
			{
				v = convertView;
			}
			TextView name = (TextView)v.findViewById(R.id.item_dribook_name);
			TextView path = (TextView)v.findViewById(R.id.item_dribook_path);
			name.setText(d.getName());
			path.setText(d.getPath());
			v.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					if(!new File(d.getPath()).isDirectory())
					{
						uitool.toos(uitool.mainThis,"这个目录是无效的");
						return;
					}
					dismiss();
					rootDialog.setPathAndName(new File(d.getPath()));
				}
			});
			return v;
		}
	}
}
