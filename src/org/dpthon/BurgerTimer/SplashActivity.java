package org.dpthon.BurgerTimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
	}

	public void onClick(View v) {
		Intent intent = new Intent(getApplication(), TimerSetActivity.class);
		startActivity(intent);
		SplashActivity.this.finish();
	}
}