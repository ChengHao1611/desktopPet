package pet;


import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PetController {
	private PetStage petStage;
	private ImageView petImage;
	private Stage petWindow; // 寵物狀態
	private String petName;
	private AnimationTimer animationTimer; // 動畫計時器
	private boolean nextStage = true; // 是否切換到下一個狀態
	
	public PetController(ImageView petImage, Stage petWindow, String petName) {
		this.petStage = PetStage.WALK; // 初始狀態為 WALK
		this.petImage = petImage;
		this.petWindow = petWindow;
		this.petName = petName;
		
		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				petStage = PetStage.WALK; // 取得狀態
				if(!nextStage) return;
				nextStage = false; // 重置為 false，等待下一次觸發
				
				switch (petStage) {
					case WALK:
						petWalk();
						break;
					case CLIMB:
						petImage.setImage(new Image(
							PetController.class.getResource("/image/"+petName+"/climb/1.png").toExternalForm()
						));
						break;
					case SUSPENSION:
						petImage.setImage(new Image(
							PetController.class.getResource("/image/"+petName+"/suspension/1.png").toExternalForm()
						));
						break;
					case FALL:
						petImage.setImage(new Image(
							PetController.class.getResource("/image/"+petName+"/fall/1.png").toExternalForm()
						));
						break;
					case STUMBLE:
						petImage.setImage(new Image(
							PetController.class.getResource("/image/"+petName+"/stumble/1.png").toExternalForm()
						));
						break;
					case SIT:
						petImage.setImage(new Image(
							PetController.class.getResource("/image/"+petName+"/sit/1.png").toExternalForm()
						));
						break;
					case DRAG:
						petImage.setImage(new Image(
							PetController.class.getResource("/image/"+petName+"/drag/1.png").toExternalForm()
						));
						break;
					case IDLE:
						petImage.setImage(new Image(
							PetController.class.getResource("/image/"+petName+"/idle/1.png").toExternalForm()
						));
						break;
				}
			}
		};
	}

	public void start() {
		animationTimer.start(); // 啟動動畫計時器
	}

	private void petWalk() {
	    // 建立 Timeline 動畫
		Timeline timeline = new Timeline();
	    for (int i = 0; i < 4; i++) {
	        final int frameIndex = i + 1;
	        timeline.getKeyFrames().add(new KeyFrame(
	            Duration.millis(200 * (frameIndex - 1)), // 在200 * (frameIndex-1)毫秒時執行event
	            event -> {  
	                petImage.setImage(new Image(
	                    PetController.class.getResource("/image/" + petName + "/walk/" + frameIndex + ".png").toExternalForm()
	                ));
	                petWindow.setX(petWindow.getX() - 2); // 每次移動1像素
	            }
	        ));
	    }
	    timeline.setCycleCount(1); // 只執行一次
	    timeline.setOnFinished(e -> {
	        nextStage = true; // ✅動畫播放完畢後才允許進入下一階段
	    });
	    timeline.play(); // 開始動畫
	}

}
