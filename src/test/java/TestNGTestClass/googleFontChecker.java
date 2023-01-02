package TestNGTestClass;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;
import utilities.Driver;
import utilities.ExcelUtil;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class googleFontChecker {

    Logger logger = LoggerFactory.getLogger(googleFontTestClass.class);
    static String browser = ConfigurationReader.get("browser");
    static String path = ConfigurationReader.get("excelPath");
    static String sheetName = ConfigurationReader.get("sheetName");
    String url = "";
    String number = "0";

    public googleFontChecker(String url) {
        super();
        this.url = url;
        this.number= number;
    }

    @DataProvider
    public Object[][] urlData () {
        ExcelUtil listOfUrl = new ExcelUtil(path, sheetName);
        String[][] dataArray = listOfUrl.getDataArrayWithoutFirstRow();
        return dataArray;

    }

    @Test(dataProvider = "urlData")
    public void findGoogleFont (String number, String url){
        logger.info("checked url: " + url);
        try {
            Driver.get().navigate().to("http://"+url);
        } catch (Exception URL) {
            Driver.get().navigate().to("https://"+url);
        }

        String pageSource = Driver.get().getPageSource();
        boolean googleFontLoaded1 = pageSource.contains("fonts.googleapis.com");
        boolean googleFontLoaded2 = pageSource.contains("fonts.gstatic.com");

        int rowNum = Integer.parseInt(number.replace(".0", ""));
        System.out.println(rowNum + " = ");
        int colNum = 2;
        ExcelUtil listOfUrl = new ExcelUtil(path, sheetName);
        String text = "found google-font";
        String blank = "";
        listOfUrl.setCellData(blank, rowNum, colNum);

        if (googleFontLoaded1 || googleFontLoaded2) {
            listOfUrl.setCellData(text, rowNum, colNum);
            logger.info(Driver.get().getCurrentUrl());
            System.out.println("Found google-font = " + Driver.get().getCurrentUrl());
        } else {
            System.out.println("Ok");
        }

    }


    @BeforeClass
    public static void getDriver () {
        if (!browser.contains("headless")) {
            Driver.get().manage().window().maximize();
        }
        long Duration = 4000;
        Driver.get().manage().timeouts().implicitlyWait(Duration, TimeUnit.MILLISECONDS);
    }

    @AfterClass
    public static void closeDriver () {
        Driver.closeDriver();
    }


    public static String takeScreenshot() {
        final byte[] screenshot = ((TakesScreenshot) Driver.get()).getScreenshotAs(OutputType.BYTES);
        String base64Path = Base64.getEncoder().encodeToString(screenshot);
        return base64Path;
    }


}
