package lablog.model;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.LocalDateStringConverter;

/**
 * 
 * @author Linus Lundahl Model class for StatisticsController. Implements
 *         PrinterClient interface.
 * 
 */
public class Statistic implements PrintClient {

	FileManager fm;
	private ArrayList<StatsTable> logg;
	private ArrayList<StatsTable> selection;

	/**
	 * Constructor initializes FilManager and ArrayList.
	 */
	public Statistic() {
		super();
		fm = FileManager.FM;
		selection = new ArrayList<StatsTable>();
	}

	/**
	 * Load logg from file and creats StatsTable objects (for disply in
	 * TableView). Return data as ArrayList<StatsTable>.
	 * 
	 */
	public void loadLogg() {
		ArrayList<StatsTable> sTable = new ArrayList<StatsTable>();
		for (String[] string : fm.read("logg")) {
			sTable.add(new StatsTable(string[0] + " " + string[1], string[3], string[4], string[5] + " " + string[6],
					string[7] + " " + string[8]));
		}
		logg = sTable;
	}

	/**
	 * Extracts data from ObservableList<StatsTable> and store it in a String.
	 * Send Strings and File to write method in FileManager.
	 * 
	 * @param tableEntries
	 *            - Data from TableView.
	 * @param textArea
	 *            - Text from "display" in StatisticsController.
	 * @param file
	 *            - FileChooser generated file.
	 */
	public void saveStatistics(ObservableList<StatsTable> tableEntries, String textArea, File file) {
		String entries = "";
		for (StatsTable statsTable : tableEntries) {
			entries += statsTable.getName() + ", ";
			entries += statsTable.getPathogen() + ", ";
			entries += statsTable.getLaboratory() + "\n";
			entries += statsTable.getLogInDate() + ", ";
			entries += statsTable.getLogOutDate() + "\n\n";
		}

		fm.writeStatistics(textArea, entries, file);
	}

	// Selection methods

	/**
	 * Get data from ArrayList<StatsTable> logg, within selected dates (start,
	 * end). Call methods to check other selections.
	 * 
	 * @param start
	 *            - Datepicker start date.
	 * @param end
	 *            - Datepicker end date.
	 * @param name
	 *            - Specify name to show.
	 * @param pathogen
	 *            - Specify pathogen to show.
	 * @param lab
	 *            - Specify lab to show.
	 * @return ArrayList corresponding to user selection.
	 */
	public ArrayList<StatsTable> getSelection(LocalDate start, LocalDate end, String name, String pathogen,
			String lab) {
		selection.clear();

		if (start != null && end != null)
			selectDate(start, end);
		else if (start == null && end != null)
			selectDate(end.minusYears(100), end);
		else if (end == null && start != null)
			selectDate(start, LocalDate.now());
		else
			selection.addAll(logg);

		if (name != null)
			selectedName(name);
		if (pathogen != null)
			selectedPathogen(pathogen);
		if (lab != null)
			selectedLab(lab);

		return selection;
	}

	public ArrayList<String> individualCost(int cost) {
		TreeMap<String, Integer> temp = new TreeMap<String, Integer>();
		ArrayList<String> individualEntries = new ArrayList<String>();

		for (StatsTable st : selection) {
			if (temp.containsKey(st.getName())) {
				temp.replace(st.getName(), temp.get(st.getName()).intValue() + cost);
			} else
				temp.put(st.getName(), cost);
		}

		for (String string : temp.keySet()) {
			string += " " + temp.get(string);
			individualEntries.add(string);
		}

		return individualEntries;
	}

	public ArrayList<String> individualCost(int cost, int lab5) {
		TreeMap<String, Integer> temp = new TreeMap<String, Integer>();
		ArrayList<String> individualEntries = new ArrayList<String>();

		for (StatsTable st : selection) {
			if (temp.containsKey(st.getName())) {
				temp.replace(st.getName(), temp.get(st.getName()).intValue() + cost);
				if (st.getLaboratory().equals("Lab 5"))
					temp.replace(st.getName(), temp.get(st.getName()).intValue() + lab5);
			}
			else {
				temp.put(st.getName(), cost);
			if (st.getLaboratory().equals("Lab 5"))
				temp.replace(st.getName(), temp.get(st.getName()).intValue() + lab5);
			}
		}

		for (String string : temp.keySet()) {
			string += " " + temp.get(string);
			individualEntries.add(string);
		}

		return individualEntries;
	}

