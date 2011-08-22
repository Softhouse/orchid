/**
 * 
 */
package se.softhouse.garden.orchid.demo.publisher;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author mis
 * 
 */
@Component
@Scope(value = "session")
public class SessionBeanImpl implements SessionBean {

	private String name;

	/**
     * 
     */
	public SessionBeanImpl() {
		System.out.println("Session created");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
