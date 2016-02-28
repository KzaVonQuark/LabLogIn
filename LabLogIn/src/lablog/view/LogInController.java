package lablog.view;

import lablog.MainLab;
import lablog.model.LogIn;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;

public class LogInController  implements Initializable {

	private MainLab lab;
	private LogIn model;
	
	private ObservableList<String> nameEntries = FXCollections.observableArrayList();
	private ObservableList<String> listEntries = FXCollections.observableArrayList();
	private ObservableList<String> pathogenEntries = FXCollections.observableArrayList();
	private ObservableList<String> labEntries = FXCollections.observableArrayList();

	@FXML private ImageView imageView;
	
	@FXML private Button administrateButton;
	@FXML private Button logIn;
	@FXML private Button logOut;

	@FXML private ComboBox<String> selectName;
	@FXML private ComboBox<String> selectPathogen;
	@FXML private ComboBox<String> selectLab;

	@FXML private ListView<String> loggedInList;
	
	@FXML TextArea infotext;
	
	@FXML
	private void handleLogIn() {
		boolean checkName = true;
		for (String string : loggedInList.getItems()) {
			if (string.contains(selectName.getValue())) {
				checkName = false;
				String selected = selectName.getValue();
				Alert logInAlert = new Alert(AlertType.INFORMATION);
				logInAlert.setTitle("INFORMATION");
				logInAlert.setHeaderText(selected + " already logged in.");
				logInAlert.setContentText("Logg out before proceding.");
				logInAlert.showAndWait();
			}
		}
		if (checkName)
			listEntries.add(model.listResearcher(selectName.getValue(), selectPathogen.getValue(), selectLab.getValue())
					.toString());
		defaultVisibility();
	}

	@FXML
	private void handleLogOut() {
		String selected = loggedInList.getSelectionModel().getSelectedItem();
		Alert logOutAlert = new Alert(AlertType.CONFIRMATION);
		logOutAlert.setTitle("Log out");
		logOutAlert.setHeaderText(selected);
		logOutAlert.setContentText("Confirm Log out");
		Optional<ButtonType> confirm = logOutAlert.showAndWait();
		if (confirm.get() == ButtonType.OK) {
			if (!listEntries.isEmpty()) {
				listEntries.remove(selected);
				model.logOut(selected);
			}
		}
	}

	@FXML
	private void handleSelectName() {
		selectLab.setVisible(false);
		administrateButton.setVisible(false);
		if (model.loadLogInFields("administrator").contains(selectName.getValue())) {
			administrateButton.setVisible(true);
		} else if (!model.loadLogInFields("administrator").contains(selectName.getValue())) {
			administrateButton.setVisible(false);
		}
		selectPathogen.setVisible(true);
	}

	@FXML
	private void handleSelectPathogen() {
		selectLab.setVisible(true);
	}

	@FXML
	private void handleSelectLaboratory() {
		logIn.setDisable(false);
	}

	@FXML
	private void handleAdminButton() {
		if (lab.getNewStage().isShowing()) {
			Alert adminAlert = new Alert(AlertType.WARNING);
			adminAlert.setTitle("Administrator Logged in");
			adminAlert.setHeaderText("Administrator already logged in");
			defaultVisibility();
			adminAlert.show();
			lab.closeStage();
		} else {
			TextInputDialog passwordDialog = new TextInputDialog("Enter Password");
			passwordDialog.setHeaderText(selectName.getValue());
			Optional<String> password = passwordDialog.showAndWait();

			if (password.isPresent()) {
				if (model.createAdmin(selectName.getValue(), password.get())) {
					selectPathogen.setVisible(false);
					selectLab.setVisible(false);
					model.saveLoggedIn(listEntries);
					lab.changeStage("AdministrationView");
				} else
					defaultVisibility();
			}
		}
	}

	@FXML
	private void emergencyPrintOut() {
		String description = "Emergency print out made: " + new Date();
		String printConfirm = model.printToServer(description, listEntries);
		Alert logInAlert = new Alert(AlertType.INFORMATION);
		logInAlert.setTitle("INFORMATION");
		logInAlert.setHeaderText(description);
		logInAlert.setContentText(printConfirm);
		logInAlert.showAndWait();
	}

	private void defaultVisibility() {
		selectName.setValue("Select Name");
		selectPathogen.setValue("Select Pathogen");
		selectLab.setValue("Select Lab");
		logIn.setDisable(true);
		selectPathogen.setVisible(false);
		selectLab.setVisible(false);
		administrateButton.setVisible(false);
	}

	// ----------------- Initializes Log in view (RootLayout) ---------------------------------

	/**
	 * Load data to comboboxes (name, pathogen and lab).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lab = new MainLab();
		model = new LogIn();

		nameEntries.addAll(model.loadLogInFields("researcher"));
		Collections.sort(nameEntries);
		selectName.setItems(nameEntries);

		pathogenEntries.addAll(model.loadLogInFields("bacteria"));
		pathogenEntries.addAll(model.loadLogInFields("virus"));
		Collections.sort(pathogenEntries);
		selectPathogen.setItems(pathogenEntries);

		labEntries.addAll(model.loadLogInFields("laboratory"));
		Collections.sort(labEntries);
		selectLab.setItems(labEntries);

		if (new File("src/files/loggedin.txt").exists())
			listEntries.addAll(model.loadLoggedIn());
		loggedInList.setItems(listEntries);

		infotext.setText(model.loadInfoText());

	}
}
