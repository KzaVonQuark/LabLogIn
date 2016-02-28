
package lablog.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import javafx.collections.ObservableList;
import lablog.view.StatisticsController;

/**
 * 
 * @author Kza von Quark User inherited klass that holds all administration
 *         method. Class is used as a model class for AdminController
 */
public class Administrator extends User implements PrintClient {

	FileManager fm;
	StatisticsController sc;

	private TreeMap<String, User> researchers, administrators;
	private TreeMap<String, Pathogen> pathogens;
	private TreeMap<String, Laboratory> laboratories;

	/**
	 * Load users, pathogens and laboratories to TreeMap's. Create a statistics
	 * controller and statistic class.
	 * 
	 * @param name
	 * @param userData
	 * @param password
	 */
	Administrator(String name, String lastName, String password) {
		super(name, lastName, password);
		fm = FileManager.FM;

		sc = new StatisticsController();
		sc.setStats(new Statistic());

		loadResearchers();
		loadAdministrators();
		loadPathogens();
		loadLaboratories();
	}
	// Aggregering - Administrator har en statisticsControllet (skapas i kostruktorn).
	

	// ----------------------- User methods ------------------------------

	/**
	 * Add a new user to researcher TreeMap<String, User>. Name and last name is
	 * set as key.
	 * 
	 * @param firstName
	 * @param lastName
	 * @param department
	 */
	public void addUser(String firstName, String lastName, String department) {
		firstName = firstName.replaceAll(" ", "-");
		lastName = lastName.replaceAll(" ", "-");
		String name = firstName + " " + lastName;

		researchers.put(name, new User(firstName, lastName, department));

	}

	/**
	 * Split name into first and last name and put a administration boject into TreeMap, with name as key. 
	 * @param name
	 * @param password
	 */
	public void addAdministrator(String name, String password) {
		String[] splitName = name.split("[ ]");

		administrators.put(name, new User(splitName[0], splitName[1], password));

	}

	/**
	 * Remove researcher object form TreeMap using name as key. 
	 * @param userName
	 */
	public void removeResearcher(String userName) {
		researchers.remove(userName);
	}

	/**
	 * Remove administrator object form TreeMap using name as key. 
	 * @param userName
	 */
	public void removeAdministrator(String userName) {
		administrators.remove(userName);
	}

	/**
	 * Rewrite all user data to equivalent TreeMap object. If user is registered administrator
	 * administrator TreeMap is also rewritten.
	 * 
	 * @param user - object subject of change.
	 * @param newName
	 * @param newLastName
	 * @param department
	 * @param password
	 * @param admin
	 */
	public void editUser(String user, String newName, String newLastName, String department, String password,
			boolean admin) {
		researchers.get(user).setName(newName);
		researchers.get(user).setLastName(newLastName);
		researchers.get(user).setUserData(department);
		if (admin) {
			administrators.get(user).setName(newName);
			administrators.get(user).setLastName(newLastName);
			administrators.get(user).setUserData(password);
		}
	}

	/**
	 * Get current administrator and set new userdata (password).
	 * @param newPassword
	 */
	public void changePassword(String newPassword) {
		administrators.get(this.getName() + " " + this.getLastName()).setUserData(newPassword);
	}

	// ----------------------- Pathogen methods ------------------------------

	/**
	 * 	 * Add a new virus or bacteria to patghogen TreeMap<String, Pathogen>.
	 * Pathogen name set as key.
	 * 
	 * @param name
	 * @param type
	 * @param cfr
	 * @param characteristic - is enveloped for virus, has spores for bacteria.
	 */
	public void addPathogen(String name, String type, int cfr, boolean characteristic) {
		if (type.equals("virus"))
			pathogens.put(name, new Virus(name, type, cfr, characteristic));
		else if (type.equals("bacteria"))
			pathogens.put(name, new Bacteria(name, type, cfr, characteristic));
	}

	/**
	 * Remove pathogen object form TreeMap using name as key. 
	 * @param name
	 */
	public void removePathogen(String name) {
		pathogens.remove(name);
	}

