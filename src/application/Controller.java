package application;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import pet.PetWindow;

public class Controller {

	@FXML
    private Button startButton;
    @FXML
    private ListView<checkBox> selectDesktopPet;

    @FXML
    public void startButtonClicked(ActionEvent e) {
    	// build the pet scene
    	Stage startStage = (Stage) startButton.getScene().getWindow();
    	//startStage.close();
    	PetWindow petWindow = new PetWindow();
    	Thread petThread = new Thread(petWindow);
    	petThread.start(); // 啟動桌寵視窗執行緒
    }
    
    @FXML
    public void initialize() {
    	// build up the checkBoxList
    	ObservableList<checkBox> tmpList = FXCollections.observableArrayList();
    	
    	File mainDir = new File("../image");
    	File[] subDirs = mainDir.listFiles(File::isDirectory);
    	if (subDirs == null) {
    		System.out.println("該路徑下並不存在資料夾");
    		return;
    	}
    	for (File folder : subDirs) {
    		tmpList.add(new checkBox(foler.getName()))
    	}
    	selectDesktopPet.setItems(tmpList);
    }
}
