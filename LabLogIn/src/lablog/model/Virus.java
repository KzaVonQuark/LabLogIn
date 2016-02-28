package lablog.model;

/**
 * 
 * @author Kza von Quark
 * Create a virus object.
 */
public class Virus extends Pathogen {

	private boolean enveloped;

	public Virus(String name, String type, int cfr, boolean enveloped) {
		super(name, type, cfr);
		this.enveloped = enveloped;
	}

	public boolean isEnveloped() {
		return enveloped;
	}

	public void setEnveloped(boolean enveloped) {
		this.enveloped = enveloped;
	}
	
	

}
