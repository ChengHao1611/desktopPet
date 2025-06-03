package pet;

import java.io.File;
import java.io.FilenameFilter;

public class PetPictureNumber {
	private String petName; // 寵物名稱
	public int walk, climb, suspension, fall, stumble, sit, drag, idle; // 寵物圖片編號
	
	public PetPictureNumber(String petName) {
		this.petName = petName;
		
		walk = setPictureNumber("walk");
		climb = setPictureNumber("climb");
		suspension = setPictureNumber("suspension");
		fall = setPictureNumber("fall");
		stumble = setPictureNumber("stumble");
		sit = setPictureNumber("sit");
		drag = setPictureNumber("drag");
		idle = setPictureNumber("idle");
	}
	
	private int setPictureNumber(String state) {
		// 指定資料夾路徑
        String folderPath = "src/image/" + petName + "/" +state; // 替換成你的資料夾路徑

        File folder = new File(folderPath);

        // 檢查資料夾是否存在且為資料夾
        if (folder.exists() && folder.isDirectory()) {
            // 透過 FilenameFilter 過濾出所有 .png 檔案
            File[] pngFiles = folder.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".png");
                }
            });

            if (pngFiles != null) {
                //System.out.println("資料夾中的 PNG 檔案數量: " + pngFiles.length);
                return pngFiles.length; // 返回 PNG 檔案數量
            } else {
                System.out.println("資料夾中無 PNG 檔案");
            }
        } else {
            System.out.println("指定的資料夾不存在或路徑錯誤");
        }
		return 0;
	}
}
