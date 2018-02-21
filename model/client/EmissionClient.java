package model.client;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import model.server.Server;


public class EmissionClient implements Runnable {

	private PrintWriter out;
	private String login = null, message = null;
	private Scanner sc = null;

	public EmissionClient(PrintWriter out) {
		this.out = Objects.requireNonNull(out);

	}


	public void run() {

		sc = new Scanner(System.in);

		while(true){
			System.out.println("Votre message :");
			message = sc.nextLine();
			ArrayList<String> dest = new ArrayList<>();
			System.out.println(Server.users.size());
			for (String user : Server.clients.keySet())
			{
				System.out.println("user "+user);
				for (String msg : Arrays.asList(message.split(" "))) {
					System.out.println(msg);
					if (msg.contains("@"+user)) dest.add(user);
				}
			}
			System.out.println(dest);
			String cmd = "";
			if (dest.size() == 1) cmd = "NICK";
			else if (dest.size() > 1) cmd = "MULTICAST";
			else cmd = "BROADCAST";
			out.println(cmd+" "+message);
			out.flush();
		}
	}
}
