package org.dpthon.BurgerTimer;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class TimerActivity extends Activity {
	private final Handler mHandler = new Handler();
	private MediaPlayer mMp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(new MySurfaceView(this));

	}

	@Override
	protected void onStop() {
		if (mMp != null) {
			if (mMp.isPlaying()) {
				mMp.stop();
			}
			mMp = null;
		}
		super.onStop();
	}

	class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
		private Random mRnd = new Random();
		private int mWidth, mHeight;
		private float mCenterX, mCenterY;
		private BitmapFactory.Options mOpts;

		public MySurfaceView(Context context) {
			super(context);

			// ビットマップオプションの設定
			mOpts = new BitmapFactory.Options();
			mOpts.inPurgeable = true;	//メモリ開放
			mOpts.inPreferredConfig = Bitmap.Config.ARGB_4444;	//16bit
			mOpts.inTempStorage = new byte[16*1024];

			// SurfaceHolder の取得
			SurfaceHolder holder = getHolder();
			holder.setFormat(PixelFormat.RGBA_8888);

			// SurfaceHolder に コールバックを設定
			holder.addCallback(this);
			holder.setFixedSize(getWidth(), getHeight());

			// フォーカスをあてる
			setFocusable(true);

		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			mHeight = height;
			mWidth = width;
			// 中心座標
			mCenterX = width / 2;
			mCenterY = height / 2;

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mHandler.post(mDrawThread);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			mHandler.removeCallbacks(mDrawThread);
		}

		// 描画スレッド
		private final Runnable mDrawThread = new Runnable() {
			public void run() {
				// 描画の開始
				Canvas canvas = getHolder().lockCanvas();
				draw(canvas);
				// 描画の終了
				getHolder().unlockCanvasAndPost(canvas);
			}
		};

		int cnt;
		@Override
		public void draw(final Canvas canvas) {
			// 現在の状態を保存
			canvas.save();

			// 背景
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.table, mOpts);
			Bitmap resizedBitmap = null;
			Matrix matrix = new Matrix();
			// calculate the scale - in this case
			float scaleWidth  = ((float) mWidth)  / bitmap.getWidth();
			float scaleHeight = ((float) mHeight) / bitmap.getHeight();
			// resize the bit map
			matrix.postScale(scaleWidth, scaleHeight);
			// recreate the new Bitmap
			resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			// 背景描画
			Paint paintBitmap = new Paint();
			canvas.drawBitmap(resizedBitmap, 0, 0, paintBitmap);

			// バーガー積み
			float left = mWidth/2;
			float top = mHeight-200;
			if (cnt >= 1) {
				bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.buns, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
//				canvas.drawBitmap(resizedBitmap, mWidth/2-300/2, mHeight-200+15, paintBitmap);
				System.out.println(bitmap.getWidth()+":"+bitmap.getHeight());
			}
			if (cnt >= 2) {
				bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.meat, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 3) {
				bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lettuce, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}


			// サンプルテキスト
			TextPaint textPaint = new TextPaint();
			textPaint.setTextSize(24f);
			textPaint.setColor(Color.WHITE);
			textPaint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText("Cnt="+cnt, mCenterX, mCenterY, textPaint);


			// 現在の状態の変更
			canvas.restore();

			cnt++;

			if (cnt == 18) {
				fanfare();
				mHandler.removeCallbacks(mDrawThread);
			} else {
				// 次の描画をセット
				mHandler.removeCallbacks(mDrawThread);
				mHandler.postDelayed(mDrawThread, 1000);
			}
		}

		private void fanfare() {
			mMp = MediaPlayer.create(TimerActivity.this, R.raw.fanfare);
//			try {
//				mMp.prepare(); // 準備
				mMp.seekTo(0);
				mMp.start(); // 再生
//			} catch (IOException e) {
//			}

		}
	}

}
