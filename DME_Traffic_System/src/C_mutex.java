/*
 * Student ID - 2940038
 */

import java.net.*;

/*
 * The Mutex class is responsible for sending the token to the nodes requesting for it and getting back the token to be returned to the coordinator
 */
public class C_mutex extends Thread {

	C_buffer buffer; // An instance of the buffer class
	Socket s; // Socket of the node that asks for the token
	int port; // port number of the mutex object

	// The string token that is used to confirm the request from the node socket
	String token = "token";

	// ip address and port number of the node requesting the token.
	// They will be fetched from the buffer
	String n_host;
	int n_port;

	/*
	 * Constructor for the mutex object
	 * 
	 * @param b - The buffer that stores the request
	 * 
	 * @param p - The port number of the mutex
	 */
	public C_mutex(C_buffer b, int p) {

		buffer = b;
		port = p;
	}

	public void go() {

		try {
			// >>> Listening from the server socket on port 7001
			// from where the TOKEN will be later on returned.
			// This place the coordination server creation outside the while loop.

			// The port number of the mutex server socket is 7001, it listens to the request
			// on that port
			ServerSocket ss_back = new ServerSocket(7001);

			while (true) {

				// >>> Print some info on the current buffer content for debuggin purposes.
				// >>> please look at the available methods in C_buffer
				System.out.println("C:Mutex    Buffer size is " + buffer.size());

				// A sleep time to avoid the spam that displays the buffer size
				Thread.sleep(3000);

				// >>> if the buffer is not empty
				if (buffer.size() > 0) {
					// >>> Getting the first (FIFO) node that is waiting for a TOKEN form the buffer
					// Type conversions may be needed.

					// Retrieves the host and port of the first node in the buffer
					n_host = (String) buffer.get();
					n_port = Integer.parseInt((String) buffer.get());

					// >>> **** Granting the token to the node
					//
					try {

						// Create a socket by providing the node host and the port
						s = new Socket(n_host, n_port);
						// Writes the string token into the sockets output stream
						s.getOutputStream().write(token.getBytes());
						System.out.println("C:Mutex    The Token has been granted to " + n_host + ":" + n_port);
						// Close the socket
						s.close();

					} catch (java.io.IOException e) {
						System.out.println(e);
						System.out.println("CRASH: Mutex    Connecting to the node for granting the TOKEN" + e);
					}

					// >>> **** Getting the token back from the node
					try {
						// Accepts the connection with the server socket
						Socket s_receive = ss_back.accept();
						// Create an array of length 128, this array is further used to store the data
						// from the input stream of the socket
						byte[] byteArray = new byte[128];
						// Reads the data from the input stream of the socket and stores it in the byte
						// array
						s_receive.getInputStream().read(byteArray);
						// Close the socket
						s_receive.close();
						// Removes the white spaces in the byte array and stores it in a string message
						String message = new String(byteArray).trim();
						// Checks if the message from the input stream of the socket is equal to 'token'
						// string and grants the token if its equal
						if (message.equals(token)) {
							System.out.println("C:Mutex    The Token has been sent from " + n_host + ":" + n_port
									+ " back to the Coordinator");
						} else {
							System.out.println("ERROR " + n_host + ":" + n_port);
						}
					} catch (java.io.IOException e) {
						System.out.println(e);
						System.out.println("CRASH: Mutex    Waiting for the TOKEN back" + e);
					}
				} // endif
			} // endwhile
		} catch (Exception e) {
			System.out.print(e);
			System.out.println(
					"Exception    The Nodes have been terminated and there are no token requests to the coordinator"
							+ e);
		}

	}

	// When the mutex thread is started it runs the code in this method which calls
	// the go() method from this class
	public void run() {

		go();
	}

}
