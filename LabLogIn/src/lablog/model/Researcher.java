package lablog.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Researcher extends User {

	private String pathogen, lab;
	private Date timeLogIn, timeLogOut;
	
	/**
	 * Creates Researcher when person logs in and log in time is set.
	 * 
	 * @param name - Inherit from User
	 * @param lastName - Inherit from User
	 * @param pathogen - sets when person logs in
	 * @param lab - sets when person logs in
	 * @param timeLogIn - sets when person logs in
	 * @param timeLogOut - sets when object logs out.
	 */
	public Researcher(String name, String lastName, String pathogen, String lab) {
		super(name, lastName, ""); //userData left blank, it is set from user interface. 
		this.pathogen = pathogen;
		this.lab = lab;
		this.timeLogIn = null;
		this.timeLogOut = null;
	}

	// Override toString for presention in listview (logged in).
	@Override
	public String toString() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return getName() +" "+ getLastName() + ":  " + pathogen + ", " + lab + "\t" + df.format(timeLogIn);
	}

	/**
	 * @return the pathogen
	 */
	public String getPathogen() {
		return pathogen;
	}

	/**
	 * @param pathogen the pathogen to set
	 */
	public void setPathogen(String pathogen) {
		this.pathogen = pathogen;
	}

	/**
	 * @return the lab
	 */
	public String getLab() {
		return lab;
	}

	/**
	 * @param lab the lab to set
	 */
	public void setLab(String lab) {
		this.lab = lab;
	}

	/**
	 * @return the timeLogIn
	 */
	public Date getTimeLogIn() {
		return timeLogIn;
	}

	/**
	 * @param timeLogIn the timeLogIn to set
	 */
	public void setTimeLogIn(Date timeLogIn) {
		this.timeLogIn = timeLogIn;
	}

	/**
	 * @return the timeLogOut
	 */
	public Date getTimeLogOut() {
		return timeLogOut;
	}

	/**
	 * @param timeLogOut the timeLogOut to set
	 */
	public void setTimeLogOut(Date timeLogOut) {
		this.timeLogOut = timeLogOut;
	}

}
