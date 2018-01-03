package jysh.mf.Util;
import android.database.sqlite.*;
import java.io.*;
import java.util.*;
import jysh.mf.Widget.*;
import android.database.*;

public class dbtool
{
	private static final String databasePath = "/storage/emulated/0/Android/data/com.jysh.mf/books.db";
	
	private static final String createDriBook =
	"create table dri("
		+"time longer,"
		+"name text,"
		+"path text);";
		
	private static final String createFileBook =
	"create table file("
		+"time longer,"
		+"name text,"
		+"path text);";
		
	public static SQLiteDatabase sql = null;
	
	public static void InitDataBase()
	{
		boolean b = new File(databasePath).exists();
		new File(databasePath).getParentFile().mkdir();
		sql = SQLiteDatabase.openDatabase
		(
			databasePath,
			null,
			SQLiteDatabase.CREATE_IF_NECESSARY | SQLiteDatabase.OPEN_READWRITE
		);
		if(b)
		{
			return;
		}
		sql.execSQL(createDriBook);
		sql.execSQL(createFileBook);
	}
	
	public static List<DriLayout.Data> initDri()
	{
		List<DriLayout.Data> data = new ArrayList<>();
		Cursor cursor = sql.query("dri",null,null,null,null,null,null);
		if(cursor.moveToFirst())
		{
			do{
				long time = cursor.getLong(cursor.getColumnIndex("time"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String path = cursor.getString(cursor.getColumnIndex("path"));
				data.add(new DriLayout.Data(time,name,path));
			}while(cursor.moveToNext());
		}
		return data;
	}
	
	public static void addDri(DriLayout.Data data)
	{
		sql.execSQL
		(
			"insert into dri (time,name,path) values(?,?,?)",
			new String[]{data.getTime()+"",data.getName(),data.getPath()}
		);
	}
	
	public static void deleteDri(DriLayout.Data data)
	{
		sql.execSQL
		(
			"delete from dri where time=? and name=? and path=?",
			new String[]{data.getTime()+"",data.getName(),data.getPath()}
		);
	}
	
	
	
	public static List<FileLayout.Data> initFile()
	{
		List<FileLayout.Data> data = new ArrayList<>();
		Cursor cursor = sql.query("file",null,null,null,null,null,null);
		if(cursor.moveToFirst())
		{
			do{
				long time = cursor.getLong(cursor.getColumnIndex("time"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String path = cursor.getString(cursor.getColumnIndex("path"));
				data.add(new FileLayout.Data(time,name,path));
			}while(cursor.moveToNext());
		}
		return data;
	}

	public static void addFile(FileLayout.Data data)
	{
		sql.execSQL
		(
			"insert into file (time,name,path) values(?,?,?)",
			new String[]{data.getTime()+"",data.getName(),data.getPath()}
		);
	}
	
	public static void deleteFile(FileLayout.Data data)
	{
		sql.execSQL
		(
			"delete from file where time=? and name=? and path=?",
			new String[]{data.getTime()+"",data.getName(),data.getPath()}
		);
	}
}
