package projekt.inzynierski;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WidokPowiadomieniaEkran extends Activity implements
		OnClickListener {

	private TextView tvTytul;
	private TextView tvGodzina;
	private TextView tvCyklicznosc;
	private TextView tvOpis;
	private Button btnUsunZdarzenie;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widokpowiadomieniaekran);

		initialize();
	}

	public void initialize() {
		tvTytul = (TextView) findViewById(R.id.tvTytulPowiadomienia);
		tvGodzina = (TextView) findViewById(R.id.tvGodzinaPowiadomienia);
		tvCyklicznosc = (TextView) findViewById(R.id.tvPowtorzCo);
		tvOpis = (TextView) findViewById(R.id.tvOpisPowiadomienia);
		btnUsunZdarzenie = (Button) findViewById(R.id.btnUsunZdarzenie);

		btnUsunZdarzenie.setOnClickListener(WidokPowiadomieniaEkran.this);

		Bundle odbierz = getIntent().getExtras();
		String[] odebrane = odbierz.getStringArray("KONIEC");

		tvTytul.setText(odebrane[1]);
		tvOpis.setText(odebrane[3]);
		tvGodzina.setText(odebrane[4]);
		tvCyklicznosc.setText(odebrane[5]);

		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		int uniqueID = Integer.parseInt(odebrane[0]);
		id = uniqueID;
		uniqueID += 1234567;
		nm.cancel(uniqueID);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnUsunZdarzenie:
			Intent intent = new Intent(WidokPowiadomieniaEkran.this,
					KuracjaOdbiorcaKomunikatow.class);
			PendingIntent sender = PendingIntent.getBroadcast(
					WidokPowiadomieniaEkran.this, id, intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.cancel(sender);
			Toast.makeText(WidokPowiadomieniaEkran.this, "Usuniêto zdarzenie",
					Toast.LENGTH_LONG).show();

			break;
		}
	}
}
