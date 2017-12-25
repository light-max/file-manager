package jysh.mf.Widget;
import android.widget.*;
import android.content.*;
import android.view.*;
import jysh.mf.R;
import android.support.v7.widget.*;
import android.util.*;
import java.util.*;
import android.graphics.*;
import jysh.mf.Util.*;
import android.app.*;

public class AppTitile extends LinearLayout implements View.OnClickListener
{
	public AppTitile(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		LayoutInflater.from(context).inflate(R.layout.title,this);
		
		titleList = (RecyclerView)findViewById(R.id.title_recyclerview_titlelist);
		LinearLayoutManager layoutManager = new LinearLayoutManager(context);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		titleList.setLayoutManager(layoutManager);
		listAdapt = new TitleListAdapter();
		titleList.setAdapter(listAdapt);
		
		drawLayout = (ImageButton)findViewById(R.id.title_imagebutton_drawlayout);
		openmMenu = (ImageButton)findViewById(R.id.title_imagebutton_openmenu);
		drawLayout.setOnClickListener(this);
		openmMenu.setOnClickListener(this);
	}
	
	public void setPosition(int position)
	{
		listAdapt.setPosition(position);
	}
	
	private RecyclerView titleList;
	private TitleListAdapter listAdapt;
	private ImageButton drawLayout;
	private ImageButton openmMenu;

	@Override
	public void onClick(View v)
	{
		
	}
	
	static public class TitleListAdapter extends RecyclerView.Adapter<TitleListAdapter.ViewHolder>
	{
		public TitleListAdapter()
		{
			data = new ArrayList<String>();
			position = 0;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			TextView view = (TextView)LayoutInflater.from(parent.getContext())
				.inflate(R.layout.textview_title,parent,false);
			final ViewHolder holder = new ViewHolder(view);
			holder.title.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					int position = holder.getPosition();
					setPosition(position);
				//	uitool.add.setPosition(position);
					uitool.pagerAdapter.setPosition(position);
				}
			});
			return holder;
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position)
		{
			holder.title.setText(data.get(position));
			if(position==this.position)
			{
				holder.title.setBackgroundColor(Color.parseColor("#aaaaaa"));
				return;
			}
			holder.title.setBackground(null);
		}

		@Override
		public int getItemCount()
		{
			return data.size();
		}
		
		static public class ViewHolder extends RecyclerView.ViewHolder
		{
			public TextView title;
			public ViewHolder(TextView view)
			{
				super(view);
				title = view;
			}
		}
		public List<String> data;
		private int position;
		
		public void setPosition(int position)
		{
			this.position = position;
			notifyDataSetChanged();
		}
	}
	
	public void add(String title)
	{
		listAdapt.data.add(title);
	}

	public void clear()
	{
		listAdapt.data.clear();
	}
	
	public void notifyDataSetChanged()
	{
		listAdapt.notifyDataSetChanged();
	}
}
