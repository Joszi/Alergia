package projekt.inzynierski;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SynchronizacjaEkran extends Activity implements OnClickListener,
		Runnable {

	private TextView tvAdresIP;
	private Button btnPobierzDane;
	private EditText etWpiszAdresIP;
	private BazaDanychKuracja bdk;

	private static final int SERWERPORT = 5001;
	private String host;
	private String adres;
	private Socket socket;
	private ServerSocket serverSocket;

	private WifiManager wifiManager;
	private WifiInfo wifiInfo;
	public static boolean koniec = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.synchronizacjaekran);

		tvAdresIP = (TextView) findViewById(R.id.tvAdresIP);
		etWpiszAdresIP = (EditText) findViewById(R.id.etWpiszIP);
		btnPobierzDane = (Button) findViewById(R.id.btnPobierzDane);
		btnPobierzDane.setOnClickListener(SynchronizacjaEkran.this);

		wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		wifiInfo = wifiManager.getConnectionInfo();

		int ipAddress = wifiInfo.getIpAddress();
		host = intToIp(ipAddress);
		tvAdresIP.setText(host);
	}

	@Override
	protected void onResume() {
		super.onResume();

		koniec = false;
		Thread watek = new Thread(SynchronizacjaEkran.this);
		watek.start();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnPobierzDane:
			adres = etWpiszAdresIP.getText().toString();
			new SynchronizacjaWywolaj(adres, SERWERPORT);
			Toast.makeText(SynchronizacjaEkran.this, "Pobrano dane",
					Toast.LENGTH_LONG).show();
			break;
		}
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(SERWERPORT);
			serverSocket.setSoTimeout(60000);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				socket = serverSocket.accept();

				if (socket != null) {
					bdk = new BazaDanychKuracja(SynchronizacjaEkran.this);
					bdk.open();
					String dane = bdk.getData();
					bdk.close();

					new SynchronizacjaOdbierz(socket, dane);
				}

			} catch (InterruptedIOException e) {
				try {
					serverSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		koniec = true;
	}

	public String intToIp(int i) {

		return ((i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 24) & 0xFF));
	}

	private class SynchronizacjaWywolaj implements Runnable {

		private String adresSerwera;
		private final int portSerwera;
		private String response;

		SynchronizacjaWywolaj(String adresIP, int port) {
			adresSerwera = adresIP;
			portSerwera = port;

			Thread t = new Thread(SynchronizacjaWywolaj.this);
			t.start();
		}

		@Override
		public void run() {

			try {
				Socket socket = new Socket(adresSerwera, portSerwera);
				ObjectOutputStream output = new ObjectOutputStream(
						socket.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(
						socket.getInputStream());

				output.writeObject("POBIERZ DANE");
				Thread.sleep(1000);
				response = (String) input.readObject();

				String[] data = response.split(" +");

				bdk = new BazaDanychKuracja(SynchronizacjaEkran.this);
				bdk.open();
				bdk.deleteAllEntries();
				for (int i = 0; i < data.length; i += 3) {
					bdk.createEntry(data[i], data[i + 1], data[i + 2]);
				}
				bdk.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private class SynchronizacjaOdbierz implements Runnable {

		private Socket sock;
		private String line;
		private String response;
		private String[] command;

		SynchronizacjaOdbierz(Socket s, String dane) {
			sock = s;
			response = dane;
			Thread tr = new Thread(SynchronizacjaOdbierz.this);
			tr.start();
		}

		@Override
		public void run() {
			ObjectInputStream in;
			ObjectOutputStream out;
			try {
				in = new ObjectInputStream(sock.getInputStream());
				out = new ObjectOutputStream(sock.getOutputStream());
	
				while ((line = (String) in.readObject()) != null) {
					command = line.split(" +");

					if (command[0].equals("POBIERZ")) {

						out.writeObject(response);
						out.flush();

						in.close();
						out.close();

						sock.close();
						sock = null;

						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
