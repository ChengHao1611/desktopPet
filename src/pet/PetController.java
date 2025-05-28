package pet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PetController {
	private PetStage petStage;
	private ImageView petImage;
	
	public PetController(ImageView petImage) {
		this.petStage = PetStage.WALK; // 初始狀態為 WALK
		this.petImage = petImage;
		
		 // 加入點擊事件
	    petImage.setOnMouseClicked(event -> {
	    	
//	    	petImage.setImage(new Image(
//				PetWindow.class.getResource("/image/pikachu/walk/2.png").toExternalForm()
//			)); // 點擊後更換圖片
	        System.out.println("🐾 桌寵被點擊了！");
	    });
	}
	
	public void setPetStage(PetStage petStage) {
		this.petStage = petStage;
	}

}
