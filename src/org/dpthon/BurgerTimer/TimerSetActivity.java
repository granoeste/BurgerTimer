package org.dpthon.BurgerTimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class TimerSetActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.timerset);
	}

	public void onClick(View v) {
		Intent intent = new Intent(getApplication(), TimerActivity.class);
		startActivity(intent);
		TimerSetActivity.this.finish();
	}

}
