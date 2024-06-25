/*
 * Student ID - 2940038
 */

import java.net.*;
import java.io.*;

/*
 * Reacts to a node request.
 * Receives and records the node request in the buffer.
 */
public class C_Connection_r extends Thread {

	// class variables
	C_buffer buffer; // An instance of C_Buffer to save the requests from the node

	Socket s; // Socket to receive the requests from the node
	InputStream in; // InputStream to read the request data from the node
	BufferedReader bin; // BufferedReader to read the text from the InputStream

	/*
	 * A constructor that is used to establish a connection between the node and the
	 * coordinator by listening to the port from the socket.
	 * 
	 * @param s - The socket that has the ip and the port number of the node that
	 * has requested
	 * 
	 * @param b - The buffer that stores the requests made from the node
	 */
	public C_Connection_r(Socket s, C_buffer b) {
		this.s = s;
		this.buffer = b;

	}

	/*
	 * Stores the ip and the port number of the node that requests for the token and
	 * stores it in the buffer. The method runs when the instance of connection
	 * thread is started.
	 */
	public void run() {

		// The index of the node ip and the port number in the array of requests
		final int NODE = 0;
		final int PORT = 1;

		// The request array that holds 2 values, the ip and the port of the node thats
		// requests
		String[] request = new String[2];

		// Displays that the connection between the coordinator and the node has been
		// established
		System.out.println("C:Connection IN    Dealing with request from socket " + s);
		try {

			// >>> read the request, i.e. node ip and port from the socket s
			// >>> save it in a request object and save the object in the buffer (see
			// C_buffer's methods).

			// An instance of the input stream that uses the socket passed in the connection
			in = s.getInputStream();
			// An instance of the buffered reader to read from the input stream
			bin = new BufferedReader(new InputStreamReader(in));

			// Retrieves the ip of the node that requests the token
			request[NODE] = s.getInetAddress().getHostAddress();
			// Retrieves the port number of the node that requests the token
			request[PORT] = bin.readLine();

			// Saves the request array of the nodes ip and port number into the buffer using
			// the saveRequest method of the buffer class
			buffer.saveRequest(request);

			// Closes the socket
			s.close();

			// Displays the received and recorded request from the node
			System.out.println("C:Connection OUT    Received and recorded request from " + request[NODE] + ":"
					+ request[PORT] + "  (socket closed)");

		} catch (java.io.IOException e) {
			System.out.println(
					"C:Connection    Noticed an issue in retrieving the ip and port number of the node. Please ensure that the node has valid ip and port number entered in the run configuration.");
			System.out.println(e);
			System.exit(1);
		}

		// Displays the contents of the buffer
		buffer.show();

	} // end of run() method

} // end of class
