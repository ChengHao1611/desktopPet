package application;

import javafx.fxml.FXML;
import havafx.event.ActionEvent;
import javafx.scene.control.Button;

public class Controller {

    @FXML
    private Button startButton;

    public void startButtonClicked(ActionEvent e) {
    	Stage startStage = (Stage) startButton.getScene().getWindow();
    	startStage.close();
    	// TODO - show pet scene
    }
}
