package com.elena.foliotouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

////////////////////////
public class MyFolioReceiver extends BroadcastReceiver {

@Override
public void onReceive(Context context, Intent intent) {
Bundle extras = intent.getExtras();
if (extras != null) {
//String state = extras.getString("state");
Log.d("MyFolioApp", "not null extras");

}
}

} 
