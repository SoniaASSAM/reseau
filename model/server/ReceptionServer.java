package model.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;


public class ReceptionServer implements Runnable {

	private BufferedReader in;
	private String message = null, login = null;

	public ReceptionServer(BufferedReader in, String login){

		this.in = Objects.requireNonNull(in);
		this.login = Objects.requireNonNull(login);
	}

	public void run() {
		
		PrintWriter out = null;

		while(true){
			try {
				message = in.readLine();
				String cmd = message.split(" ")[0];
				ArrayList<String> dest = new ArrayList<>();
				System.out.println(Server.clients.size());
				/**if (cmd.equals("NICK")) {
					String d = message.split("@")[1].split(" ")[0];
					dest.add(d);
				}
				if (message.contains("QUIT")) // quit
				if (message.contains("BROADCAST")) //broadcast
				if (message.contains("MULTICAST"))//multicast
				*/
				
				
				for(Socket user : Server.clients.values()) {
					out = new PrintWriter(new OutputStreamWriter(user.getOutputStream(), "UTF8"), true);
					out.println(login+":"+message);
					out.flush();
				}
				System.out.println(login+" : "+message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
