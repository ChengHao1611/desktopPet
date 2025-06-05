package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import pet.PetWindow;

public class Controller {

	@FXML
    private Button startButton, newButton, editButton, clearButton;
    @FXML
    private ListView<CheckBox> selectDesktopPet;
    @FXML
    private ListView<RadioButton> listViewForRadioButton;
    @FXML
    private TextField newFolderName;
    
    private List<PetWindow> pets = new ArrayList<>(); // every pets
    private Set<String> petNames = new HashSet<>(); // to avoid generating same pet.
    private boolean hasAlerted = false;
    private ToggleGroup group = new ToggleGroup(); // group radio buttons
    private String selectedPetName = "";

    @FXML
    public void startButtonClicked(ActionEvent e) {
    	for (CheckBox checkbox : selectDesktopPet.getItems()) {
			if (checkbox.isSelected()) {
				File petDir = new File("src/image/" + checkbox.getText() + "/walk");
				if (petDir.isDirectory() && petDir.list() != null && petDir.list().length > 0) {
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
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("警告");
					alert.setHeaderText("注意！");
					alert.setContentText("該桌寵資料夾不存在或資料夾內沒有圖片！");
					alert.show();
				}
			}
    	}
    	hasAlerted = false; // reset alert flag after checking all checkBoxes
    }
    
    @FXML
    public void refreshListView() {
    	// build up the checkBoxList in summon page
    	ObservableList<CheckBox> tmpList = FXCollections.observableArrayList();
    	ObservableList<RadioButton> tmpRadioButtonList = FXCollections.observableArrayList();
    	
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
    		tmpRadioButtonList.add(radioButton);
    	}
    	selectDesktopPet.setItems(tmpList);
    	listViewForRadioButton.setItems(tmpRadioButtonList);
    }
    
    @FXML
    public void initialize() {
    	refreshListView();
    	
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
    			selectedPetName = selectedRadioButton.getText();
    		}
    	});
    	editButton.setDisable(true); // initialize editButton state
    	clearButton.setDisable(true); // initialize clearButton state
    	
    	// if there is no folder name in the textField, it will disable to push the newButton
    	newButton.disableProperty().bind(Bindings.createBooleanBinding(
    		    () -> newFolderName.getText().trim().isEmpty(),
    		    newFolderName.textProperty()
    		));
    }
    
    @FXML
    public void clearButtonClicked(ActionEvent e) {
    	// clear the selected radio button
    	group.selectToggle(null);
    }
    
    public void openFolder(String folderName) {
    	try { // open the folder scene
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("EditScene.fxml"));
        	Parent root = loader.load();
        	Scene scene = new Scene(root);
        	Stage stage = new Stage();
        	stage.setScene(scene);
        	stage.setTitle(folderName);
        	stage.show();
    	} catch (Exception e) {
			e.printStackTrace();
    	}
    }
    
    @FXML
    public void editButtonClicked(ActionEvent e) {
    	openFolder(selectedPetName);
    }
    
    @FXML
    public void newButtonClicked(ActionEvent e) {
    	String folderName = newFolderName.getText().trim();
    	File newFolder = new File("src/image/" + folderName);
    	if (!newFolder.exists()) {
    		// build folders
    		File walkDir = new File("src/image/" + folderName + "/walk");
        	File climbDir = new File("src/image/" + folderName + "/climb");
        	File dragDir = new File("src/image/" + folderName + "/drag");
        	File fallDir = new File("src/image/" + folderName + "/fall");
        	File idleDir = new File("src/image/" + folderName + "/idle");
        	File sitDir = new File("src/image/" + folderName + "/sit");
        	File stumbleDir = new File("src/image/" + folderName + "/stumble");
        	File suspensionDir = new File("src/image/" + folderName + "/suspension");
        	
        	walkDir.mkdirs();
        	climbDir.mkdirs();
        	dragDir.mkdirs();
        	fallDir.mkdirs();
        	idleDir.mkdirs();
        	sitDir.mkdirs();
        	stumbleDir.mkdirs();
        	suspensionDir.mkdirs();
        	refreshListView(); // refresh VBox and listView

            openFolder(folderName);
    	} else {
    		Alert alert = new Alert(AlertType.WARNING);
		    alert.setTitle("警告");
		    alert.setHeaderText("注意！");
		    alert.setContentText("該名稱之桌寵已存在！");
		    alert.show();
    	}
    	newFolderName.clear(); // clear the text field after creating folder
    }
}
