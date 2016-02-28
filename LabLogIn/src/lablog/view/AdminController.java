package lablog.view;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import lablog.MainLab;
import lablog.model.Administrator;

public class AdminController implements Initializable {

	private MainLab lab;
	static Administrator admin;


	@FXML Button setMessage;
	@FXML Button exit;
	@FXML Button changeMode;
	@FXML Button confirm;
	@FXML Button clear;
	
	@FXML CheckBox checkBox;
	
	@FXML RadioButton virusButton;
	@FXML RadioButton bacteriaButton;
	
	@FXML TextArea adminInfotext;

	@FXML TextField fieldOne;
	@FXML TextField fieldTwo;
	@FXML TextField fieldThree;

	@FXML Label adminLabel;
	
	@FXML ComboBox<String> list;
	
	ObservableList<String> entries = FXCollections.observableArrayList();
	ToggleGroup tg;

	@FXML
	private void changeToStatisticsMode() {
		lab.changeStage("Statsview");
	}
	
	@FXML
	private void exitAdmin() {
		admin.exitAdmin();

		admin = null;
		lab.closeStage();
		lab.initRootLayout();
	}
	
	@FXML
	private void addText() {
		admin.saveInfoText(adminInfotext.getText());
	}
	
	@FXML
	private void clearText() {
		adminInfotext.clear();
	}

	// ------------------------- User events ------------------------------
	@FXML
	private void addUser() {
		defaultVisibility();
		addEditUserVisibility();
		adminLabel.setText("Add new researcher");

		confirm.setOnAction(event -> {
			if (checkBox.isSelected()) {
				fieldThree.setText(checkAdmin((fieldOne.getText() +" "+ fieldTwo.getText())));
			}
			admin.addUser(fieldOne.getText(), fieldTwo.getText(), fieldThree.getText());
			defaultVisibility();
		});
	}

	@FXML
	private void removeUser() {
		removeVisibility("Researcher");
		confirm.setOnAction(event -> {
			admin.removeResearcher(list.getValue());
			admin.removeAdministrator(list.getValue());
			defaultVisibility();
		});
	}
	
	@FXML
	private void editUser() {
		defaultVisibility();
		addEditUserVisibility();
		list.setVisible(true);
		list.setPromptText("Choose Researcher");
		updateList("researcher");
		adminLabel.setText("Edit researcher");

		list.setOnAction(event -> {
			checkBox.setSelected(false);
			fieldOne.setText(admin.getResearchers().get(list.getValue()).getName());
			fieldTwo.setText(admin.getResearchers().get(list.getValue()).getLastName());
			fieldThree.setText(admin.getResearchers().get(list.getValue()).getUserData());
			if (admin.getAdministrators().containsKey(list.getValue())) {
				checkBox.setSelected(true);
			}
		});

		String currentAdmin = admin.getName() + " " + admin.getLastName();
		if (!currentAdmin.equals(list.getValue())) {

			confirm.setOnAction(confirmEvent -> {
				String password = "";
				if (!checkBox.isSelected() && admin.getAdministrators().containsKey(list.getValue())) {
					admin.removeAdministrator(list.getValue());
				}

				if (checkBox.isSelected()) {
					password = checkAdmin(list.getValue());
				}
				admin.editUser(list.getValue(), fieldOne.getText(), fieldTwo.getText(), fieldThree.getText(), password,
						checkBox.isSelected());
				defaultVisibility();
			});
		}
	}

	@FXML
	private void changePasswordButton() {
		defaultVisibility();
		adminLabel.setVisible(true);
		fieldOne.setVisible(true);
		confirm.setVisible(true);
		clear.setVisible(true);
		adminLabel.setText("Change Password");
		fieldOne.setPromptText("Enter new password");
		confirm.setOnAction(event -> {
			admin.changePassword(fieldOne.getText());
			defaultVisibility();
		});
	}

	// ------------------------- Pathogen events ------------------------------
	
	@FXML
	private void addPathogen() {
		defaultVisibility();
		addEditPathogenVisibility();
		adminLabel.setText("Add new pathogen");
		virusButton.setToggleGroup(tg);
		bacteriaButton.setToggleGroup(tg);
		checkRadioButtons();
		
		confirm.setOnAction(event -> {
			String[] tgValue = tg.getSelectedToggle().toString().split("[']");
			admin.addPathogen(fieldOne.getText(), tgValue[1].toLowerCase(),
					admin.checkInt(fieldTwo.getText()), checkBox.isSelected());
			defaultVisibility();
		});
	}

