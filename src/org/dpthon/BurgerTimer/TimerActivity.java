package org.dpthon.BurgerTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
		final int GU[] = {
				R.drawable.beer, R.drawable.flog, R.drawable.suica,
				R.drawable.ufo, R.drawable.cherry, R.drawable.fish, R.drawable.takenoko,
				R.drawable.cheese_a, R.drawable.cheese_a,
				R.drawable.cheese_b, R.drawable.cheese_b,
				R.drawable.egg,
				R.drawable.lettuce, R.drawable.lettuce, R.drawable.lettuce, R.drawable.lettuce, R.drawable.lettuce,
				R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat,
				R.drawable.pain, R.drawable.pain,
				R.drawable.tomato, R.drawable.tomato, R.drawable.tomato, R.drawable.tomato, R.drawable.tomato
				};
		List<Integer> listGu;
		int drawableIds[] = new int[16];

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

			for (int i=0; i<drawableIds.length;i++) {
				drawableIds[i] = GU[mRnd.nextInt(GU.length)];
			}

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
			//float left = mWidth/2;
			float top = mHeight-200*scaleHeight;
			int drawableId;
			if (cnt >= 1) {
				bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.buns, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
//				canvas.drawBitmap(resizedBitmap, mWidth/2-300/2, mHeight-200+15, paintBitmap);
				System.out.println(bitmap.getWidth()+":"+bitmap.getHeight());
			}
			if (cnt >= 2) {
				drawableId = drawableIds[2-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 3) {
				drawableId = drawableIds[3-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 4) {
				drawableId = drawableIds[4-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 5) {
				drawableId = drawableIds[5-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 6) {
				drawableId = drawableIds[6-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 7) {
				drawableId = drawableIds[7-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 8) {
				drawableId = drawableIds[8-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 9) {
				drawableId = drawableIds[9-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 10) {
				drawableId = drawableIds[10-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				if(top > 0)
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 11) {
				drawableId = drawableIds[11-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				if(top > 0)
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 12) {
				drawableId = drawableIds[12-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				if(top > 0)
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 13) {
				drawableId = drawableIds[13-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				if(top > 0)
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 14) {
				drawableId = drawableIds[14-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				if(top > 0)
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 15) {
				drawableId = drawableIds[15-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				if(top > 0)
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 16) {
				drawableId = drawableIds[16-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				if(top > 0)
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}
			if (cnt >= 17) {
				drawableId = drawableIds[17-2];
				bitmap = BitmapFactory.decodeResource(getResources(), drawableId, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				if(top > 0)
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, top, paintBitmap);
			}

			if (cnt >= 18) {
				bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.buns_top, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				top = top-bitmap.getHeight()/2*scaleHeight;
				if(top > 0)
					top = 0;
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, 0, paintBitmap);
//				canvas.drawBitmap(resizedBitmap, mWidth/2-300/2, mHeight-200+15, paintBitmap);
//				System.out.println(bitmap.getWidth()+":"+bitmap.getHeight());
			}

			// サンプルテキスト
			TextPaint textPaint = new TextPaint();
			textPaint.setTextSize(24f);
			textPaint.setColor(Color.WHITE);
			textPaint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText("Cnt="+cnt, mCenterX, mCenterY, textPaint);


			// 現在の状態の変更
			canvas.restore();

			if (cnt == 18) {
				fanfare();

				bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.itstime, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				canvas.drawBitmap(resizedBitmap, mCenterX-bitmap.getWidth()/2*scaleWidth, bitmap.getHeight()/2*scaleHeight, paintBitmap);

				bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.delicious, mOpts);
				resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				canvas.drawBitmap(resizedBitmap, 0, mCenterY, paintBitmap);

				mHandler.removeCallbacks(mDrawThread);
			} else {
				// 次の描画をセット
				mHandler.removeCallbacks(mDrawThread);
				mHandler.postDelayed(mDrawThread, 500);
			}

			cnt++;
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
