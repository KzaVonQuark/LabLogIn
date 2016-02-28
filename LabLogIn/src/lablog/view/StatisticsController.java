package lablog.view;

import lablog.MainLab;
import lablog.model.Statistic;
import lablog.model.StatsTable;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * Controller for StatsView.fxml
 * @author Linus Lundahl
 *
 */
public class StatisticsController implements Initializable {

	private MainLab lab;
	static Statistic stats;
	private final int COST = 33;
	File userDir;

	
	private ObservableList<StatsTable> tableEntries = FXCollections.observableArrayList();
	private ObservableList<String> nameEntries = FXCollections.observableArrayList();
	private ObservableList<String> pathogenEntries = FXCollections.observableArrayList();
	private ObservableList<String> labEntries = FXCollections.observableArrayList();
	
	@FXML ImageView labImage;
	
	@FXML TableView<StatsTable> statsTable;
	@FXML TableColumn<StatsTable, String> name;
	@FXML TableColumn<StatsTable, String> pathogen;
	@FXML TableColumn<StatsTable, String> laboratory;
	@FXML TableColumn<StatsTable, String> logInDate;
	@FXML TableColumn<StatsTable, String> logOutDate;
	
	@FXML DatePicker datepicker1;
	@FXML DatePicker datepicker2;
	
	@FXML ComboBox<String> selectName;
	@FXML ComboBox<String> selectPathogen;
	@FXML ComboBox<String> selectLab;
	
	@FXML TextArea display;
	@FXML CheckBox lab5;
	@FXML Button save;
	
	@FXML
	private void handleExit() {
		lab.changeStage("AdministrationView");
	}

	@FXML
	private void getStatistics() {
		tableEntries.clear();
		tableEntries.addAll(stats.getSelection(datepicker1.getValue(), datepicker2.getValue(), selectName.getValue(),
				selectPathogen.getValue(), selectLab.getValue()));
		statsTable.setItems(tableEntries);
		if (lab5.isSelected())
			cost(tableEntries.size(), 33);
		else
			cost(tableEntries.size());
	}

	@FXML
	private void clear() {
		selectName.setValue(null);
		selectPathogen.setValue(null);
		selectLab.setValue(null);
		selectName.setPromptText("Name");
		selectPathogen.setPromptText("Pathogen");
		selectLab.setPromptText("Lab");
	}

	@FXML
	private void handleCheckBox() {
		if (lab5.isSelected())
			cost(tableEntries.size(), 33);
		else
			cost(tableEntries.size());
	}

	@FXML
	private void saveButton() {
		FileChooser fileChooser = new FileChooser();

		chooseSaveDir(fileChooser);

		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			stats.saveStatistics(tableEntries, display.getText(), file);
		}
	}


	@FXML
	private void printStatistics() {
		String description = "Registered reseachers printed";
		String printConfirm = stats.printToServer(description, tableEntries);
		Alert printAlert = new Alert(AlertType.INFORMATION);
		printAlert.setTitle("INFORMATION");
		printAlert.setHeaderText(description);
		printAlert.setContentText(printConfirm);
		printAlert.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lab = new MainLab();
		
		stats.loadLogg();

		setCellValues();

		loadComboboxes();

	}

	// ---------------- Auxillary methods ---------------------------

	private void loadComboboxes() {
		nameEntries.addAll(stats.updateName());
		Collections.sort(nameEntries);
		selectName.setItems(nameEntries);

		pathogenEntries.addAll(stats.updatePathogen());
		Collections.sort(pathogenEntries);
		selectPathogen.setItems(pathogenEntries);

		labEntries.addAll(stats.updateLab());
		Collections.sort(labEntries);
		selectLab.setItems(labEntries);
	}

	private void setCellValues() {
		name.setCellValueFactory(new PropertyValueFactory<StatsTable, String>("name"));
		pathogen.setCellValueFactory(new PropertyValueFactory<StatsTable, String>("pathogen"));
		laboratory.setCellValueFactory(new PropertyValueFactory<StatsTable, String>("laboratory"));
		logInDate.setCellValueFactory(new PropertyValueFactory<StatsTable, String>("logInDate"));
		logOutDate.setCellValueFactory(new PropertyValueFactory<StatsTable, String>("logOutDate"));
	}

	private void chooseSaveDir(FileChooser _fileChooser) {
		
		_fileChooser.setTitle("Save File");

		if (userDir == null) {
			userDir = new File(System.getProperty("user.home"));
		}
			
		if (!userDir.canRead()) {
			userDir = new File("c:/");
		}
		
		_fileChooser.setInitialDirectory(userDir);

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		_fileChooser.getExtensionFilters().add(extFilter);
	}

	/**
	 * Overloaded method that takes number of entries and multiply it with the
	 * constant value COST. Set total cost in TextArea display.
	 * 
	 * @param entry
	 */
	private void cost(int entry) {
		display.clear();
		int total = entry * COST;
		display.appendText("Number of entries: " + entry);
		display.appendText("\nTotal cost: " + total + " kr" + "\n");

		for (String string : stats.individualCost(COST)) {
			display.appendText("\n" + string + " kr");
		}
	}

	/**
	 * Overload cost method and adds a extra cost for Lab5 use.
	 * 
	 * @param entry
	 * @param lab5
	 */
	private void cost(int entry, int lab5) {
		display.clear();
		int total = entry * COST;
		for (StatsTable st : tableEntries) {
			if (st.getLaboratory().equals("Lab 5"))
				
				total += lab5;
		}
		display.appendText("Number of entries: " + entry);
		display.appendText("\nTotal cost: " + total + " kr" + "\n");
		
		for (String string : stats.individualCost(COST, lab5)) {
			display.appendText("\n" + string + " kr");
		}
	}
	

	// Getters and setters
	/**
	 * @return the stats
	 */
	public Statistic getStats() {
		return stats;
	}

	/**
	 * @param stats
	 *            the stats to set
	 */
	public void setStats(Statistic statistics) {
		stats = statistics;
	}
}
