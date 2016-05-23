package projekt.inzynierski;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WidokZaplanowanejKuracjiEkran extends Activity implements
		OnClickListener {

	private TextView tvTytul;
	private TextView tvData;
	private TextView tvOpis;
	private Button btnUtworzZdarzenie;

	private BazaDanychKuracja bdk;
	String[] wyslij;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widokzaplanowanejkuracji);

		tvTytul = (TextView) findViewById(R.id.tvTytulKuracji);
		tvData = (TextView) findViewById(R.id.tvDataWprowadzenia);
		tvOpis = (TextView) findViewById(R.id.tvOpisKuracji);
		btnUtworzZdarzenie = (Button) findViewById(R.id.btnUtworzZdarzenie);
		btnUtworzZdarzenie
				.setOnClickListener(WidokZaplanowanejKuracjiEkran.this);

		Bundle odbierz = getIntent().getExtras();
		int odebrane = odbierz.getInt("KLUCZ");

		bdk = new BazaDanychKuracja(WidokZaplanowanejKuracjiEkran.this);
		bdk.open();
		String dane = bdk.getEntry(odebrane);
		bdk.close();

		String[] wynik = dane.split(" +");

		String klucz = wynik[0];
		String tytul = wynik[1];
		tytul = tytul.replaceAll("_", " ");
		String data = wynik[2];
		
		tvTytul.setText(tytul);
		tvData.setText(data);

		String opis = "";
		for (int i = 3; i < wynik.length; i++) {
			opis += wynik[i];
			opis += " ";
		}
		opis = opis.replaceAll("_", " ");
		tvOpis.setText(opis);

		wyslij = new String[4];
		wyslij[0] = klucz;
		wyslij[1] = tytul;
		wyslij[2] = data;
		wyslij[3] = opis;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnUtworzZdarzenie:

			Bundle wys = new Bundle();
			wys.putStringArray("DANE", wyslij);
			Intent i = new Intent(WidokZaplanowanejKuracjiEkran.this,
					WspomaganieKuracjiPowiadomienieEkran.class);
			i.putExtras(wys);
			startActivity(i);
			break;
		}
	}
}
