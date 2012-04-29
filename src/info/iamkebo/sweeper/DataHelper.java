package info.iamkebo.sweeper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * data manager, used for store numbers
 * @author kebo
 *
 */

public class DataHelper{

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "sweeper";
	private static final String DATABASE_TABLE_NAME = "numbers";
	private static final String KEY_NUMBER = "number";
	private SQLiteDatabase db;
	private final DBOpenHelper dbOpenHelper;
	
	
	public DataHelper(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
		establishDB();
	}

	private void establishDB(){
		if(db == null){
			db = dbOpenHelper.getWritableDatabase();
		}
	}
	
	public void cleanup(){
		if(db != null){
			db.close();
			db = null;
		}
	}
	
	public void insert(String number){
		ContentValues values = new ContentValues();
		values.put(KEY_NUMBER, number);
		db.insert(DATABASE_TABLE_NAME, null, values);
	}
	
	public List<String> getAll(){
		List<String> numbers = new ArrayList<String>();
		Cursor c = null;
		try{
			c = db.query(DATABASE_TABLE_NAME, 
					new String[]{KEY_NUMBER}, 
					null, null, null, null, null);
			int rows = c.getCount();
			c.moveToFirst();
			for(int i=0;i<rows;i++){
				numbers.add(c.getString(0));
				c.moveToNext();
			}
		}catch(SQLException e){
			Log.v("Sweeper", DataHelper.class.getName(), e);
		}finally{
			if(c!=null && !c.isClosed() ){
				c.close();
			}
		}
		return numbers;
	}
	
	public boolean isExist(String number){
		boolean exist = false;
		Cursor c = null;
		try{
			c = db.query(DATABASE_TABLE_NAME, new String[]{KEY_NUMBER}, 
					KEY_NUMBER + "=?", new String[]{number}, null, null, null);
			if(c.getCount()>0)
				exist = true;
		}catch(SQLException e){
			Log.v("Sweeper", DataHelper.class.getName(), e);
		}finally{
			if(c != null && !c.isClosed()){
				c.close();
			}
		}
		
		return exist;
	}
	
	public int delete(String number){
		return db.delete(DATABASE_TABLE_NAME, KEY_NUMBER + "=?", new String[]{number});
	}
	
	
	private static class DBOpenHelper extends SQLiteOpenHelper{
		
		private static final String DATABASE_TABLE_CREATE = 
				"CREATE TABLE " + DATABASE_TABLE_NAME + "(" +
				KEY_NUMBER + " TEXT);";
		
		public DBOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_TABLE_CREATE);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
	}

}
