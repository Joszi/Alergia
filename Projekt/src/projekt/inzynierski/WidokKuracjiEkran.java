package projekt.inzynierski;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class WidokKuracjiEkran extends ListActivity implements OnClickListener {

	private Button btnDodaj;
	private CheckBox chkUsunDane;
	private String[] pobraneKuracje;
	private String[] pobraneTytuly;
	private ArrayAdapter<String> adapter;
	private BazaDanychKuracja info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widokkuracji);

		btnDodaj = (Button) findViewById(R.id.btnDodaj);
		btnDodaj.setOnClickListener(WidokKuracjiEkran.this);
		chkUsunDane = (CheckBox) findViewById(R.id.chkUsunWpis);

		odswiezListe();
	}

	@Override
	protected void onResume() {
		super.onResume();
		odswiezListe();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnDodaj:
			Intent i = new Intent("projekt.inzynierski.WSPOMAGANIEKURACJIEKRAN");
			startActivity(i);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		info = new BazaDanychKuracja(WidokKuracjiEkran.this);
		info.open();
		pobraneTytuly[position] = pobraneTytuly[position].replaceAll(" ", "_");
		String pozycja = info.getKey(pobraneTytuly[position]);
		info.close();

		final int poz = Integer.parseInt(pozycja);

		if (!chkUsunDane.isChecked()) {
			Bundle wyslij = new Bundle();
			wyslij.putInt("KLUCZ", poz);
			Intent i = new Intent(WidokKuracjiEkran.this,
					WidokZaplanowanejKuracjiEkran.class);
			i.putExtras(wyslij);
			startActivity(i);
		} else {
			AlertDialog alertDialog = new AlertDialog.Builder(
					WidokKuracjiEkran.this).create();
			alertDialog.setTitle("Usuñ wpis");
			alertDialog.setMessage("Czy jesteœ pewny?");
			alertDialog.setButton("Tak", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					info = new BazaDanychKuracja(WidokKuracjiEkran.this);
					info.open();
					info.deleteEntry(poz);
					info.close();

					odswiezListe();
				}
			});
			alertDialog.setButton2("Nie",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alertDialog.show();
		}
	}

	private void odswiezListe() {
		info = new BazaDanychKuracja(WidokKuracjiEkran.this);
		info.open();
		String dane = info.getDateTytul();
		info.close();

		String[] pobraneDane = dane.split(" +");
		pobraneTytuly = new String[pobraneDane.length];
		String[] pobraneDaty = new String[pobraneDane.length];
		pobraneKuracje = new String[pobraneDane.length];

		if (pobraneDane[0].length() > 1) {
			for (int i = 0; i < pobraneDane.length; i++) {
				pobraneDaty[i] = pobraneDane[i].substring(0, 10);
				pobraneTytuly[i] = pobraneDane[i].substring(10,
						pobraneDane[i].length());
				pobraneTytuly[i] = pobraneTytuly[i].replaceAll("_", " ");
			}

			for (int i = 0; i < pobraneDane.length; i++) {
				pobraneKuracje[i] = pobraneTytuly[i] + " " + pobraneDaty[i];
			}

			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					pobraneKuracje);
			setListAdapter(adapter);
		}
	}
}
