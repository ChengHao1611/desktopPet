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
import javafx.stage.Stage;
import pet.PetWindow;

public class Controller {

	@FXML
    private Button startButton;
    @FXML
    private ListView<CheckBox> selectDesktopPet;
    private List<PetWindow> pets = new ArrayList<>(); // every pets
    private Set<String> petNames = new HashSet<>(); // to avoid generating same pet.
    private boolean hasAlerted = false;

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
    	hasAlerted = false; // reset alert flag after checking all checkboxes
    }
    
    @FXML
    public void initialize() {
    	// build up the checkBoxList
    	ObservableList<CheckBox> tmpList = FXCollections.observableArrayList();
    	
    	File mainDir = new File("src/image");
    	File[] subDirs = mainDir.listFiles(File::isDirectory);
    	if (subDirs == null) {
    		System.out.println("該路徑下並不存在資料夾");
    		return;
    	}
    	for (File folder : subDirs) {
    		tmpList.add(new CheckBox(folder.getName()));
    	}
    	selectDesktopPet.setItems(tmpList);
    	
    	// creating a persistent listener
    	Platform.runLater(() ->{
    		Stage stage = (Stage) startButton.getScene().getWindow();
        	stage.setOnCloseRequest(event -> {
        		for (PetWindow pet : pets) {
        			pet.petWindowclose();
        		}
        	});
    	});
    }
}