	/**
	 * Converts StatsView.logInDate & StatsView.logOutDate to LocalDate for
	 * comparison with DatePicker data (start and end). Add entires within time
	 * span to ArrayList<String> selection.
	 */
	private void selectDate(LocalDate start, LocalDate end) {
		LocalDateStringConverter ldc = new LocalDateStringConverter();
		String[] logIn, logOut;
		for (StatsTable stats : logg) {
			logIn = stats.getLogInDate().split("[ ]");
			logOut = stats.getLogInDate().split("[ ]");
			if (ldc.fromString(logIn[0]).isAfter(start) && ldc.fromString(logOut[0]).isBefore(end)) {
				selection.add(stats);
			}
		}
	}

	/**
	 * Remove all data that does not match selected name.
	 * 
	 * @param name
	 */
	private void selectedName(String name) {
		Iterator<StatsTable> nameItr = selection.iterator();
		while (nameItr.hasNext()) {
			StatsTable st = (StatsTable) nameItr.next();
			if (!st.getName().equals(name))
				nameItr.remove();
		}
	}

	/**
	 * Remove all data that does not match selected pathogen.
	 * 
	 * @param pathogen
	 */
	private void selectedPathogen(String pathogen) {
		Iterator<StatsTable> pathogenItr = selection.iterator();
		while (pathogenItr.hasNext()) {
			StatsTable st = (StatsTable) pathogenItr.next();
			if (!st.getPathogen().equals(pathogen))
				pathogenItr.remove();
		}
	}

	/**
	 * Remove all data that does not match selected laboratory.
	 * 
	 * @param lab
	 */
	private void selectedLab(String lab) {
		Iterator<StatsTable> labItr = selection.iterator();
		while (labItr.hasNext()) {
			StatsTable st = (StatsTable) labItr.next();
			if (!st.getLaboratory().equals(lab))
				labItr.remove();
		}
	}

	// Update methods

	/**
	 * Load all names from ArrayList<StatsTable> logg.
	 * 
	 * @return an ArrayList<String> with names.
	 */
	public ArrayList<String> updateName() {
		ArrayList<String> names = new ArrayList<String>();
		for (StatsTable param : logg) {
			if (!names.contains(param.getName()))
				names.add(param.getName());
		}
		return names;
	}

	/**
	 * Load all names from ArrayList<StatsTable> logg.
	 * 
	 * @return an ArrayList<String> with pathogen names.
	 */
	public ArrayList<String> updatePathogen() {
		ArrayList<String> pahogens = new ArrayList<String>();
		for (StatsTable param : logg) {
			if (!pahogens.contains(param.getPathogen()))
				pahogens.add(param.getPathogen());
		}
		return pahogens;
	}

	/**
	 * Load all names from ArrayList<StatsTable> logg.
	 * 
	 * @return an ArrayList<String> with laboratory names.
	 */
	public ArrayList<String> updateLab() {
		ArrayList<String> laboratories = new ArrayList<String>();
		for (StatsTable param : logg) {
			if (!laboratories.contains(param.getLaboratory()))
				laboratories.add(param.getLaboratory());
		}
		return laboratories;
	}

	// ---------------------- Print methods -----------------------

	/**
	 * Converts data from ObservableList<StatsTable> table to an ObservableList
	 * <String> table to match input parameters in PrintClient.
	 * 
	 * @param cost
	 * @param table
	 * @return infromation from PrinterServer.
	 */
	public String printToServer(String cost, ObservableList<StatsTable> table) {

		ObservableList<String> entries = FXCollections.observableArrayList();
		for (StatsTable entry : table) {
			entries.add(entry.getName() + " " + entry.getPathogen() + " " + entry.getLaboratory() + " "
					+ entry.getLogInDate() + " " + entry.getLogOutDate());
		}

		return PrintClient.printToServer(cost, entries);
	}

	// Getters and setters
	/**
	 * @return the logg
	 */
	public ArrayList<StatsTable> getLogg() {
		return logg;
	}

	/**
	 * @param logg
	 *            the logg to set
	 */
	public void setLogg(ArrayList<StatsTable> logg) {
		this.logg = logg;
	}

}
