package projekt.inzynierski;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class WspomaganieKuracjiPowiadomienieEkran extends Activity implements
		OnClickListener, OnItemSelectedListener {

	private TimePicker tpCzas;
	private Spinner spPowtorz;
	private Button btnDodajZdarzenie;

	private String powtorz = "24 godziny";
	private final String[] jakCzesto = new String[] { "co 1 minutê",
			"co 6 godzin", "co 12 godzin", "co 24 godziny", "co 1 tydzieñ",
			"co 2 tygodnie", "co 4 tygodnie" };
	private int licznik = 86400;

	private long czasPoczatek;
	private long czasKoniec;
	private String[] odebrane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widokpowiadomienia);

		tpCzas = (TimePicker) findViewById(R.id.tpUstawTermin);
		spPowtorz = (Spinner) findViewById(R.id.spUstawPowtarzanie);
		btnDodajZdarzenie = (Button) findViewById(R.id.btnDodajZdarzenie);
		btnDodajZdarzenie
				.setOnClickListener(WspomaganieKuracjiPowiadomienieEkran.this);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				WspomaganieKuracjiPowiadomienieEkran.this,
				android.R.layout.simple_spinner_item, jakCzesto);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spPowtorz.setAdapter(adapter);
		spPowtorz
				.setOnItemSelectedListener(WspomaganieKuracjiPowiadomienieEkran.this);

		int godzina = tpCzas.getCurrentHour();
		int minuta = tpCzas.getCurrentMinute();
		czasPoczatek = (godzina * 3600 + minuta * 60);

		Bundle odbierz = getIntent().getExtras();
		odebrane = odbierz.getStringArray("DANE");
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnDodajZdarzenie:

			int godzina = tpCzas.getCurrentHour();
			int minuta = tpCzas.getCurrentMinute();
			czasKoniec = (godzina * 3600 + minuta * 60);

			long czas = czasKoniec - czasPoczatek;

			long firstTime = SystemClock.elapsedRealtime();
			firstTime += czas * 1000;

			String[] przesyl = new String[6];
			przesyl[0] = odebrane[0];
			przesyl[1] = odebrane[1];
			przesyl[2] = odebrane[2];
			przesyl[3] = odebrane[3];

			String czasPrzeslij = String.valueOf(godzina) + ":"
					+ String.valueOf(minuta);
			przesyl[4] = String.valueOf(czasPrzeslij);
			przesyl[5] = powtorz;

			Bundle wys = new Bundle();
			wys.putStringArray("BROADCAST", przesyl);

			Intent intent = new Intent(
					WspomaganieKuracjiPowiadomienieEkran.this,
					KuracjaOdbiorcaKomunikatow.class);
			intent.putExtras(wys);

			int id = Integer.parseInt(przesyl[0]);
			PendingIntent sender = PendingIntent.getBroadcast(
					WspomaganieKuracjiPowiadomienieEkran.this, id, intent,
					PendingIntent.FLAG_CANCEL_CURRENT);

			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
					licznik * 1000, sender);

			Toast.makeText(WspomaganieKuracjiPowiadomienieEkran.this,
					"Utworzono nowe zdarzenie", Toast.LENGTH_LONG).show();
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		int index = arg0.getSelectedItemPosition();

		if (index == 0) {
			licznik = 60;
			powtorz = "1 minuta";
		} else if (index == 1) {
			licznik = 21600;
			powtorz = "6 godzin";
		} else if (index == 2) {
			licznik = 43200;
			powtorz = "12 godzin";
		} else if (index == 3) {
			licznik = 86400;
			powtorz = "24 godziny";
		} else if (index == 4) {
			licznik = 604800;
			powtorz = "1 tydzieñ";
		} else if (index == 5) {
			licznik = 1209600;
			powtorz = "2 tygodnie";
		} else {
			licznik = 2419200;
			powtorz = "4 tygodnie";
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		licznik = 86400;
		powtorz = "24 godziny";
	}
}
