package application;

import javafx.fxml.FXML;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pet.PetWindow;

public class Controller {

	@FXML
    private Button startButton, newButton, editButton, clearButton;
    @FXML
    private ListView<CheckBox> selectDesktopPet;
    @FXML
    private VBox VBox = new VBox(10);
    
    private List<PetWindow> pets = new ArrayList<>(); // every pets
    private Set<String> petNames = new HashSet<>(); // to avoid generating same pet.
    private boolean hasAlerted = false;
    private ToggleGroup group = new ToggleGroup(); // group radio buttons

    @FXML
    public void startButtonClicked(ActionEvent e) {
    	for (CheckBox checkbox : selectDesktopPet.getItems()) {
			if (checkbox.isSelected()) {
				PetWindow petWindow = new PetWindow(checkbox.getText()); // 取得選中的寵物名稱
				Thread petThread = new Thread(petWindow);
				if (!petNames.add(checkbox.getText()) && !hasAlerted) { // to avoid generating same pet. 
					Alert alert = new Alert(AlertType.WARNING);
				    alert.setTitle("警告");
				    alert.setHeaderText("注意！");
				    alert.setContentText("請勿重複添加相同桌寵！");
				    alert.show();
				    hasAlerted = true; // set alert flag to true to avoid multiple alerts
				} else {
					pets.add(petWindow); // add pet into list
					petThread.start(); // 啟動桌寵視窗執行緒
				}
			}
    	}
    	hasAlerted = false; // reset alert flag after checking all checkBoxes
    }
    
    @FXML
    public void initialize() {
    	// build up the checkBoxList in summon page
    	ObservableList<CheckBox> tmpList = FXCollections.observableArrayList();
    	
    	File mainDir = new File("src/image");
    	File[] subDirs = mainDir.listFiles(File::isDirectory);
    	if (subDirs == null) {
    		System.out.println("該路徑下並不存在資料夾");
    		return;
    	}
    	for (File folder : subDirs) {
    		tmpList.add(new CheckBox(folder.getName()));
    		RadioButton radioButton = new RadioButton(folder.getName());
    		radioButton.setToggleGroup(group);
    		VBox.getChildren().add(radioButton);
    	}
    	selectDesktopPet.setItems(tmpList);
    	
    	// a persistent listener to take actions when the window is closed
    	Platform.runLater(() ->{
    		Stage stage = (Stage) startButton.getScene().getWindow();
        	stage.setOnCloseRequest(event -> {
        		for (PetWindow pet : pets) {
        			pet.petWindowclose();
        		}
        	});
    	});
    	
    	// the listener for radio buttons
    	group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
    		editButton.setDisable(newValue == null);
    		clearButton.setDisable(newValue == null);
    		if (newValue != null) {
    			RadioButton selectedRadioButton = (RadioButton) newValue;
    			String selectedPetName = selectedRadioButton.getText();
    		}
    	});
    	editButton.setDisable(true); // initialize editButton state
    	clearButton.setDisable(true); // initialize clearButton state
    }
    
    @FXML
    public void clearButtonClicked(ActionEvent e) {
    	// clear the selected radio button
    	group.selectToggle(null);
    }
    
    public void editButtonClicked(ActionEvent e) {
    	
    }
    
    public void newButtonClicked(ActionEvent e) {
    	
    }
}
