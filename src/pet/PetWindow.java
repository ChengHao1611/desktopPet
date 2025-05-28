package pet;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PetWindow {
	private PetController petController; // 寵物控制器
	
    public void show() {
        Platform.runLater(() -> {
            // 建立寵物圖片
            ImageView petImage = new ImageView(new Image(
                PetWindow.class.getResource("/image/pikachu/walk/1.png").toExternalForm()
            ));
            petImage.setPreserveRatio(true); //?
            petImage.setFitWidth(120);//?
            

            // 建立畫面與背景透明
            StackPane root = new StackPane(petImage); //?
            root.setStyle("-fx-background-color: transparent;"); // 設定背景透明
            Scene scene = new Scene(root); //?
            scene.setFill(Color.TRANSPARENT); // 設定背景透明

            // 建立視窗
            Stage stage = new Stage(); //?
            stage.initStyle(StageStyle.TRANSPARENT); // 無邊框透明
            stage.setAlwaysOnTop(true);              // 永遠在最上層
            stage.setScene(scene);

            // 顯示在螢幕右下角
            double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
            double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
            stage.setX(screenWidth - 150);
            stage.setY(screenHeight - 180);

            petController = new PetController(petImage); // 初始化寵物控制器

            
            stage.show();
        });
    }
}

