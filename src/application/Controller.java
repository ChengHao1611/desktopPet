package application;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button startButton;

    public void startButtonClicked(ActionEvent e) {
    	Stage startStage = (Stage) startButton.getScene().getWindow();
    	startStage.close();
    	System.out.println("Start button clicked, closing the start window.");
    }
}
