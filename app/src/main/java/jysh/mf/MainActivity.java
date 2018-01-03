package jysh.mf;

import android.app.*;
import android.os.*;
import java.util.*;
import jysh.mf.Util.*;
import jysh.mf.Widget.*;
import jysh.mf.Adapter.*;
import android.support.v4.view.*;
import jysh.mf.Dialog.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		dbtool.InitDataBase();
        setContentView(R.layout.main);
		uitool.mainThis = this;
		uitool.InitViewWidget(this);
		
		// 自定义布局不这样就会闪退
		uitool.drawlayout.dri.loadDatabase();
		uitool.drawlayout.file.loadDatabase();
    }

	@Override
	public void onBackPressed()
	{
		int position = uitool.add.getPosition();
		if(uitool.pagerAdapter.get(position).Backpressed())
		{
			if(uitool.deleteTitleList(position))
				super.onBackPressed();
		}
	}
	
	public Handler UpdateUi = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case LayoutFileList.UPDATE:
					LayoutFileList f = uitool.pagerAdapter.get(uitool.add.getPosition());
					f.notifyDataSetChanged();
					break;
				case Progeress.DISMISS:
					uitool.popselect.cancel();
					uitool.progerss.dismiss();
					if(msg.obj!=null)
					{
						for(LayoutFileList v:(List<LayoutFileList>)msg.obj)
						{
							v.listadp.loadList();
							v.listadp.notifyDataSetChanged();
						}
					}
					break;
				case uitool.TOAS:
					uitool.toos(uitool.mainThis,(String)msg.obj);
					break;
			}
		}
	};
}
