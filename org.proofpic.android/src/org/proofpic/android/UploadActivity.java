package org.proofpic.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class UploadActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Intent intent = getIntent();
		if (intent.getAction().equals(Intent.ACTION_SEND)) {
			Bundle extras = intent.getExtras();
			if (extras.containsKey("android.intent.extra.STREAM"))
				upload((Uri)extras.get("android.intent.extra.STREAM"));
		}
		finish();
	}

	private void upload(Uri uri) {
		Log.d(getClass().toString(), "upload: " + uri);
	}
	
}
