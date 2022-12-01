package TestNGTestClass;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;
import utilities.Driver;
import utilities.ExcelUtil;
import java.util.concurrent.TimeUnit;

public class GoogleFontTestClass {

    Logger logger = LoggerFactory.getLogger(GoogleFontTestClass.class);
    static String browser = ConfigurationReader.get("browser");

    String url = "";

    public GoogleFontTestClass(String url) {
        super();
        this.url=url;

    }
    @DataProvider
    public Object [][] urlData() {
        String path = ConfigurationReader.get("excelPath");
        String sheetName = "urlList";

        ExcelUtil listOfUrl = new ExcelUtil(path,sheetName);
        String [][] dataArray = listOfUrl.getDataArrayWithoutFirstRow();
        return dataArray;

    }

    @Test(dataProvider = "urlData")
    public void findGoogleFont(String number, String url) {
        logger.info("checked url: " + url);
        Driver.get().navigate().to(url);
        int webElements= 0;
        int webElements2= 0;
        int webElements3= 0;
        int webElements4= 0;
        String link = "";
        String path = ConfigurationReader.get("excelPath");
        String sheetName = "urlList";
        int rowNum=  Integer.parseInt(number.replace(".0",""));
        System.out.println(rowNum + " = ");
        int colNum = 2;
        ExcelUtil listOfUrl = new ExcelUtil(path,sheetName);
        String text = "found google-font";
        String blank = "";
        listOfUrl.setCellData(blank, rowNum, colNum);

        try {
            //to find CSS google-fonts
            webElements = Driver.get().findElements(By.cssSelector("[rel=stylesheet][href*=googleapis]")).size();
            //to find static google-fonts
            webElements2 = Driver.get().findElements(By.cssSelector("[href*=gstatic]")).size();
            //to find static google-fonts
            //webElements3 = Driver.get().findElements(By.cssSelector("[src*=gstatic]")).size();
            //to find javascript google-fonts
            //webElements4 = Driver.get().findElements(By.cssSelector("[src*=googleapis]")).size();
        } catch (Exception exception) {
        }

        if (webElements>0 || webElements2>0) {
            listOfUrl.setCellData(text, rowNum, colNum);
            logger.info(Driver.get().getCurrentUrl());
            System.out.println("Found google-font = " + Driver.get().getCurrentUrl());
        } else{
            System.out.println("OK");
        }

    }


    @BeforeClass
    public static void getDriver() {
        if (!browser.contains("headless")) {
            Driver.get().manage().window().maximize();
        }
        Driver.get().manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeDriver() {
        Driver.closeDriver();
    }

}

