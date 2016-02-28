package lablog.model;

import java.util.ArrayList;
import java.util.Date;

import javafx.collections.ObservableList;
import lablog.view.AdminController;

public class LogIn implements PrintClient {

	FileManager fm;
	private AdminController ac;
	ArrayList<Researcher> researchers;

	public LogIn() {
		super();
		this.researchers = new ArrayList<Researcher>();
		
		fm = FileManager.FM;
	}

	/**
	 * Creates a researcher object from parameters. Adds user data from file.
	 * 
	 * @param name
	 * @param pathogen
	 * @param lab
	 * @return a researcher object.
	 */
	public Researcher listResearcher(String name, String pathogen, String lab) {
		String[] names = name.split("[ ]");
		Researcher researcher = new Researcher(names[0], names[1], pathogen, lab);
		researcher.setTimeLogIn(new Date());
		researchers.add(researcher);
		for (String[] string : fm.read("researcher")) {
			if (string[0].equals(names[0]))
				researcher.setUserData(string[2]);
		}
		return researcher;
	}

	/**
	 * Set log out time on user object and sends user data to write method in
	 * FileManager.
	 * 
	 * @param selected - Containing all user data.
	 */
	public void logOut(String selected) {
		String[] name = selected.split("[:]");
		for (Researcher researcher : researchers) {
			String fullName = researcher.getName() + " " + researcher.getLastName();
			if (fullName.equals(name[0])) {
				researcher.setTimeLogOut(new Date());
				fm.writeLogOut(researcher);
			}
		}
	}

	/**
	 * Load combobox data from read method in FileManager.
	 * 
	 * @param fileName - name of file that stores data 
	 * @return a sorted ArrayList<String>.
	 */
	public ArrayList<String> loadLogInFields(String fileName) {
		ArrayList<String> text = new ArrayList<String>();
		if (fileName.equals("researcher") || fileName.equals("administrator")) {
			for (String[] string : fm.read(fileName)) {
				text.add(string[0] + " " + string[1]);
			}
		} else {
			for (String[] string : fm.read(fileName)) {
				text.add(string[0]);
			}
		}
		return text;
	}	
	
	/**
	 * Loads infotext data from infotext.txt, via read method in Filemanager.
	 * @return a infotext string
	 */
	public String loadInfoText() {
		String infotext = "";
		for (String string : loadLogInFields("infotext")) {
			infotext += string + "\n";
		}
		return infotext;
	}
	
	public void saveLoggedIn(ObservableList<String> entries) {
		String loggedIn = "";
		for (String string : entries) {
			loggedIn += string + "\n";
		}
		fm.saveLoggedIn(loggedIn);
	}
	
	public ArrayList<String> loadLoggedIn() {
		ArrayList<String> loggedIn = new ArrayList<String>();
		for (String[] string : fm.read("loggedin")) {
			loggedIn.add(string[0]);
		};
		return loggedIn;
	}

	/**
	 * Divide name into first and last name and creats new administrator using parameters. 
	 * @param name
	 * @param password
	 * @return
	 */
	public boolean createAdmin(String name, String password) {
		String[] nameSplit = name.split("[ ]");
		if (checkPassword(name, password)) {
			ac = new AdminController();
			ac.setAdmin(new Administrator(nameSplit[0], nameSplit[1], password));
			return true;
		} else
			return false;
	}

	/**
	 * Calls FileManager read method and gets name and password. Check if name
	 * and password is registered.
	 * 
	 * @param name
	 * @param password
	 * @return - true if name and password is ok. Else returns false.
	 */
	public boolean checkPassword(String name, String password) {
		for (String[] string : fm.read("administrator")) {
			if (name.equals(string[0] + " " + string[1]) && password.equals(string[2])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Connects to printer server. Then convert parm ObservableList<String> to one string
	 * and send it to printer server.
	 * Closes client server and and socket.
	 * @param listEntries
	 * @return
	 */
	public String printToServer(String description, ObservableList<String> entries) {
		return PrintClient.printToServer(description, entries);
	}
}