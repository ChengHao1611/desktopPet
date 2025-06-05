package application;

import java.io.File;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class EditSceneController {
	@FXML
	private Button editButton, clearButton;
	@FXML
	private ListView<RadioButton> actionsFolders;
	
	private ToggleGroup group = new ToggleGroup();
	private String selectedAction = "";
	private String currentFolderName = "";
	
	@FXML
	public void clearButtonClicked(ActionEvent e) {
		// clear the selected action
		group.selectToggle(null);
	}
	
	@FXML
	public void editButtonClicked(ActionEvent e) {
		try { // open the folder scene
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("ActionScene.fxml"));
        	Parent root = loader.load();
        	Scene scene = new Scene(root);
        	Stage stage = new Stage();
        	stage.setScene(scene);
        	stage.setTitle(currentFolderName + "/" + selectedAction);
        	stage.show();
    	} catch (Exception ex) {
			ex.printStackTrace();
    	}
	}
	
	@FXML
	public void initialize() {
		// initialize the listView with radio buttons
		ObservableList<RadioButton> tmpList = FXCollections.observableArrayList();
		
		Platform.runLater(() ->{
			Stage stage = (Stage) editButton.getScene().getWindow();
			currentFolderName = stage.getTitle();
			File mainDir = new File("src/image/" + currentFolderName);
			File[] subDirs = mainDir.listFiles(File::isDirectory);
			if (subDirs == null) {
				System.out.println("該路徑下並不存在資料夾");
				return;
			}
			for (File folder : subDirs) {
				RadioButton radioButton = new RadioButton(folder.getName());
				radioButton.setToggleGroup(group);
				tmpList.add(radioButton);
			}
			actionsFolders.setItems(tmpList);
    	});
		
		// the listener for radio buttons
		group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
    		editButton.setDisable(newValue == null);
    		clearButton.setDisable(newValue == null);
    		if (newValue != null) {
    			RadioButton selectedRadioButton = (RadioButton) newValue;
    			selectedAction = selectedRadioButton.getText();
    		}
    	});
		editButton.setDisable(true); // initialize editButton state
		clearButton.setDisable(true); // initialize clearButton state
	}
	
}