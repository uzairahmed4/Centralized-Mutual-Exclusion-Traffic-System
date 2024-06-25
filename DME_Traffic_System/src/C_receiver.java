/*
 * Student ID - 2940038
 */

import java.io.IOException;
import java.net.*;

/*
 * Listens for the request from the node. The request from the node consists of the ip and port number which on connection stores the 
 * request in an array and then creates a thread for the request. 
 */
public class C_receiver extends Thread {

	private C_buffer buffer; // Instance of the buffer class
	private int port; // The port number of the server socket which listens to the request from the
						// node

	private ServerSocket s_socket; // The server socket that listens to the request from the node
	private Socket socketFromNode; // The socket from node that connects to the coordinator

	private C_Connection_r connect; // An instance of the connection class after the connection has been established

	/*
	 * A constructor for the receiver that uses the buffer and the port number as
	 * the parameters
	 * 
	 * @param b - The buffer that holds the requests of the nodes
	 * 
	 * @param p - The port number that listens to the requests
	 */
	public C_receiver(C_buffer b, int p) {
		buffer = b;
		port = p;
	}

	/*
	 * The receiver thread that creates a socket that the coordinator listens to
	 * which gets the requests from the node and establishes a connection between
	 * the coordinator and the node
	 */
	public void run() {

		// >>> create the socket the coordinator will listen to
		try {
			// Socket creation connected to a specified port
			s_socket = new ServerSocket(port);
			System.out.println("C:Receiver    Waiting for the request for the token from the Node");
		} catch (IOException e) {
			System.out.println("Exception    The connection could not be made" + e);
		}

		while (true) {

			try {

				// >>> get a new connection

				// Accepts the request from the server socket
				socketFromNode = s_socket.accept();
				System.out.println("C:Receiver    Coordinator has received a request.");
				// >>> create a separate thread to service the request, a C_Connection_r thread.

				// An instance of the connection thread storing the request from the node
				connect = new C_Connection_r(socketFromNode, buffer);
				// starts the connection thread
				connect.start();

			} catch (java.io.IOException e) {
				System.out.println("Exception    When creating a connection " + e);
			}

		}
	}// end run

}
