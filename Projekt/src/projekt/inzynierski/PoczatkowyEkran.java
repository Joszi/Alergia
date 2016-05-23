package projekt.inzynierski;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PoczatkowyEkran extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		Thread timer = new Thread() {
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Intent openProjektActivity = new Intent(
							"projekt.inzynierski.POLACZENIEEKRAN");
					startActivity(openProjektActivity);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}
