import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class SerwerProxy implements Runnable {

	private static final int SERWERPORT = 5000;
	private Socket socket;
	private ServerSocket serverSocket;

	private String host;
	private int socketPort;

	SerwerProxy() {
		Thread watek = new Thread(SerwerProxy.this);
		watek.start();
	}

	@Override
	public void run() {
		try {
			host = InetAddress.getLocalHost().getHostAddress() + ":"
					+ SERWERPORT + " ("
					+ InetAddress.getLocalHost().getHostName() + ") ";
			serverSocket = new ServerSocket(SERWERPORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Serwer PROXY zostal uruchomiony na hoscie " + host);

		while (true) {
			try {
				socket = serverSocket.accept();

				if (socket != null) {
					new Urzadzenie(socket);
					socketPort = socket.getPort();
					System.out
							.println("Nowe urzadzenie polaczylo sie z serwerem na porcie "
									+ socketPort);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new SerwerProxy();
	}
}

class Urzadzenie implements Runnable {

	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	private static final String BASEURL = "http://www.twojapogoda.pl/tematyczne/dla-alergikow/polska/";
	private String line;
	private String response;
	private String[] command;

	Urzadzenie(Socket s) {
		socket = s;
		Thread t = new Thread(Urzadzenie.this);
		t.start();
	}

	@Override
	public void run() {
		try {
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());

			while ((line = (String) input.readObject()) != null) {
				command = line.split(" +");

				if (command[0].equals("POBIERZ")) {

					command[1] = changeSigns(command[1]);
					command[2] = changeSigns(command[2]);

					command[1] = command[1].toLowerCase();
					command[2] = command[2].toLowerCase();

					String pom = command[0];
					pom += " ";
					pom += command[1];
					pom += " ";
					pom += command[2];

					System.out.println(pom);

					response = readData(command[1], command[2]);
					response = formatData(response);

					output.writeObject(response);
					output.flush();

					System.out.println("POBIERZ OK");

					input.close();
					output.close();

					socket.close();
					socket = null;

					System.out.println("KONIEC");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String changeSigns(String result) {
		if (result.contains("π")) {
			result = result.replaceAll("π", "a");
		}
		if (result.contains("Ê")) {
			result = result.replaceAll("Ê", "c");
		}
		if (result.contains("Í")) {
			result = result.replaceAll("Í", "e");
		}
		if (result.contains("≥")) {
			result = result.replaceAll("≥", "l");
		}
		if (result.contains("Ò")) {
			result = result.replaceAll("Ò", "n");
		}
		if (result.contains("Û")) {
			result = result.replaceAll("Û", "o");
		}
		if (result.contains("ú")) {
			result = result.replaceAll("ú", "s");
		}
		if (result.contains("ü")) {
			result = result.replaceAll("ü", "z");
		}
		if (result.contains("ø")) {
			result = result.replaceAll("ø", "z");
		}
		if (result.contains("•")) {
			result = result.replaceAll("•", "a");
		}
		if (result.contains("∆")) {
			result = result.replaceAll("∆", "c");
		}
		if (result.contains(" ")) {
			result = result.replaceAll(" ", "e");
		}
		if (result.contains("£")) {
			result = result.replaceAll("£", "l");
		}
		if (result.contains("—")) {
			result = result.replaceAll("—", "n");
		}
		if (result.contains("”")) {
			result = result.replaceAll("”", "o");
		}
		if (result.contains("å")) {
			result = result.replaceAll("å", "s");
		}
		if (result.contains("è")) {
			result = result.replaceAll("è", "z");
		}
		if (result.contains("Ø")) {
			result = result.replaceAll("Ø", "z");
		}

		return result;
	}

	private String readData(String city, String state) {
		String url = BASEURL + state + "/" + city;
		String pom = null;

		try {
			Document doc = Jsoup.connect(url).get();
			Elements ele = doc.select("[class=days]");

			pom = ele.html();
			pom = pom.replaceAll("<div class=\"value v0\"></div>",
					"<div class=\"value v0\">0</div>");
			pom = pom.replaceAll("<div class=\"value v1\"></div>",
					"<div class=\"value v1\">1</div>");
			pom = pom.replaceAll("<div class=\"value v2\"></div>",
					"<div class=\"value v2\">2</div>");
			pom = pom.replaceAll("<div class=\"value v3\"></div>",
					"<div class=\"value v3\">3</div>");
			pom = pom.replaceAll("<div class=\"value v4\"></div>",
					"<div class=\"value v4\">4</div>");

			Document dom = Jsoup.parse(pom);
			pom = dom.text();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return pom;
	}

	private String formatData(String result) {
		String response = "";
		String[] command;
		command = result.split(" +");

		for (int i = 2; i < 227; i++) {
			response += command[i];
			response += " ";
		}

		return response;
	}
}
