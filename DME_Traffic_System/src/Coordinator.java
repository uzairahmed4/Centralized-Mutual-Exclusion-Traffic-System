/*
 * Student ID - 2940038
 */

import java.net.*;

/*
 * The Coordinator class that is responsible for handling the requests from the nodes 
 * by using the threads of the receiver and the mutex class and giving the token to the node and getting it back
 */
public class Coordinator {

	public static void main(String args[]) {

		// The port number of the coordinator that receives the request from the node
		int port = 7000;

		// Creates and instance of a coordinator
		Coordinator c = new Coordinator();

		try {
			// Retrieves the host name of the coordinators system
			InetAddress c_addr = InetAddress.getLocalHost();

			// Retrieves the ip of the coordinator
			String c_name = c_addr.getHostName();

			System.out.println("Coordinator address is " + c_addr);
			System.out.println("Coordinator host name is " + c_name + "\n\n");
		} catch (Exception e) {
			System.err.println(e);
			System.err.println("Error in coordinator");
		}

		// allows defining port at launch time
		if (args.length == 1)
			port = Integer.parseInt(args[0]);

		// Creates a buffer object that stores the requests from the node
		C_buffer b = new C_buffer();
		// Creates an instance of the receiver by passing the buffer and the port number
		// of the coordinator (7000)
		C_receiver crec = new C_receiver(b, port);
		// Creates an instance of the mutex by passing the buffer and the port number of
		// the mutex(7001)
		C_mutex mtx = new C_mutex(b, port + 1);

		// >>> run the C_receiver and C_mutex object sharing a C_buffer object
		crec.start();
		mtx.start(); // runs the threads of the receiver and the mutex instances

	}

}
