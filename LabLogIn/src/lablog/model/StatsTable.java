package lablog.model;

public class StatsTable {

	private String name;
	private String pathogen;
	private String laboratory;
	private String logInDate;
	private String logOutDate;

	/*
	 * private SimpleStringProperty department; private SimpleIntegerProperty
	 * logInTime; private SimpleIntegerProperty logOutTime;
	 */
	public StatsTable(String name, String pathogen, String lab, String logInDate, String logOutDate) {
		super();
		this.name = name;
		this.pathogen = pathogen;
		this.laboratory =lab;
		this.logInDate = logInDate;
		this.logOutDate = logOutDate;
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
	 * @return the laboratory
	 */
	public String getLaboratory() {
		return laboratory;
	}

	/**
	 * @param laboratory the laboratory to set
	 */
	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}


	/**
	 * @return the logInDate
	 */
	public String getLogInDate() {
		return logInDate;
	}


	/**
	 * @param logInDate the logInDate to set
	 */
	public void setLogInDate(String logInDate) {
		this.logInDate = logInDate;
	}


	/**
	 * @return the logOutDate
	 */
	public String getLogOutDate() {
		return logOutDate;
	}


	/**
	 * @param logOutDate the logOutDate to set
	 */
	public void setLogOutDate(String logOutDate) {
		this.logOutDate = logOutDate;
	}

}
