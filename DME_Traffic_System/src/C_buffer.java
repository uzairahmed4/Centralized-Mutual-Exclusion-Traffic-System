/*
 * Student ID - 2940038
 */

import java.util.*;

/*
 * The C_buffer class handles all the requests from the nodes and stores all the requests in a vector object called data. 
 * The C_mutex then responds to these requests by sending the token to the node requesting for it.
 * The port and ip of the nodes are saved in the FIFO - First In First Out order.
 */
public class C_buffer {

	// Initialize the vector of objects data
	private Vector<Object> data;
	private int i = 0;

	/*
	 * Constructor for buffer class
	 */
	public C_buffer() {
		// Instantiates the vector of objects
		data = new Vector<Object>();
	}

	/*
	 * Returns the size of the buffer
	 * 
	 * @return The number of elements/requests in the buffer
	 */
	public int size() {

		// returns the number of elements/requests in the buffer
		return data.size();
	}

	/*
	 * Saves the port and ip of the node from the request array and stores it in the
	 * vector of objects.
	 * 
	 * @param r - The request array that stores the port and ip of the node from the
	 * request
	 */
	public synchronized void saveRequest(String[] r) {
		boolean seek = false; // Boolean to check if the request from the node already exists or not

		// Iterates through the buffer in steps of two since the array stores the port
		// number and ip of the node as two elements now which are adjacent to
		// each other
		for (int i = 0; i < data.size(); i += 2) {
			// Checks if the pair of elements in the buffer list matches with the elements
			// in the save request
			if (data.get(i).equals(r[0]) && data.get(i + 1).equals(r[1])) {
				// sets the boolean to true if its equal so that the request is not stored again
				// until the previous request has been executed
				seek = true;
				break;
			}
		}

		// If the two elements (the port number and sleep time of the node) does not
		// exist in the buffer then it is added
		if (!seek) {
			data.add(r[0]);
			data.add(r[1]);
		}
	}

	/*
	 * Displays the nodes that have their requests saved in the buffer
	 */
	public void show() {

		// Iterates and displays all the nodes that have their requests saved in the
		// buffer
		for (int i = 0; i < data.size(); i++)
			System.out.print(" " + data.get(i) + " ");
		System.out.println(" ");
	}

	/*
	 * Adds an object in the buffer holding the requests
	 * 
	 * @param o - The object to add elements in buffer
	 */
	public void add(Object o) {

		// Adds an object in the buffer holding the requests
		data.add(o);

	}

	/*
	 * Gets the first element (FIFO) from the buffer and returns it. The element is
	 * then removed from the buffer. If there are no elements present in the buffer
	 * it returns null.
	 * 
	 * @return returns the first element from the buffer
	 */
	synchronized public Object get() {

		Object o = null;

		// The object o iterates through the array of buffer by starting with the i
		// value 0
		o = data.get(i);
		i++;

		// The index value i is checked if it is equal to or greater then the array size
		if (i >= data.size()) {
			// When the value becomes equal to the size of the array then it is set to 0 so
			// that it ends up in a circular iteration throughout the array
			i = 0;
		}
		return o;
	}

}
