package projekt.inzynierski;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class WspomaganieKuracjiEkran extends Activity implements
		OnClickListener {

	private DatePicker dpData;
	private EditText etTytul;
	private EditText etOpis;
	private Button btnDodajZdarzenie;

	private BazaDanychKuracja bdk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kuracje);
		initialize();
	}

	public void initialize() {
		dpData = (DatePicker) findViewById(R.id.dpUstawDate);
		etTytul = (EditText) findViewById(R.id.etUstawTytul);
		etOpis = (EditText) findViewById(R.id.etUstawOpis);

		btnDodajZdarzenie = (Button) findViewById(R.id.btnDodajZdarzenie);
		btnDodajZdarzenie.setOnClickListener(WspomaganieKuracjiEkran.this);
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
			utworzWpis();
			Toast.makeText(WspomaganieKuracjiEkran.this,
					"Utworzono nowe zdarzenie", Toast.LENGTH_LONG).show();
			break;
		}
	}

	private void utworzWpis() {
		int dzien = dpData.getDayOfMonth();
		int miesiac = dpData.getMonth();
		miesiac = miesiac + 1;
		int rok = dpData.getYear();

		String day = "";
		String month = "";
		String year = "";

		if (dzien < 10) {
			day += "0";
			day += String.valueOf(dzien);
		} else {
			day += String.valueOf(dzien);
		}

		if (miesiac < 10) {
			month += "0";
			month += String.valueOf(miesiac);
		} else {
			month += String.valueOf(miesiac);
		}

		year = String.valueOf(rok);
		String data = day + "." + month + "." + year;

		String tytul = "";
		tytul = etTytul.getText().toString();
		tytul = tytul.replaceAll(" ", "_");

		String opis = "";
		opis = etOpis.getText().toString();
		opis = opis.replaceAll(" ", "_");

		bdk = new BazaDanychKuracja(WspomaganieKuracjiEkran.this);
		bdk.open();
		bdk.createEntry(data, tytul, opis);
		bdk.close();
	}
}
