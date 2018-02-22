package model.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Server {
	public static ServerSocket ss = null;
	public static Thread t;
	private final static Map<String,String> users = initUsersMap();
	private final static Map <String,Socket> connections = new HashMap<>();


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

	private static synchronized Map<String,String> initUsersMap() {
		String fileName = "users";
		
		Map<String, String> users = new HashMap<>();

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
		return users;
	}
	
	public synchronized static void addConnection(String login, Socket client) {
		Objects.requireNonNull(login);
		Objects.requireNonNull(client);
		connections.put(login, client);
	}
	
	public synchronized static Set<String> getConnectedUsers() {
		return connections.keySet();
	}
	
	public synchronized static Set<String> getUsers() {
		return users.keySet();
	}
	
	public synchronized static Socket getClient(String login) {
		Objects.requireNonNull(login);
		return connections.get(login);
	}
	
	public synchronized static boolean isValidUser(String login, String pass) {
		System.out.println(getUsers());
		Objects.requireNonNull(pass);
		Objects.requireNonNull(login);
		return (pass.equals(users.get(login)));
	}
	
	public synchronized static Set<Socket> getClients () {
		return (Set<Socket>) connections.values();
	}
}
