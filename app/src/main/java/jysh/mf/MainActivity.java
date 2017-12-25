package jysh.mf;

import android.app.*;
import android.os.*;
import jysh.mf.Widget.*;
import jysh.mf.Util.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		uitool.InitViewWidget(this);
    }

	@Override
	public void onBackPressed()
	{
	//	if(uitool.flist.Backpressed())
		{
			super.onBackPressed();
		}
	}
}
