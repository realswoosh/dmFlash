package com.dm.dmflash;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Camera camera;
	private Button button;
	private boolean islighton = false;
	
	private SoundPool soundpool;
	private int soundswitch;
	
	@Override
	protected void onStop()
	{
		super.onStop();
		if (camera != null)
			camera.release();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 액티비티가 떠 있을경우
		// 화면이 꺼지지 않게 한다.
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// 사운드 파일 로딩
		soundpool   = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		soundswitch = soundpool.load(getApplicationContext(), R.raw.sound_switch, 1 );
		
		button = (Button)findViewById(R.id.buttonFlashlight);
		
		Log.e("INFO", "onCreate");
		
		Context context = this;
		PackageManager pm = context.getPackageManager();
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA))
		{
			Log.e("ERR", "Device has no camera!");
			return;
		}
		
		// 카메라 얻기
		camera = Camera.open();
		
		if (camera != null)
		{
			final Parameters p = camera.getParameters();
			
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					soundpool.play(soundswitch, 1f, 1f, 0, 0, 1f);
					
					if (islighton) {
						Log.i("info", "torch is turn off!");
						button.setBackgroundResource(R.drawable.button_off);
						p.setFlashMode(Parameters.FLASH_MODE_OFF);
						camera.setParameters(p);
						camera.stopPreview();
						islighton = false;
					} else {
						button.setBackgroundResource(R.drawable.button_on);
						Log.i("info", "torch is turn on!");
						p.setFlashMode(Parameters.FLASH_MODE_TORCH);
						camera.setParameters(p);
						camera.startPreview();
						islighton = true;
					}
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
