package projekt.inzynierski;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class WidokAlergenuEkran extends Activity {

	private TextView tvRodzajAlergenu;
	private TextView tvOpisAlergenu;
	private ImageView ivAlergen;
	private String opis;

	private BazaDanychAlergen bda;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widokalergenuekran);

		tvRodzajAlergenu = (TextView) findViewById(R.id.tvRodzajAlergenu);
		tvOpisAlergenu = (TextView) findViewById(R.id.tvOpisAlergenu);
		ivAlergen = (ImageView) findViewById(R.id.ivAlergen);

		Bundle odbierz = getIntent().getExtras();
		String odebrane = odbierz.getString("KEY");

		bda = new BazaDanychAlergen(WidokAlergenuEkran.this);
		bda.open();
		opis = bda.getOpis(odebrane);
		bda.close();

		tvRodzajAlergenu.setText(odebrane);
		tvOpisAlergenu.setText(opis);

		if (odebrane.equals("Alternaria")) {
			ivAlergen.setImageResource(R.drawable.alternaria);
		} else if (odebrane.equals("Babka")) {
			ivAlergen.setImageResource(R.drawable.babka);
		} else if (odebrane.equals("Brzoza")) {
			ivAlergen.setImageResource(R.drawable.brzoza);
		} else if (odebrane.equals("Bylica")) {
			ivAlergen.setImageResource(R.drawable.bylica);
		} else if (odebrane.equals("Cladosporium")) {
			ivAlergen.setImageResource(R.drawable.cladosporium);
		} else if (odebrane.equals("D¹b")) {
			ivAlergen.setImageResource(R.drawable.dab);
		} else if (odebrane.equals("Komosa")) {
			ivAlergen.setImageResource(R.drawable.komosa);
		} else if (odebrane.equals("Leszczyna")) {
			ivAlergen.setImageResource(R.drawable.leszczyna);
		} else if (odebrane.equals("Olsza")) {
			ivAlergen.setImageResource(R.drawable.olsza);
		} else if (odebrane.equals("Pokrzywa")) {
			ivAlergen.setImageResource(R.drawable.pokrzywa);
		} else if (odebrane.equals("Szczaw")) {
			ivAlergen.setImageResource(R.drawable.szczaw);
		} else if (odebrane.equals("Topola")) {
			ivAlergen.setImageResource(R.drawable.topola);
		} else if (odebrane.equals("Trawy")) {
			ivAlergen.setImageResource(R.drawable.trawy);
		} else if (odebrane.equals("Wierzba")) {
			ivAlergen.setImageResource(R.drawable.wierzba);
		}
	}
}
