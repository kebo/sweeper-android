package info.iamkebo.sweeper;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

	private static final String SMS_REC_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(!intent.getAction().equals(SMS_REC_ACTION)){
			return;
		}
		
		Bundle info = intent.getExtras();
		if(info != null){
			Object[] pdus = (Object[])info.get("pdus");
			DataHelper db = new DataHelper(context);
			List<String> numbers = db.getAll();
			db.cleanup();
			for(Object pdu: pdus){
				SmsMessage sms = SmsMessage.createFromPdu((byte[])pdu);
				String address = sms.getDisplayOriginatingAddress();
				if(numbers.contains(address)){
					abortBroadcast();
					context.startService(new Intent(context, SmsService.class));
				}
			}
		}
		
	}

}
