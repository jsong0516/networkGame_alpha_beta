/*
Name : Byung Gon Song
ID : cs61b-ayg
Project 2
Description: This is a class for singly linked list. 
*/

package list;

public class slist {
	private slistNode head;
	private slistNode tail;
	private int size;

	public slist() {
		size = 0;
		head = null;
		tail = null;
	}
    /**
     * A method return the size of DList
     * @return size the size of the DList
     */
	public int size() {
		return size;
	}
	/**
	 * A method check is the size is empty or not
	 * @return true if the size is empty
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 *	Insert an object at the end of list. It doesn't insert null object.
	 */
	public void insert(Object item) {
		if (item == null)
			return; // End immideately if item is null
		if(isEmpty()) {
			head = new slistNode(item);
			tail = head;
		} else {
			tail = new slistNode(item, tail);
		}
		size++;
	}
	
    /**
     * A method to remove the Front slistNode
     */
	public void removeFront() {
		if(isEmpty()) 
			return; // End immideately if list is Empty;
		if(head == tail) {
			head = null;
			tail = null;
		} else {
			head = head.next;
		}
		size--;
	}
	/**
	 * A method return the head of slistNode
	 * @return head the first slistNode
	 */
	public slistNode first() {
		return head;
	}
    /**
     * A method return the tail of slistNode
     * @return tail
     */
	public slistNode end() {
		return tail;
	}

}
