/*
Name : Byung Gon Song
ID : cs61b-ayg
Project 2
*/

package list;

public class slistNode {
	public Object item;
	public slistNode next;
	/**
	 * A constructor with
	 * item = null;
	 * next = null;
	 */
	public slistNode() {
		item = null;
		next = null;
	}
	/**
	 * @param object any object
	 * @param next get the next item of the slistNode
	 */
	public slistNode(Object object, slistNode next){
		item= object;
		this.next = next;
	}
    /**
     * A Constructor to take a object
     * @param object any object
     */
	public slistNode(Object object) {
		this(object, null);
	}

}
