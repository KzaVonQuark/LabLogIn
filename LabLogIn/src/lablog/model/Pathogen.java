package lablog.model;

/**
 * 
 * @author Kza von Quark
 * Abstract class for createing different pathogen objects.
 * Atm virus and bacteria are created.
 */
public abstract class Pathogen {
	
	private String name, type;
	private int cfr;

	public Pathogen(String name, String type, int cfr) {
		super();
		this.name = name;
		this.type = type;
		this.cfr = cfr;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the cfr
	 */
	public int getCfr() {
		return cfr;
	}

	/**
	 * @param cfr the cfr to set
	 */
	public void setCfr(int cfr) {
		this.cfr = cfr;
	}
	
}
