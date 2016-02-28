package lablog.model;

/**
 * 
 * @author Kza von Quark
 * Creats a bacteria object.
 */
public class Bacteria extends Pathogen {
	
	private boolean spores;

	public Bacteria(String name, String type, int cfr, boolean spores) {
		super(name, type, cfr);
		this.spores = spores;
	}

	/**
	 * @return the spores
	 */
	public boolean isSpores() {
		return spores;
	}

	/**
	 * @param spores the spores to set
	 */
	public void setSpores(boolean spores) {
		this.spores = spores;
	}

}
