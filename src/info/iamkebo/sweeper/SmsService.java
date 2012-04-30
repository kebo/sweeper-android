package info.iamkebo.sweeper;

import java.util.List;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

public class SmsService extends IntentService {

	public SmsService() {
		super("SmsService");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private void deleteSms(){
		Uri uri = Uri.parse("content://sms");
		DataHelper db = new DataHelper(this);
		List<String> numbers = db.getAll();
		for(String number: numbers){
			getContentResolver().delete(uri, "address=?", new String[]{number});
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		deleteSms();
		stopSelf();
	}
	
}
