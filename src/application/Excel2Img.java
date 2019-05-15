package application;


import java.io.InputStream;
import java.net.URI;

import com.aspose.cells.ImageFormat;
import com.aspose.cells.ImageOrPrintOptions;
import com.aspose.cells.License;
import com.aspose.cells.SheetRender;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

public class Excel2Img {
	
	public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = Excel2Img.class.getClassLoader().getResourceAsStream("/license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

	public  static void ConvertToImage (String filepath ,String picpath){
        Workbook book = null;
        try {
            book = new Workbook(new URI(Excel2Img.class.getClass().getResource(filepath).toString()).getPath());
            Worksheet sheet = book.getWorksheets().get(0);
            ImageOrPrintOptions imgOptions = new ImageOrPrintOptions();
            imgOptions.setImageFormat(ImageFormat.getJpeg());
            imgOptions.setCellAutoFit(true);
            imgOptions.setOnePagePerSheet(true);
            SheetRender render = new SheetRender(sheet, imgOptions);
            render.toImage(0, new URI(Excel2Img.class.getClass().getResource(filepath).toString()).getPath().replaceAll("xls", "jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    public static void main (String[] args ){
    	// —È÷§License
        if (!getLicense()) {
        	System.out.println("fail to get License");
            return;
        }
        ConvertToImage(Util.EXCEL_PATH, Util.IMAGE_OF_EXCEL_PATH);
        System.out.println("completed");
    }
}
