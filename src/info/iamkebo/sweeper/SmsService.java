package info.iamkebo.sweeper;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

public class SmsService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		deleteSms();
		return START_NOT_STICKY;
	}
	
	private void deleteSms(){
		Uri uri = Uri.parse("content://sms");
		DataHelper db = new DataHelper(this);
		List<String> numbers = db.getAll();
		for(String number: numbers){
			getContentResolver().delete(uri, "address=?", new String[]{number});
		}
	}
	
}
