package org.dpthon.BurgerTimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class TimerSetActivity extends Activity {

	private final Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timerset);
	}

	public void onClick(View v) {
		Intent intent = new Intent(getApplication(), TimerActivity.class);
		startActivity(intent);
		TimerSetActivity.this.finish();
	}

}
