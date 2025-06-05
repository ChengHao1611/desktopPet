package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ActionController {
	@FXML
	private Button newButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button clearButton;
	@FXML
	private ListView<RadioButton> images;
	
	private ToggleGroup group = new ToggleGroup();
	private String selectedImage = "";
	private Stage stage;
	private String currentFolderName = "";
	
	public void refreshListView() {
		ObservableList<RadioButton> tmpList = FXCollections.observableArrayList();
		
		Platform.runLater(() ->{
			stage = (Stage) newButton.getScene().getWindow();
			currentFolderName = "src/image/" + stage.getTitle();
			File mainDir = new File(currentFolderName);
			File[] subDirs = mainDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
			if (subDirs != null && subDirs.length != 0) {
				for (File folder : subDirs) {
					RadioButton radioButton = new RadioButton(folder.getName().replaceAll(".png$", ""));
					radioButton.setToggleGroup(group);
					tmpList.add(radioButton);
				}
			}
			images.setItems(tmpList);
    	});
	}
	
	@FXML
	public void newButtonClicked(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("選擇圖片");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG 圖片", "*.png"));
		
		File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                Path targetDirectory = Path.of(currentFolderName); 
                // change the file name
                File folder = new File(currentFolderName);
                File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
                int count;
                if (files != null) {
                	count = files.length;
                }
                else {
                	count = 0;
                }
                Path targetPath = targetDirectory.resolve((count + 1) + ".png");
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING); // copy file to the destination
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        refreshListView();
	}
	
	@FXML
	public void deleteButtonClicked(ActionEvent e) {
		File targetFile = new File(currentFolderName, selectedImage + ".png");
		targetFile.delete(); // delete the selected image file
		
		// refresh the number of files in the folder
		int num = Integer.parseInt(selectedImage);
		while(true) {
			File oldFile = new File(currentFolderName, (num + 1) + ".png");
			File newFile = new File(currentFolderName, num + ".png");
			if (!oldFile.exists()) break;
			try {
				System.out.println("1");
				Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		group.selectToggle(null);
		refreshListView(); // refresh the list view to reflect the changes
	}
	
	@FXML
	public void clearButtonClicked(ActionEvent e) {
		// clear the selected action
		group.selectToggle(null);
	}
	
	@FXML
	public void initialize() {
		refreshListView();
		
		// the listener for radio buttons
		group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
    		deleteButton.setDisable(newValue == null);
    		clearButton.setDisable(newValue == null);
    		if (newValue != null) {
    			RadioButton selectedRadioButton = (RadioButton) newValue;
    			selectedImage = selectedRadioButton.getText();
    		}
    	});
		clearButton.setDisable(true); // initialize clearButton state
		deleteButton.setDisable(true); // initialize deleteButton state
	}
}