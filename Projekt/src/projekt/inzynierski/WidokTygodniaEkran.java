package projekt.inzynierski;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WidokTygodniaEkran extends ListActivity {

	private String[] pobraneDni;
	private String[] pobraneDaty;
	private String[] dzienData;
	private String[] pobraneAlergeny;

	private String[][] pylenia;
	private ArrayAdapter<String> adapter;
	private boolean czyTak = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		BazaDanychAlergia info = new BazaDanychAlergia(this);
		info.open();
		String dni = info.getDays();
		String data = info.getDate();

		pobraneDni = dni.split(" +");
		pobraneDaty = data.split(" +");
		dzienData = new String[pobraneDni.length];

		for (int i = 0; i < pobraneDni.length; i++) {
			dzienData[i] = pobraneDni[i] + " " + pobraneDaty[i];
		}

		pobraneAlergeny = new String[pobraneDni.length];
		pylenia = new String[pobraneDni.length][pobraneDni.length];

		for (int i = 0; i < pobraneDni.length; i++) {
			pobraneAlergeny[i] = info.getAlergenWartosc(pobraneDaty[i]);
			pylenia[i] = pobraneAlergeny[i].split(" +");
		}
		info.close();

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				dzienData);
		setListAdapter(adapter);
		czyTak = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		czyTak = true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (czyTak == true) {
			Bundle wyslij = new Bundle();
			wyslij.putStringArray("key", pylenia[position]);
			Intent i = new Intent(WidokTygodniaEkran.this, WidokDniaEkran.class);
			i.putExtras(wyslij);
			startActivity(i);
		}
	}
}
