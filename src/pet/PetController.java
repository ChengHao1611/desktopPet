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
	private PetStage preStage; // 寵物狀態
	private ImageView petImage;
	private Stage petWindow; // 寵物狀態
	private String petName;
	private AnimationTimer animationTimer; // 動畫計時器
	private boolean nextStage = true; // 是否切換到下一個狀態
	private PetPictureNumber petPictureNumber; // 寵物圖片數量
	private StageController controller; // 狀態控制器
	private int pictureIndex = 0; // 當前圖片索引
	
	public PetController(ImageView petImage, Stage petWindow, String petName) {
		this.petStage = PetStage.LEFT_WALK; // 初始狀態為 WALK
		this.preStage = PetStage.LEFT_WALK; // 初始狀態為 WALK
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
				if (preStage != petStage) pictureIndex = 0;
				preStage = petStage; // 更新前一狀態為當前狀態
				nextStage = false; // 重置為 false，等待下一次觸發
				petStage = controller.getCurrentStage(); // 更新狀態控制器的當前狀態
				//System.out.println("Current Stage: " + petStage); // 印出目前狀態
				
				switch (petStage) {
					case LEFT_WALK:
						pictureIndex = calculatePictureIndex(petPictureNumber.walk); // 更新圖片索引
						action(pictureIndex, "walk", -2, 0, 200, 1); // 呼叫 petWalk 方法
						break;
					case RIGHT_WALK:
						pictureIndex = calculatePictureIndex(petPictureNumber.walk); // 更新圖片索引
						action(pictureIndex, "walk", 2, 0, 200, -1); // 呼叫 petWalk 方法
						break;
					case LEFT_CLIMB_UP:
						pictureIndex = calculatePictureIndex(petPictureNumber.climb); // 更新圖片索引
						action(pictureIndex, "climb", 0, -2, 200, 1); // 呼叫 petClimb 方法
						break;
					case LEFT_CLIMB_DOWN:
						pictureIndex = calculatePictureIndex(petPictureNumber.climb); // 更新圖片索引
						action(pictureIndex, "climb", 0, 2, 200, 1); // 呼叫 petClimb 方法
						break;
					case RIGHT_CLIMB_UP:
						pictureIndex = calculatePictureIndex(petPictureNumber.climb); // 更新圖片索引
						action(pictureIndex, "climb", 0, -2, 200, -1); // 呼叫 petClimb 方法
						break;
					case RIGHT_CLIMB_DOWN:
						pictureIndex = calculatePictureIndex(petPictureNumber.climb); // 更新圖片索引
						action(pictureIndex, "climb", 0, 2, 200, -1); // 呼叫 petClimb 方法
						break;
					case LEFT_SUSPENSION:
						pictureIndex = calculatePictureIndex(petPictureNumber.suspension); // 更新圖片索引
						action(pictureIndex, "suspension", -2, 0, 200, -1); // 呼叫 petSuspension 方法
						break;
					case RIGHT_SUSPENSION:
						pictureIndex = calculatePictureIndex(petPictureNumber.suspension); // 更新圖片索引
						action(pictureIndex, "suspension", 2, 0, 200, 1); // 呼叫 petSuspension 方法
						break;
					case FALL:
						pictureIndex = calculatePictureIndex(petPictureNumber.fall); // 更新圖片索引
						action(pictureIndex, "fall", 0, 2, 200, 1); // 呼叫 petFall 方法
						break;
					case STUMBLE:
						pictureIndex = calculatePictureIndex(petPictureNumber.stumble); // 更新圖片索引
						action(pictureIndex, "stumble", 0, 0, 200, 1); // 呼叫 petStumble 方法
						break;
					case SIT:
						pictureIndex = calculatePictureIndex(petPictureNumber.sit); // 更新圖片索引
						action(pictureIndex, "sit", 0, 0, 200, 1); // 呼叫 petSit 方法
						break;
					case DRAG:
						pictureIndex = calculatePictureIndex(petPictureNumber.drag); // 更新圖片索引
						action(pictureIndex, "drag", 0, 0, 200, 1); // 呼叫 petDrag 方法
						break;
					case IDLE:
						pictureIndex = calculatePictureIndex(petPictureNumber.idle); // 更新圖片索引
						action(pictureIndex, "idle", 0, 0, 700, 1); // 呼叫 petIdle 方法
						break;
				}
			}
		};
	}
	
	private int calculatePictureIndex(int pictureCount) {
		// 計算當前圖片索引
		if(pictureCount == 0) return 0;
		return (pictureIndex % pictureCount) + 1; // 循環使用圖片
	}

	public void start() {
		animationTimer.start(); // 啟動動畫計時器
	}
	
	public void stop() {
		animationTimer.stop(); // 停止動畫計時器
	}

	// x, y 參數用於調整寵物視窗位置, millis 參數用於設定每一幀的時間間隔
	private void action(int pictureIndex, String state, int x, int y, int millis, int reverse) {
	    if(pictureIndex == 0) return; // 如果圖片索引為0，則不執行任何操作
		
		// 建立 Timeline 動畫
		Timeline timeline = new Timeline();
	    for (int i = 0; i < 2; i++) {
	        final int frameIndex = i + 1;
	        timeline.getKeyFrames().add(new KeyFrame(
	            Duration.millis(millis * (frameIndex - 1) + 1), // 在200 * (frameIndex-1)毫秒時執行event
	            event -> {  
	                petImage.setImage(new Image(
	                    PetController.class.getResource("/image/" + petName + "/" + state + "/" + pictureIndex + ".png").toExternalForm()
	                ));
	                petImage.setScaleX(reverse); // 反轉圖片
	                petWindow.setX(petWindow.getX() + x);
	                petWindow.setY(petWindow.getY() + y); // 更新寵物視窗位置
	                detectPetLocation(); // 檢測寵物位置，確保不會超出螢幕邊界
	            }
	        ));
	    }
	    
	    timeline.setCycleCount(1); // 只執行一次
	    timeline.setOnFinished(e -> {
	        nextStage = true; // ✅動畫播放完畢後才允許進入下一階段
	    });
	    timeline.play(); // 開始播放動畫
	}
	
	private void detectPetLocation() {
		if(controller.isAtLeftEdge()) {
			petWindow.setX(0); // 設定寵物視窗在左邊
		}
		
		if(controller.isAtRightEdge()) {
			petWindow.setX(controller.screenWidth() - petWindow.getWidth()); // 設定寵物視窗在右邊
		}
		
		if(controller.isAtTopEdge()) {
			petWindow.setY(0); // 設定寵物視窗在上邊
		}
		
		if(controller.isAtBottomEdge()) {
			petWindow.setY(controller.screenHeight() - petWindow.getHeight()); // 設定寵物視窗在下邊
		}
	}

}