	/**
	 * Take a pathogen object and checks if it is virus or bacteria.
	 * Then return boolean true if it is enveloped or has spores.
	 * @param name
	 * @return
	 */
	public boolean checkCharacteristic(String name) {
		Virus virus;
		Bacteria bacteria;
		if (pathogens.get(name).getType().equals("virus")) {
			virus = (Virus) pathogens.get(name);
			return virus.isEnveloped();
		} else {
			bacteria = (Bacteria) pathogens.get(name);
			return bacteria.isSpores();
		}
	}

	/**
	 * Rewrite all pathogen  data to equivalent TreeMap object.
	 * Check if pathogen is virus or bacteria and write to associated TreeMap
	 * @param name
	 * @param newName
	 * @param newCfr
	 * @param characteristic
	 */
	public void editPathogen(String name, String newName, int newCfr, boolean characteristic) {
		Virus virus;
		Bacteria bacteria;
		pathogens.get(name).setName(newName);
		pathogens.get(name).setCfr(newCfr);
		if (pathogens.get(name).getType().equals("virus")) {
			virus = (Virus) pathogens.get(name);
			virus.setEnveloped(characteristic);
			pathogens.replace(name, virus);
		} else {
			bacteria = (Bacteria) pathogens.get(name);
			bacteria.setSpores(characteristic);
			pathogens.replace(name, bacteria);
		}
	}

	// ----------------------- Laboratory methods ------------------------------

	/**
	 * Add a new laboratory to laboratories TreeMap<String, Laboratory>.
	 * Laboratory name set as key.
	 * @param name
	 * @param saftyLevel
	 */
	public void addLab(String name, int saftyLevel) {
		laboratories.put(name, new Laboratory(name, saftyLevel));
	}

	public void removeLab(String name) {
		laboratories.remove(name);
	}

	/**
	 * Remove selected laboratory and calls addLab-method with new name and/or new safty level.
	 * 
	 * @param name
	 * @param saftyLevel
	 */
	public void editLab(String name, int saftyLevel) {
		laboratories.remove(name);
		addLab(name, saftyLevel);
	}

	// ----------------------- Auxillary methods ------------------------------

	/**
	 * Write all data to files (researchers, administrators, pathogens, laboratories), unless any TreeMap is null;
	 * Call save methods in FileManage
	 */
	public void exitAdmin() {

		sc.setStats(null);

		if (researchers != null) {
			fm.clearFile("researcher");
			for (User researcher : researchers.values()) {
				fm.saveUser(researcher, "researcher");
			}
		}
		if (administrators != null) {
			fm.clearFile("administrator");
			for (User admin : administrators.values()) {
				fm.saveUser(admin, "administrator");
			}
		}
		if (pathogens != null) {
			fm.clearFile("virus");
			fm.clearFile("bacteria");
			for (Pathogen pathogen : pathogens.values()) {
				if (pathogen.getType().equals("virus"))
					fm.saveVirus((Virus) pathogen);
				else if (pathogen.getType().equals("bacteria"))
					fm.saveBacteria((Bacteria) pathogen);
			}
		}
		if (laboratories != null) {
			fm.clearFile("laboratory");
			for (Laboratory lab : laboratories.values()) {
				fm.saveLaboratory(lab);
			}
		}
	}
	
	/**
	 * Load users from file to TreeMap<String, User>.
	 * Is called from constructor.
	 */
	private void loadResearchers() {
		researchers = new TreeMap<String, User>();
		for (String[] string : fm.read("researcher")) {
			researchers.put(string[0] + " " + string[1], new User(string[0], string[1], string[2]));
		}
	}

	/**
	 * Load administrators from file to TreeMap<String, User>.
	 * Is called from constructor.
	 */
	private void loadAdministrators() {
		administrators = new TreeMap<String, User>();
		for (String[] string : fm.read("administrator")) {
			administrators.put(string[0] + " " + string[1], new User(string[0], string[1], string[2]));
		}
	}

