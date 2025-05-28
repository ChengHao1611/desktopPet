package pet;

public class PetController {
	private PetStage petStage;
	
	public PetController(PetStage petStage) {
		this.petStage = petStage; // 初始狀態為 WALK
	}
	
	public void setPetStage(PetStage petStage) {
		this.petStage = petStage;
	}
	
	
}
