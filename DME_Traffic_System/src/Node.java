/*
 * Student ID - 2940038
 */

import java.net.*;
import java.io.*;
import java.util.*;

/*
 * The Node class is responsible to carry out the critical section by requesting for token and returning it back to the coordinator.
 * 
 * The two traffic light nodes will request for the token by sending the ip and the port number to the coordinator and
 * when they receive the token the critical section is executed where the green light is turned on and off and then the token is returned back to the coordinator.
 */
public class Node {

	private Random ra; // Random instance to get a random number for the sleep time
	private Socket s;

	boolean implemented = false; // Boolean implemented to ensure that the code has been implemented on the node
									// or not

	private PrintWriter pout = null;

	private ServerSocket n_ss;
	private Socket n_token;

	String c_host = "127.0.0.1"; // The host ip
	int c_request_port = 7000; // The port of the coordinator
	int c_return_port = 7001; // The port of the mutex object

	// The host ip, name and the port number of the node
	String n_host = "127.0.0.1";
	String n_host_name;
	int n_port;

	/*
	 * The Constructor for the Node class
	 * 
	 * @param nam - host name of the system
	 * 
	 * @param por - port number of the node
	 * 
	 * @param sec - sleep time between each request
	 */
	public Node(String nam, int por, int sec) {

		ra = new Random();
		n_host_name = nam;
		n_port = por;

		System.out.println("Node " + n_host_name + ":" + n_port + " of DME is active .... \n");

		// NODE, i.e. the program for the traffic light sends n_host and n_port
		// through a socket s to the coordinator
		// c_host:c_req_port
		// and immediately opens a server socket through which it will receive
		// a TOKEN (actually just a synchronization).

		while (true) {

			// >>> sleep a random number of seconds linked to the frequency for requesting
			// the token to be able to switch the green light on. You can use the
			// initialisation
			// parameter in milliseconds (see param sec of the constructor).

			// Generate a random sleep time before requesting the token again
			int randomSleep = ra.nextInt(sec);
			System.out.println("Random waiting time for requesting the token is " + randomSleep + " ms");
			try {
				Thread.sleep(randomSleep);
			} catch (InterruptedException e) {
				System.out.println(
						"Exception    The Coordinator has been terminated and the token can no longer be passed to the nodes");
				e.printStackTrace();
			}

			try {
				// **** Send to the coordinator a token request.
				// send your ip address and port number
				// This section of code is implemented once just for the registration of the node with the coordinator
				
				// Checks if this section of code has already been implemented for the node 
				if (implemented == false) {
					// Creates a socket by providing the ip and the port number that the coordinator
					// is listening to
					s = new Socket(c_host, c_request_port);
					// A print writer to send the data of the sockets output stream, this has auto
					// flush on
					pout = new PrintWriter(s.getOutputStream(), true);
					// Writes the port number of the node on the output stream so that coordinator
					// gets the port where the node is listening to receive the token
					pout.println(n_port);
					// Close the socket
					s.close();
					System.out.println("Node " + n_host_name + ":" + n_port + " has been registered.");
					implemented = true;
				}

				// >>>
				// **** Wait for the token
				// this is just a synchronization
				// Print suitable messages

				// The server socket where the node listens for receiving the token from the
				// coordinator
				n_ss = new ServerSocket(n_port);

				// Accepts the connection with the server socket
				n_token = n_ss.accept();
				// Retrieves the input stream from the coordinator to the node
				InputStream in = n_token.getInputStream();
				// The buffered reader that reads the input stream from the coordinator
				BufferedReader bin = new BufferedReader(new InputStreamReader(in));
				String token = bin.readLine();
				System.out.println("Node " + n_host_name + ":" + n_port + " - Received token from to the coordinator.");

				// >>>
				// Switch the green light on (print suitable message - see instructions)
				// Sleep a bit, a second say.
				// Switch the light off.
				// This is the critical session
				try {
					System.out.println("Node " + n_host_name + ":" + n_port + " - Green Light is turned ON");
					Thread.sleep(3000); // Sleep timer of 3s before printing red light
					System.out.println("Node " + n_host_name + ":" + n_port + " - Green Light is turned OFF");
					// Close the server socket
					n_ss.close();
				} catch (InterruptedException e) {
					System.out.println("Exception    The critical section could not be executed");
					e.printStackTrace();
				}

				// >>>
				// **** Return the token
				// this is just establishing a synch connection to the coordinator's ip and
				// return port.
				// Print suitable messages - also considering communication failures
				try {
					// Creates a socket by providing the ip and port number of the mutex
					s = new Socket(c_host, c_return_port);
					// Writes the string token (that is received from the coordinator) into the
					// sockets output stream
					s.getOutputStream().write(token.getBytes());
					System.out.println(
							"Node " + n_host_name + ":" + n_port + " - Returns the token back to the coordinator. \n");
					// Close the sockets
					s.close();
					n_token.close();
				} catch (java.io.IOException e) {
					System.out.println("Exception    The token could not be returned");
					e.printStackTrace();
				}

			} catch (Exception e) {
				System.out.println(e);
				System.out.println(
						"Exception    The Coordinator has been terminated and the token can no longer be passed to the nodes");
				System.exit(1);
			}
		}
	}

	/*
	 * The main method that takes the arguments to run the nodes
	 * 
	 * @param args - The 2 arguments to be passed while running the Node [port
	 * number] [millisecs] The port number of the node with the sleep time before
	 * requesting for the token
	 */
	public static void main(String args[]) {

		String n_host_name = "";
		int n_port;

		// port and millisec (average waiting time) are specific of each node
		if ((args.length < 1) || (args.length > 2)) {
			System.out.print("Usage: Node [port number] [millisecs]");
			System.exit(1);
		}

		// get the IP address and the port number of the node
		try {
			InetAddress n_inet_address = InetAddress.getLocalHost();
			n_host_name = n_inet_address.getHostName();
			System.out.println("node hostname is " + n_host_name + ":" + n_inet_address);
		} catch (java.net.UnknownHostException e) {
			System.out.println(e);
			System.exit(1);
		}

		n_port = Integer.parseInt(args[0]);
		System.out.println("node port is " + n_port);

		Node n = new Node(n_host_name, n_port, Integer.parseInt(args[1]));
	}

}
