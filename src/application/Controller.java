package application;

import javafx.fxml.FXML;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

    @FXML
    public void startButtonClicked(ActionEvent e) {
    	for (CheckBox checkbox : selectDesktopPet.getItems()) {
			if (checkbox.isSelected()) {
				PetWindow petWindow = new PetWindow(checkbox.getText()); // 取得選中的寵物名稱
				Thread petThread = new Thread(petWindow);
				petThread.start(); // 啟動桌寵視窗執行緒
				pets.add(petWindow); // add the pet into list
			}
    	}
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
    }
}
