/**
 * 
 */
package se.softhouse.garden.orchid.demo.publisher;

/**
 * @author mis
 * 
 */
public interface SessionBean {

	String getId();

	/**
	 * @param name
	 */
	void setName(String name);

	/**
	 * @return
	 */
	String getName();

}