	@FXML
	private void removePathogen() {
		removeVisibility("Pathogen");		
		confirm.setOnAction(event -> {
			admin.removePathogen(list.getValue());
			defaultVisibility();
		});
	}

	@FXML
	private void editPathogen() {
		defaultVisibility();
		addEditPathogenVisibility();
		list.setVisible(true);
		list.setPromptText("Choose Pathogen");
		adminLabel.setText("Edit pathogen");
		updateList("pathogen");
		
		list.setOnAction(event -> {
			fieldOne.setText(admin.getPathogens().get(list.getValue()).getName());
			fieldTwo.setText(("" + admin.getPathogens().get(list.getValue()).getCfr()));
			
			if (admin.getPathogens().get(list.getValue()).getType().equals("virus")) {
				virusButton.setSelected(true);
			} else {
				bacteriaButton.setSelected(true);
			}
			
			virusButton.setDisable(true);
			bacteriaButton.setDisable(true);
			
			if (admin.checkCharacteristic(list.getValue()))
				checkBox.setSelected(true);
			else
				checkBox.setSelected(false);
		});
		
		checkRadioButtons();
				
		confirm.setOnAction(event -> {
			admin.editPathogen(list.getValue(), fieldOne.getText(), admin.checkInt(fieldTwo.getText()),
					checkBox.isSelected());
			defaultVisibility();
		});
	}

	// ------------------------- Laboratory events ------------------------------
	
	@FXML
	private void addLab() {
		addEditLaboratoryVisibility();
		adminLabel.setText("Add Laboratory");
		list.setVisible(false);
		confirm.setOnAction(event -> {
			admin.addLab(fieldOne.getText(), admin.checkInt(fieldTwo.getText()));
			defaultVisibility();
		});
	}
	
	@FXML
	private void removeLab() {
		removeVisibility("Laboratory");		
		confirm.setOnAction(event -> {
			admin.getLaboratories().remove(list.getValue());
			defaultVisibility();
		});
	}
	
	@FXML
	private void editLab() {
		addEditLaboratoryVisibility();
		adminLabel.setText("Edit Laboratory");
		list.setPromptText("Choose Lab");
		updateList("laboratory");
		
		list.setOnAction(event -> {
			fieldOne.setText(admin.getLaboratories().get(list.getValue()).getName());
			fieldTwo.setText(("" + admin.getLaboratories().get(list.getValue()).getSecurityLevel()));
		});
		
		confirm.setOnAction(event -> {
			admin.editLab(fieldOne.getText(), admin.checkInt(fieldTwo.getText()));
			defaultVisibility();
		});
	}

	@FXML
	private void clear() {
		fieldOne.clear();
		fieldTwo.clear();
		fieldThree.clear();
		tg.selectToggle(null);
		checkBox.setSelected(false);
		list.setPromptText("Choose value");
	}
	
	// ------------------------ Print methods -----------------------------------------
		
	@FXML
	private void printUsers() {
		String description = "Registered reseachers printed";
		updateList("researcher");
		printerDialog(description);
	}
	
	@FXML
	private void printPathogens() {
		String description = "Pathogens printed";
		updateList("pathogen");
		printerDialog(description);
	}
	
	@FXML
	private void printLaboratories() {
		String description = "Laboratories printed";
		updateList("laboratory");
		printerDialog(description);
	}
	
	// ------------------------ Initializes Administration view ---------------------------------
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lab = new MainLab();
		
		adminInfotext.setText(admin.loadInfoText());
		
