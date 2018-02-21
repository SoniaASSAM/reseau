package model.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server {
	public static ServerSocket ss = null;
	public static Thread t;
	public final static Map<String,String> users = new HashMap<>();
	public final static Map <String,Socket> clients = new HashMap<>();


	public static void main(String[] args) {

		try {
			ss = new ServerSocket(1995);
			System.out.println("Le serveur est à l'écoute du port "+ss.getLocalPort());
			initUsersMap();
			t = new Thread(new AcceptConnexion(ss));
			t.start();

		} catch (IOException e) {
			System.err.println("Le port "+ss.getLocalPort()+" est déjà utilisé !");
		}
	}

	private static void initUsersMap() {
		String fileName = "users";

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String line;
			String [] ids;
			while ((line = br.readLine()) != null) {
				ids = line.split(" ");
				users.put(ids[0], ids[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
