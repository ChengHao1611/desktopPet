package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pet.StageController;
import javafx.animation.AnimationTimer;

public class TestStageController extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 顯示用 Label
        Label statusLabel = new Label("Initializing...");
        StackPane root = new StackPane(statusLabel);
        Scene scene = new Scene(root, 200, 100);

        // 透明無邊框視窗，模擬桌寵
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.show();

        // 控制器與拖曳器
        StageController controller = new StageController(primaryStage);
        controller.start(); // 啟動狀態更新邏輯

        // 每幀更新文字（顯示目前狀態）
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                String status = "Stage: " + controller.getCurrentStage();
                statusLabel.setText(status);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
