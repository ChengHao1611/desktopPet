package pet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PetController {
	private PetStage petStage;
	private ImageView petImage;
	private Stage petWindow; // 寵物狀態
	private String petName;
	
	public PetController(ImageView petImage, Stage petWindow, String petName) {
		this.petStage = PetStage.WALK; // 初始狀態為 WALK
		this.petImage = petImage;
		this.petWindow = petWindow;
		this.petName = petName;
		
		 // 加入點擊事件
	    petImage.setOnMouseClicked(event -> {
	    	
//	    	petImage.setImage(new Image(
//				PetWindow.class.getResource("/image/pikachu/walk/2.png").toExternalForm()
//			)); // 點擊後更換圖片
	        System.out.println("🐾 桌寵被點擊了！");
	    });
	}

	public void show() {
		while(true) {
			petStage = PetStage.WALK; // 取得狀態
			
			switch (petStage) {
				case WALK:
					petImage.setImage(new Image(
						PetController.class.getResource("/image/"+petName+"/walk/1.png").toExternalForm()
					));
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
	}

}
