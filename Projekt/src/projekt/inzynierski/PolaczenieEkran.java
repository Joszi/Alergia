package projekt.inzynierski;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class PolaczenieEkran extends Activity implements OnClickListener {

	private TextView tvMiejsce;
	private Button btnPobierzDane;
	private Button btnPokazPylenie;
	private Button btnPrzebiegLeczenia;
	private Button btnSynchronizujDane;
	private Button btnUstawienia;
	private ToggleButton tbtnStanWifi;

	private ProgressDialog pdProgressDialogData;
	private WifiManager wmWifiManager;
	private TelephonyManager tmTelephonyManager;
	private GsmCellLocation gclGsmCellLocation;

	private StacjaBazowa ociOpenCellID;
	private String latitude;
	private String longitude;
	private String wynik = null;
	private String miasto;
	private String wojewodztwo;
	private String[] odczyt;

	private static final String KOMENDA = "POBIERZ ";
	private BazaDanychAlergia database;

	private String[] nazwyAlergenow;
	private String[] opisyAlergenow;
	public PobierzDane pDane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ekranglowny);
		initialize();
	}

	public void initialize() {
		tvMiejsce = (TextView) findViewById(R.id.tvMiejsce);
		btnPobierzDane = (Button) findViewById(R.id.btnPobierzDane);
		btnPokazPylenie = (Button) findViewById(R.id.btnPokazPylenie);
		btnPrzebiegLeczenia = (Button) findViewById(R.id.btnPrzebiegLeczenia);
		btnSynchronizujDane = (Button) findViewById(R.id.btnSynchronizuj);
		btnUstawienia = (Button) findViewById(R.id.btnUstawienia);
		tbtnStanWifi = (ToggleButton) findViewById(R.id.tbtnStanWifi);

		btnPobierzDane.setOnClickListener(PolaczenieEkran.this);
		btnPokazPylenie.setOnClickListener(PolaczenieEkran.this);
		btnPrzebiegLeczenia.setOnClickListener(PolaczenieEkran.this);
		btnSynchronizujDane.setOnClickListener(PolaczenieEkran.this);
		btnUstawienia.setOnClickListener(PolaczenieEkran.this);
		tbtnStanWifi.setOnClickListener(PolaczenieEkran.this);

		wmWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		tmTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		if (wmWifiManager.isWifiEnabled()) {
			tbtnStanWifi.setChecked(true);
		} else {
			tbtnStanWifi.setChecked(false);
		}

		nazwyAlergenow = new String[] { "Alternaria", "Babka", "Brzoza",
				"Bylica", "Cladosporium", "D¹b", "Komosa", "Leszczyna",
				"Olsza", "Pokrzywa", "Szczaw", "Topola", "Trawy", "Wierzba" };
		opisyAlergenow = new String[] {
				"Pocz¹tek pylenia: \npocz¹tek kwietnia\n\nKoniec pylenia: \npocz¹tek paŸdziernika\n\nNasilenie: \nVI - IX",
				"Pocz¹tek pylenia: \nmaj\n\nKoniec pylenia: \npo³owa wrzeœnia\n\nNasilenie: \nsta³e",
				"Pocz¹tek pylenia: \nkwiecieñ\n\nKoniec pylenia: \npo³owa maja\n\nNasilenie: \nIV - V",
				"Pocz¹tek pylenia: \npo³owa lipca\n\nKoniec pylenia: \nkoniec wrzeœnia\n\nNasilenie: \nVII - VIII",
				"Pocz¹tek pylenia: \npocz¹tek kwietnia\n\nKoniec pylenia: \npocz¹tek paŸdziernika\n\nNasilenie: \nVI - IX",
				"Pocz¹tek pylenia: \npo³owa kwietnia\n\nKoniec pylenia: \nkoniec maja\n\nNasilenie: \nIV - V",
				"Pocz¹tek pylenia: \nkoniec czerwca\n\nKoniec pylenia: \npo³owa wrzeœnia\n\nNasilenie: \nsta³e",
				"Pocz¹tek pylenia: \nkoniec stycznia / pocz¹tek lutego\n\nKoniec pylenia: \npo³owa kwietnia\n\nNasilenie: \nsta³e",
				"Pocz¹tek pylenia: \npo³owa lutego\n\nKoniec pylenia: \npo³owa kwietnia\n\nNasilenie: \nIII",
				"Pocz¹tek pylenia: \nmaj\n\nKoniec pylenia: \nkoniec wrzeœnia\n\nNasilenie: \nVI - VIII",
				"Pocz¹tek pylenia: \nmaj\n\nKoniec pylenia: \nkoniec sierpnia\n\nNasilenie: \nsta³e",
				"Pocz¹tek pylenia: \nmarzec\n\nKoniec pylenia: \nkwiecieñ\n\nNasilenie: \nIV",
				"Pocz¹tek pylenia: \nmaj\n\nKoniec pylenia: \npo³owa wrzeœnia\n\nNasilenie: \nVI - VII",
				"Pocz¹tek pylenia: \nkoniec lutego\n\nKoniec pylenia: \npo³owa maja\n\nNasilenie: \nIII - IV" };

		BazaDanychAlergen bda = new BazaDanychAlergen(PolaczenieEkran.this);
		bda.open();
		bda.deleteAllEntries();

		for (int i = 0; i < nazwyAlergenow.length; i++) {
			bda.createEntry(nazwyAlergenow[i], opisyAlergenow[i]);
		}
		bda.close();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (wynik == null) {
			tvMiejsce.append("\n");
			tvMiejsce.append("\n");
			tvMiejsce.append("\n");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (wynik == null) {
			tvMiejsce.setText("");
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tbtnStanWifi:
			if (tbtnStanWifi.isChecked()) {
				wmWifiManager.setWifiEnabled(true);
			} else {
				wmWifiManager.setWifiEnabled(false);
			}
			break;

		case R.id.btnPobierzDane:
			if (wmWifiManager.isWifiEnabled()) {
				pdProgressDialogData = ProgressDialog.show(
						PolaczenieEkran.this, "Proszê czekaæ",
						"trwa pobieranie danych z serwera...", true, false);
				new PokazPozycje().execute();
			} else {
				Toast.makeText(PolaczenieEkran.this, "W³¹cz Wi-Fi!",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.btnPokazPylenie:
			Intent openPylenieActivity = new Intent(
					"projekt.inzynierski.WIDOKTYGODNIAEKRAN");
			startActivity(openPylenieActivity);
			break;

		case R.id.btnPrzebiegLeczenia:
			Intent openKuracjeActivity = new Intent(
					"projekt.inzynierski.WIDOKKURACJIEKRAN");
			startActivity(openKuracjeActivity);
			break;

		case R.id.btnSynchronizuj:
			if (wmWifiManager.isWifiEnabled()) {
				if (SynchronizacjaEkran.koniec == true) {
					Intent openSynchronizujActivity = new Intent(
							"projekt.inzynierski.SYNCHRONIZACJAEKRAN");
					startActivity(openSynchronizujActivity);
				} else {
					Toast.makeText(PolaczenieEkran.this, "Poczekaj chwilê!",
							Toast.LENGTH_LONG).show();
					break;
				}
			} else {
				Toast.makeText(PolaczenieEkran.this, "W³¹cz Wi-Fi!",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.btnUstawienia:
			Intent openUstawieniaPreferencje = new Intent(
					"projekt.inzynierski.USTAWIENIA");
			startActivity(openUstawieniaPreferencje);
			break;
		}
	}

	public void getPosition(TelephonyManager tm, GsmCellLocation gcl,
			StacjaBazowa oci) {
		String networkOperator = tm.getNetworkOperator();
		String mcc = networkOperator.substring(0, 3);
		String mnc = networkOperator.substring(3);

		int cid = gcl.getCid();
		int lac = gcl.getLac();

		oci.setMobileCountryCode(mcc);
		oci.setMobileNetworkCode(mnc);
		oci.setCellID(cid);
		oci.setLocationAreaCode(lac);

		try {
			oci.getOpenCellID();

			if (!oci.isError()) {
				latitude = oci.getLatitude();
				longitude = oci.getLongitude();
			} else {
				latitude = null;
				longitude = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getAddress() {
		String lok = null;
		String lok2 = null;
		try {
			double lat = Double.parseDouble(latitude);
			double lon = Double.parseDouble(longitude);

			Geocoder gcd = new Geocoder(this, Locale.getDefault());
			List<Address> addresses = gcd.getFromLocation(lat, lon, 1);
			if (addresses.size() > 0) {

				for (int i = 0; i < addresses.size(); i++) {
					Address address = addresses.get(i);

					if ((address.getLocality() != null)) {
						SharedPreferences sp = PreferenceManager
								.getDefaultSharedPreferences(PolaczenieEkran.this);
						SharedPreferences.Editor prefEditor = sp.edit();
						prefEditor.putString("nameCity", address.getLocality());
						prefEditor.commit();

						lok = address.getLocality();

						if (lok.contains(" ")) {
							lok = lok.replaceAll(" ", "-");
						}
					} else {
						lok = "Wroc³aw";

						SharedPreferences sp = PreferenceManager
								.getDefaultSharedPreferences(PolaczenieEkran.this);
						SharedPreferences.Editor prefEditor = sp.edit();
						prefEditor.putString("nameCity", lok);
						prefEditor.commit();

						if (lok.contains(" ")) {
							lok = lok.replaceAll(" ", "-");
						}

						lok += " ";
						lok2 = "dolnoœl¹skie";

						if (lok2.contains(" ")) {
							lok2 = lok2.replaceAll(" ", "-");
						}
						lok += lok2;
						return lok;
					}

					lok += " ";
					lok2 = address.getAdminArea();

					if (lok2.contains(" ")) {
						lok2 = lok2.replaceAll(" ", "-");
					}
					lok += lok2;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lok;
	}

	public class PobierzDane extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			SharedPreferences getPrefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			String serwerIP = getPrefs.getString("serwerProxyIP",
					"176.31.202.50");
			String port = getPrefs.getString("serwerProxyPort", "5000");
			int serwerPort = Integer.parseInt(port);

			String response = null;

			if (serwerIP == null || serwerIP == "" || port == null
					|| port == "") {
				serwerIP = "176.31.202.50";
				serwerPort = 5000;
			}
			try {
				Socket socket = new Socket(serwerIP, serwerPort);
				ObjectOutputStream output = new ObjectOutputStream(
						socket.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(
						socket.getInputStream());

				String zapytanie = KOMENDA + miasto + " " + wojewodztwo;
				output.writeObject(zapytanie);
				Thread.sleep(1000);
				response = (String) input.readObject();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			odczyt = response.split(" +");
			database = new BazaDanychAlergia(PolaczenieEkran.this);
			database.open();
			database.deleteAllEntries();

			for (int i = 1; i < odczyt.length - 1; i += 2) {
				if (i > 4 && i < 33) {
					database.createEntry(odczyt[1].toString(),
							odczyt[2].toString(), odczyt[i].toString(),
							odczyt[i + 1].toString());
				}
				if (i > 36 && i < 65) {
					database.createEntry(odczyt[33].toString(),
							odczyt[34].toString(), odczyt[i].toString(),
							odczyt[i + 1].toString());
				}
				if (i > 68 && i < 97) {
					database.createEntry(odczyt[65].toString(),
							odczyt[66].toString(), odczyt[i].toString(),
							odczyt[i + 1].toString());
				}
				if (i > 100 && i < 129) {
					database.createEntry(odczyt[97].toString(),
							odczyt[98].toString(), odczyt[i].toString(),
							odczyt[i + 1].toString());
				}
				if (i > 132 && i < 161) {
					database.createEntry(odczyt[129].toString(),
							odczyt[130].toString(), odczyt[i].toString(),
							odczyt[i + 1].toString());
				}
				if (i > 164 && i < 193) {
					database.createEntry(odczyt[161].toString(),
							odczyt[162].toString(), odczyt[i].toString(),
							odczyt[i + 1].toString());
				}
				if (i > 196 && i < 225) {
					database.createEntry(odczyt[193].toString(),
							odczyt[194].toString(), odczyt[i].toString(),
							odczyt[i + 1].toString());
				}
			}
			database.close();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pdProgressDialogData.dismiss();
		}
	}

	public class PokazPozycje extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {

			gclGsmCellLocation = (GsmCellLocation) tmTelephonyManager
					.getCellLocation();
			ociOpenCellID = new StacjaBazowa();
			getPosition(tmTelephonyManager, gclGsmCellLocation, ociOpenCellID);

			String pozycja = getAddress();
			return pozycja;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			String[] podzial = result.split(" +");
			tvMiejsce.setText("");
			tvMiejsce.append("\n");
			tvMiejsce.append(podzial[0]);
			tvMiejsce.append("\n");
			tvMiejsce.append(podzial[1]);
			tvMiejsce.append("\n");

			miasto = podzial[0];
			wojewodztwo = podzial[1];
			wynik = result;

			pDane = new PobierzDane();
			pDane.execute();
		}
	}
}
