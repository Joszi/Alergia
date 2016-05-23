package projekt.inzynierski;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class WidzetAlergia extends AppWidgetProvider {

	private BazaDanychAlergia bda;
	private SharedPreferences sp;

	private String odczytDaty;
	private boolean[] alergeny = new boolean[14];

	private String[] opcje = new String[] { "checkboxAlternaria",
			"checkboxBabka", "checkboxBrzoza", "checkboxBylica",
			"checkboxCladosporium", "checkboxDab", "checkboxKomosa",
			"checkboxLeszczyna", "checkboxOlsza", "checkboxPokrzywa",
			"checkboxSzczaw", "checkboxTopola", "checkboxTrawy",
			"checkboxWierzba" };
	private String[] nazwyAlergenow = new String[] { "Alternaria", "Babka",
			"Brzoza", "Bylica", "Cladosporium", "D¹b", "Komosa", "Leszczyna",
			"Olsza", "Pokrzywa", "Szczaw", "Topola", "Trawy", "Wierzba" };

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		odczytDaty = sdf.format(date);

		String dane = odczytajDane(context);
		String[] odczyt = dane.split(" +");
		String[] odczytAlergenow = new String[3];

		for (int i = 0; i < 3; i++) {
			odczytAlergenow[i] = odczyt[i];
		}

		sp = PreferenceManager.getDefaultSharedPreferences(context);
		boolean kolor = sp.getBoolean("checkboxKolor", false);

		String miasto = sp.getString("nameCity", "Brak");
		miasto += " " + odczytDaty.substring(0, odczytDaty.length() - 5);

		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int awID = appWidgetIds[i];
			RemoteViews v = new RemoteViews(context.getPackageName(),
					R.layout.widget);

			v.setTextViewText(R.id.tvMiejscowosc, miasto);
			if (kolor == true) {
				v.setTextColor(R.id.tvMiejscowosc, Color.BLACK);
			} else {
				v.setTextColor(R.id.tvMiejscowosc, Color.WHITE);
			}

			if (odczytAlergenow[0].contains("0")) {
				v.setImageViewResource(R.id.ivAlergen1, R.drawable.poziom_0_1);
			} else if (odczytAlergenow[0].contains("1")) {
				v.setImageViewResource(R.id.ivAlergen1, R.drawable.poziom_1_1);
			} else if (odczytAlergenow[0].contains("2")) {
				v.setImageViewResource(R.id.ivAlergen1, R.drawable.poziom_2_1);
			} else if (odczytAlergenow[0].contains("3")) {
				v.setImageViewResource(R.id.ivAlergen1, R.drawable.poziom_3_1);
			} else if (odczytAlergenow[0].contains("4")) {
				v.setImageViewResource(R.id.ivAlergen1, R.drawable.poziom_4_1);
			}

			v.setTextViewText(R.id.tvAlergen1, odczytAlergenow[0].substring(0,
					odczytAlergenow[0].length() - 1));
			if (kolor == true) {
				v.setTextColor(R.id.tvAlergen1, Color.BLACK);
			} else {
				v.setTextColor(R.id.tvAlergen1, Color.WHITE);
			}

			if (odczytAlergenow[1].contains("0")) {
				v.setImageViewResource(R.id.ivAlergen2, R.drawable.poziom_0_1);
			} else if (odczytAlergenow[1].contains("1")) {
				v.setImageViewResource(R.id.ivAlergen2, R.drawable.poziom_1_1);
			} else if (odczytAlergenow[1].contains("2")) {
				v.setImageViewResource(R.id.ivAlergen2, R.drawable.poziom_2_1);
			} else if (odczytAlergenow[1].contains("3")) {
				v.setImageViewResource(R.id.ivAlergen2, R.drawable.poziom_3_1);
			} else if (odczytAlergenow[1].contains("4")) {
				v.setImageViewResource(R.id.ivAlergen2, R.drawable.poziom_4_1);
			}

			v.setTextViewText(R.id.tvAlergen2, odczytAlergenow[1].substring(0,
					odczytAlergenow[1].length() - 1));
			if (kolor == true) {
				v.setTextColor(R.id.tvAlergen2, Color.BLACK);
			} else {
				v.setTextColor(R.id.tvAlergen2, Color.WHITE);
			}

			if (odczytAlergenow[2].contains("0")) {
				v.setImageViewResource(R.id.ivAlergen3, R.drawable.poziom_0_1);
			} else if (odczytAlergenow[2].contains("1")) {
				v.setImageViewResource(R.id.ivAlergen3, R.drawable.poziom_1_1);
			} else if (odczytAlergenow[2].contains("2")) {
				v.setImageViewResource(R.id.ivAlergen3, R.drawable.poziom_2_1);
			} else if (odczytAlergenow[2].contains("3")) {
				v.setImageViewResource(R.id.ivAlergen3, R.drawable.poziom_3_1);
			} else if (odczytAlergenow[2].contains("4")) {
				v.setImageViewResource(R.id.ivAlergen3, R.drawable.poziom_4_1);
			}

			v.setTextViewText(R.id.tvAlergen3, odczytAlergenow[2].substring(0,
					odczytAlergenow[2].length() - 1));
			if (kolor == true) {
				v.setTextColor(R.id.tvAlergen3, Color.BLACK);
			} else {
				v.setTextColor(R.id.tvAlergen3, Color.WHITE);
			}

			Intent launchAppIntent = new Intent(context, PoczatkowyEkran.class);
			PendingIntent launchAppPendingIntent = PendingIntent.getActivity(
					context, 0, launchAppIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			v.setOnClickPendingIntent(R.id.widget_view, launchAppPendingIntent);

			appWidgetManager.updateAppWidget(awID, v);
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	private String odczytajDane(Context context) {
		sp = PreferenceManager.getDefaultSharedPreferences(context);

		for (int i = 0; i < 14; i++) {
			alergeny[i] = sp.getBoolean(opcje[i], false);
		}

		bda = new BazaDanychAlergia(context);
		bda.open();
		String wynik = "";

		for (int i = 0; i < 14; i++) {
			if (alergeny[i] == true) {
				wynik += bda.getDateAlergen(odczytDaty, nazwyAlergenow[i]);
			}
		}
		bda.close();
		return wynik;
	}
}