	/**
	 * Load pathogens from file to TreeMap<String, Pathogen>.
	 * Is called from constructor.
	 */
	private void loadPathogens() {
		pathogens = new TreeMap<String, Pathogen>();
		for (String[] string : fm.read("virus")) {
			pathogens.put(string[0],
					new Virus(string[0], string[1], Integer.parseInt(string[2]), Boolean.parseBoolean(string[3])));
		}
		for (String[] string : fm.read("bacteria")) {
			pathogens.put(string[0],
					new Bacteria(string[0], string[1], Integer.parseInt(string[2]), Boolean.parseBoolean(string[3])));
		}
	}
	/**
	 * Load laboratories from file to TreeMap<String, Laboratory>.
	 * Is called from constructor.
	 */
	private void loadLaboratories() {
		laboratories = new TreeMap<String, Laboratory>();
		for (String[] string : fm.read("laboratory")) {
			laboratories.put(string[0], new Laboratory(string[0], Integer.parseInt(string[1])));
		}
	}

	/**
	 * Check name of file and calls read method in FileManager.
	 * Returns a sorted ArrayList<String>
	 * @param fileName
	 * @return 
	 */
	public ArrayList<String> updateEntries(String fileName) {
		ArrayList<String> entries = new ArrayList<String>();
		if (fileName.equals("researcher") || fileName.equals("administrator")) {
			for (String[] string : fm.read(fileName + ".txt")) {
				entries.add(string[0] + " " + string[1]);
			}
		} else
			for (String[] string : fm.read(fileName)) {
				entries.add(string[0]);
			}
		Collections.sort(entries);
		return entries;
	}

	/**
	 * Do a NumberFromatException check on entered integer.
	 * @param string
	 * @return valid integet (int)
	 */
	public int checkInt(String string) {
		int number = 0;
		try {
			number = Integer.parseInt(string);
		} catch (NumberFormatException e) {
		}
		return number;
	}

	/**
	 * Call read from FileManager and recieves ArrayList<String[]>.
	 * Read String[] and add all elements to String text.
	 * @return text string.
	 */
	public String loadInfoText() {
		String text = "";
		for (String[] string : fm.read("infotext")) {
			for (String string2 : string) {
				text += string2 + "\n";
			}
		}
		return text;
	}

	/**
	 * Call save method in FileManager to save current information text.
	 * @param infoText
	 */
	public void saveInfoText(String infoText) {
		fm.saveInfotext(infoText);
	}

	/**
	 * Interface method that send entries to printer server.
	 * @param description
	 * @param entries
	 * @return printer information from server.
	 */
	public String printToServer(String description, ObservableList<String> entries) {
		return PrintClient.printToServer(description, entries);
	}

	/**
	 * @return the researchers
	 */
	public TreeMap<String, User> getResearchers() {
		return researchers;
	}

	/**
	 * @param researchers
	 *            the researchers to set
	 */
	public void setResearchers(TreeMap<String, User> researchers) {
		this.researchers = researchers;
	}

	/**
	 * @return the administrators
	 */
	public TreeMap<String, User> getAdministrators() {
		return administrators;
	}

	/**
	 * @param administrators
	 *            the administrators to set
	 */
	public void setAdministrators(TreeMap<String, User> administrators) {
		this.administrators = administrators;
	}

	/**
	 * @return the pathogens
	 */
	public TreeMap<String, Pathogen> getPathogens() {
		return pathogens;
	}

	/**
	 * @param pathogens
	 *            the pathogens to set
	 */
	public void setPathogens(TreeMap<String, Pathogen> pathogens) {
		this.pathogens = pathogens;
	}

	/**
	 * @return the laboratories
	 */
	public TreeMap<String, Laboratory> getLaboratories() {
		return laboratories;
	}

	/**
	 * @param laboratories
	 *            the laboratories to set
	 */
	public void setLaboratories(TreeMap<String, Laboratory> laboratories) {
		this.laboratories = laboratories;
	}

}