		tg = new ToggleGroup();		
	}

	// ------------------------- Auxillary methods ------------------------------
	
	private void updateList(String fileName) {
		entries.clear();
		if (fileName.equals("researcher"))
			entries.addAll(admin.getResearchers().keySet());
		
		else if (fileName.equals("pathogen"))
			entries.addAll(admin.getPathogens().keySet());
		
		else if (fileName.equals("laboratory"))
			entries.addAll(admin.getLaboratories().keySet());
		
		list.setItems(entries);
	}
	
	private boolean checkPassword() {
		TextInputDialog passwordDialog = new TextInputDialog("Enter Password");
		Optional<String> password = passwordDialog.showAndWait();
		if (password.isPresent()) {
			if (password.equals(admin.getUserData()))
				return true;
		}
		return false;
	}
	
	private void checkRadioButtons() {
		tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (virusButton.isSelected())
					checkBox.setText("Is Enveloped");

				else if (bacteriaButton.isSelected())
					checkBox.setText("Has Spores");
			}
		});
	}
	
	private String checkAdmin(String name) {
		checkPassword();
		TextInputDialog passwordDialog;
		
		if (!admin.getAdministrators().containsKey(name)) {
			String pw = "" + (int)Math.random()*1000;
			admin.addAdministrator(name, pw);
		}
		
		passwordDialog = new TextInputDialog(admin.getAdministrators().get(name).getUserData());
		
		passwordDialog.setHeaderText("Change password for " + name);
		passwordDialog.setContentText("Current password");
		return passwordDialog.showAndWait().get();
	}

	
	private void printerDialog(String _description) {
		String printConfirm = admin.printToServer(_description, entries);
		Alert logInAlert = new Alert(AlertType.INFORMATION);
		logInAlert.setTitle("INFORMATION");
		logInAlert.setHeaderText(_description);
		logInAlert.setContentText(printConfirm);
		logInAlert.showAndWait();
	}
	
	// ------------------------ Visibility methods -------------------------------
	
	private void defaultVisibility() {
		fieldOne.clear();
		fieldTwo.clear();
		fieldThree.clear();
		checkBox.setText("Value");
		adminLabel.setVisible(false);
		fieldOne.setVisible(false);
		fieldTwo.setVisible(false);
		fieldThree.setVisible(false);
		confirm.setVisible(false);
		clear.setVisible(false);
		list.setVisible(false);
		checkBox.setVisible(false);
		checkBox.setSelected(false);
		bacteriaButton.setVisible(false);
		bacteriaButton.setSelected(false);
		virusButton.setVisible(false);
		virusButton.setSelected(false);

		list.setOnAction(null);
	}
	
	private void addEditUserVisibility() {
		defaultVisibility();
		adminLabel.setVisible(true);
		fieldOne.setVisible(true);
		fieldTwo.setVisible(true);
		fieldThree.setVisible(true);
		confirm.setVisible(true);
		clear.setVisible(true);
		checkBox.setVisible(true);
		adminLabel.setText("Edit researcher");
		fieldOne.setPromptText("First name");
		fieldTwo.setPromptText("Last name");
		fieldThree.setPromptText("Department");
		checkBox.setText("Administrator");
	}
	
	private void addEditPathogenVisibility() {
		defaultVisibility();
		
		adminLabel.setVisible(true);
		fieldOne.setVisible(true);
		fieldTwo.setVisible(true);
		confirm.setVisible(true);
		clear.setVisible(true);
		virusButton.setVisible(true);
		virusButton.setDisable(false);
		bacteriaButton.setVisible(true);
		bacteriaButton.setDisable(false);
		checkBox.setVisible(true);
		fieldOne.setPromptText("Name");
		fieldTwo.setPromptText("CFR-value");
		virusButton.setToggleGroup(tg);
		bacteriaButton.setToggleGroup(tg);
		checkBox.setText("");
		tg.selectToggle(null);
	}
	
	private void addEditLaboratoryVisibility() {
		defaultVisibility();
		
		adminLabel.setVisible(true);
		list.setVisible(true);
		fieldOne.setVisible(true);
		fieldTwo.setVisible(true);
		confirm.setVisible(true);
		clear.setVisible(true);
		fieldOne.setPromptText("Name");
		fieldTwo.setPromptText("Safty Level");
	}
	
	private void removeVisibility(String type) {
		defaultVisibility();
		adminLabel.setVisible(true);
		adminLabel.setText("Remove " + type);
		list.setVisible(true);
		list.setPromptText("Choose " + type);
		confirm.setVisible(true);
		updateList(type.toLowerCase());
	}
	
	public Administrator getAdmin() {
		return admin;
	}
	
	public void setAdmin(Administrator administrator) {
		admin = administrator;
	}
}
