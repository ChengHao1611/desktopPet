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
	private PetPictureNumber petPictureNumber; // 寵物圖片數量
	private StageController controller; // 狀態控制器
	
	public PetController(ImageView petImage, Stage petWindow, String petName) {
		this.petStage = PetStage.LEFT_WALK; // 初始狀態為 WALK
		this.petImage = petImage;
		this.petWindow = petWindow;
		this.petName = petName;
		this.petPictureNumber = new PetPictureNumber(petName); // 初始化寵物圖片數量
		controller = new StageController(petWindow); // 初始化狀態控制器
        controller.start(); // 啟動狀態更新邏輯
		
		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if(!nextStage) return;
				nextStage = false; // 重置為 false，等待下一次觸發
				petStage = controller.getCurrentStage(); // 更新狀態控制器的當前狀態
				System.out.println("Current Stage: " + petStage); // 印出目前狀態
				
				switch (petStage) {
					case LEFT_WALK:
						action(petPictureNumber.walk, "walk", -2, 0, 200); // 呼叫 petWalk 方法
						break;
					case LEFT_CLIMB_UP:
						action(petPictureNumber.climb, "climb", 0, -2, 200); // 呼叫 petClimb 方法
						break;
					case SUSPENSION:
						action(petPictureNumber.suspension, "suspension", 2, 0, 200); // 呼叫 petSuspension 方法
						break;
					case FALL:
						action(petPictureNumber.fall, "fall", 0, 2, 200); // 呼叫 petFall 方法
						break;
					case STUMBLE:
						action(petPictureNumber.stumble, "stumble", 0, 0, 200); // 呼叫 petStumble 方法
						break;
					case SIT:
						action(petPictureNumber.sit, "sit", 0, 0, 200); // 呼叫 petSit 方法
						break;
					case DRAG:
						action(petPictureNumber.drag, "drag", 0, 0, 200); // 呼叫 petDrag 方法
						break;
					case IDLE:
						action(petPictureNumber.idle, "idle", 0, 0, 700); // 呼叫 petIdle 方法
						break;
				}
			}
		};
	}

	public void start() {
		animationTimer.start(); // 啟動動畫計時器
	}
	
	public void stop() {
		animationTimer.stop(); // 停止動畫計時器
	}

	// x, y 參數用於調整寵物視窗位置, millis 參數用於設定每一幀的時間間隔
	private void action(int pictureNum, String state, int x, int y, int millis) {
	    // 建立 Timeline 動畫
		Timeline timeline = new Timeline();
	    for (int i = 0; i < pictureNum; i++) {
	        final int frameIndex = i + 1;
	        timeline.getKeyFrames().add(new KeyFrame(
	            Duration.millis(millis * (frameIndex - 1) + 1), // 在200 * (frameIndex-1)毫秒時執行event
	            event -> {  
	                petImage.setImage(new Image(
	                    PetController.class.getResource("/image/" + petName + "/" + state + "/" + frameIndex + ".png").toExternalForm()
	                ));
	                petWindow.setX(petWindow.getX() + x);
	                petWindow.setY(petWindow.getY() + y); // 更新寵物視窗位置
	            }
	        ));
	    }
	    
	    // 最後一幀的處理
	    timeline.getKeyFrames().add(new KeyFrame(
	            Duration.millis(millis * (pictureNum) + 1), // 在200 * (frameIndex-1)毫秒時執行event
	            event -> {  
	                petImage.setImage(new Image(
	                    PetController.class.getResource("/image/" + petName + "/" + state + "/" + pictureNum + ".png").toExternalForm()
	                ));
	                petWindow.setX(petWindow.getX() + x);
	                petWindow.setY(petWindow.getY() + y); // 更新寵物視窗位置
	            }
	        ));
	    
	    timeline.setCycleCount(1); // 只執行一次
	    timeline.setOnFinished(e -> {
	        nextStage = true; // ✅動畫播放完畢後才允許進入下一階段
	    });
	    timeline.play(); // 開始動畫
	}

}
