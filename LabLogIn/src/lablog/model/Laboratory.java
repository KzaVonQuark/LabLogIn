package lablog.model;

/**
 * 
 * @author Kza von Quark
 * Create a Laboratory object.
 */
public class Laboratory {
	
	private String name;
	private int securityLevel;
	
	/**
	 * Constructor creates a laboratory.
	 * @param name
	 * @param securityLevel
	 */
	public Laboratory(String name, int securityLevel) {
		super();
		this.name = name;
		this.securityLevel = securityLevel;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the securityLevel
	 */
	public int getSecurityLevel() {
		return securityLevel;
	}

	/**
	 * @param securityLevel the securityLevel to set
	 */
	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
	}
	

}