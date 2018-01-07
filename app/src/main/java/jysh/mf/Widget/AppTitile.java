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
import jysh.mf.Dialog.*;
import android.graphics.drawable.*;

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
		
		drawLayout = (ImageView)findViewById(R.id.title_imagebutton_drawlayout);
		openmMenu = (ImageView)findViewById(R.id.title_imagebutton_openmenu);
		drawLayout.setOnClickListener(this);
		openmMenu.setOnClickListener(this);
	}
	
	public void setPosition(int position)
	{
		listAdapt.setPosition(position);
		titleList.smoothScrollToPosition(position);
	}
	
	private RecyclerView titleList;
	public TitleListAdapter listAdapt;
	private ImageView drawLayout;
	private ImageView openmMenu;
	public static final String DRITITLE = "目录标签";

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.title_imagebutton_drawlayout:
				uitool.mainDrawer.openDrawer(Gravity.START);
				break;
			case R.id.title_imagebutton_openmenu:
				PopMenu menu = null;
				PopupWindow pop = new PopupWindow
				(
					menu = new PopMenu(getContext(),null),
					GridLayout.LayoutParams.WRAP_CONTENT,
					GridLayout.LayoutParams.WRAP_CONTENT
				);
				pop.setBackgroundDrawable(new BitmapDrawable());
				pop.setFocusable(true);
				pop.showAtLocation(uitool.mainDrawer,Gravity.TOP | Gravity.RIGHT,0,200);
				menu.setPop(pop);
				break;
		}
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
					uitool.add.setPosition(position);
					uitool.pagerAdapter.setPosition(position);
				}
			});
			view.setOnLongClickListener(new View.OnLongClickListener(){
				@Override
				public boolean onLongClick(View v)
				{
					new MessageBox(uitool.mainThis)
						.setTitle("删除标签")
						.setMessage("你确定要删除这个"+holder.title.getText().toString()+"吗？")
						.setLeft("取消")
						.setRight("确认")
						.setRight(new MessageBox.onButton(){
							@Override
							public void onClick()
							{
								int position = holder.getPosition();
								if(uitool.deleteTitleList(position))
									uitool.mainThis.finish();
							}
						})
						.show();
					return false;
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
	
	public void remove(int position)
	{
		listAdapt.data.remove(position);
	}
}
