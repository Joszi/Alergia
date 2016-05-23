package projekt.inzynierski;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class KuracjaOdbiorcaKomunikatow extends BroadcastReceiver {

	BazaDanychKuracja bdk;

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Odebra³em komunikat", Toast.LENGTH_LONG)
				.show();

		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Bundle odbierz = intent.getExtras();
		String[] odebrane = odbierz.getStringArray("BROADCAST");

		Bundle wys = new Bundle();
		wys.putStringArray("KONIEC", odebrane);

		int id = Integer.parseInt(odebrane[0]);

		Intent open = new Intent(context, WidokPowiadomieniaEkran.class);
		open.putExtras(wys);
		PendingIntent pi = PendingIntent.getActivity(context, id, open,
				PendingIntent.FLAG_CANCEL_CURRENT);

		Notification n = new Notification(R.drawable.ikona, odebrane[1],
				System.currentTimeMillis());
		n.setLatestEventInfo(context, odebrane[1], odebrane[3], pi);
		n.defaults = Notification.DEFAULT_ALL;

		id += 1234567;
		nm.notify(id, n);
	}
}
