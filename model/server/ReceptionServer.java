package model.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class ReceptionServer implements Runnable {

	private BufferedReader in;
	private String message = null, login = null;

	public ReceptionServer(BufferedReader in, String login){

		this.in = Objects.requireNonNull(in);
		this.login = Objects.requireNonNull(login);
	}

	public void run() {

		PrintWriter out = null;
		boolean stop = false;

		while(true){
			try {
				message = in.readLine();
				if (message.equals("QUIT")) // le déconnecter
				{
					Server.getClient(login).close();
					System.out.println(login+" a quitté la conversation");
				}
				else {
					String [] received = message.split("__");
					String cmd = received[0];
					message = received[1];
					Set<String> dest = new HashSet<>();
					if (cmd.equals("NICK")) {
						String[] msgs = message.split(" ");
						for (String d : msgs) {
							if (d.startsWith("@")) {
								dest.add(d.substring(1));
								break;
							}
						}
					}
					else if (cmd.equals("BROADCAST")) 
					{ 
						dest = Server.getConnectedUsers();//broadcast
					}
					else if (cmd.contains("MULTICAST")) {
						//multicast 
						String [] msgs = message.split(" ");
						for (String d : msgs) {
							if (d.startsWith("@")) dest.add(d.substring(1));
						}
					}
					Socket client = null;
					if (!stop) {
						for(String user : dest) {
							client = Server.getClient(user);
							if (client != null) {
								out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "UTF8"), true);
								out.println(login+":"+message);
								out.flush();
							}
							//sinon Ajouter le msg à la file d'attente
						}
						System.out.println(login+" : "+message);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}