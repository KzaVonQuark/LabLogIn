package lablog;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lablog.model.FileManager;

/**
 * This program is for logging when reseacher use of laboratories and what they work with.
 * It has a administration module for adding, removing and editing all entry data. There is also
 * a statistics module that is only accessible from the administration module.
 * 
 * The interface design is made on the basis of the customers requirements.
 * @author Linus Lundahl 
 */
public class MainLab extends Application {

	private static Stage primaryStage;
	private static Stage newStage = new Stage();
	private static BorderPane rootLayout;
	FileManager fm;

	@Override
	public void start(Stage _primaryStage) {
		primaryStage = _primaryStage;
		primaryStage.setTitle("Lab user registration");
		
		fm = FileManager.FM;
		fm.clearFile("loggedin"); //Clear the loggedin file before launching.
		
		initRootLayout();
	}

	/**
	 * Initializes the root layout (with the main interface).
	 */
	public void initRootLayout() {
		try {
			primaryStage.close();
			
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainLab.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Load a new window, from fxml-file, by creating a new stage and scene.
	 * If a new window is already showing it closes before showing the called one.  
	 */
	public void changeStage(String stageName) {
		if (newStage.isShowing())
			newStage.close();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainLab.class.getResource("view/" + stageName + ".fxml"));
		AnchorPane layout = null;

		try {
			layout = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene newScene = new Scene(layout);
		newStage.setScene(newScene);
		newStage.show();
		newStage.setResizable(false);
	}
	
	/**
	 * Close the new window (stage)
	 */
	public void closeStage() {
		newStage.close();
	}
 
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Returns the secondary stage (newStage).
     * @return
     */
	public Stage getNewStage() {
		return newStage;
	}


	/**
	 * @return the rootLayout
	 */
	public static BorderPane getRootLayout() {
		return rootLayout;
	}

	
    public static void main(String[] args) {
        launch(args);
    }
}



