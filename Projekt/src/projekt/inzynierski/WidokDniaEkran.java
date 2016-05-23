package projekt.inzynierski;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class WidokDniaEkran extends ListActivity {

	private String[] pylenie;
	private String[] alergen;
	private BazaDanychAlergia info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle odbierz = getIntent().getExtras();
		pylenie = odbierz.getStringArray("key");

		WidokDniaAdapter ada = new WidokDniaAdapter(this, pylenie);
		setListAdapter(ada);

		info = new BazaDanychAlergia(this);
		info.open();
		String wynik = info.getAlergen();
		info.close();

		alergen = wynik.split(" +");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Bundle wyslij = new Bundle();
		wyslij.putString("KEY", alergen[position]);
		Intent i = new Intent(WidokDniaEkran.this, WidokAlergenuEkran.class);
		i.putExtras(wyslij);
		startActivity(i);
	}
}
