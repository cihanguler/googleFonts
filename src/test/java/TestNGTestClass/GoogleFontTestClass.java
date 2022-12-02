package TestNGTestClass;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utilities.BrowserUtils;
import utilities.ConfigurationReader;
import utilities.Driver;
import utilities.ExcelUtil;
import java.util.concurrent.TimeUnit;

public class GoogleFontTestClass {

    Logger logger = LoggerFactory.getLogger(GoogleFontTestClass.class);
    static String browser = ConfigurationReader.get("browser");
    static String path = ConfigurationReader.get("excelPath");
    static String sheetName = ConfigurationReader.get("sheetName");
    String url = "";
    String number = "0";

    public GoogleFontTestClass(String url) {
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
            Driver.get().navigate().to(url);
            BrowserUtils.waitForPageToLoad(4);
            int webElements = 0;
            int webElements2 = 0;
            int webElements3 = 0;
            int webElements4 = 0;
            int rowNum = Integer.parseInt(number.replace(".0", ""));
            System.out.println(rowNum + " = ");
            int colNum = 2;
            ExcelUtil listOfUrl = new ExcelUtil(path, sheetName);
            String text = "found google-font";
            String blank = "";
            listOfUrl.setCellData(blank, rowNum, colNum);

            try {
                //to find CSS google-fonts
                webElements = Driver.get().findElements(By.cssSelector("[rel=stylesheet][href*=googleapis]")).size();
                //to find static google-fonts
                webElements2 = Driver.get().findElements(By.cssSelector("[href*=gstatic]")).size();
                //to find static google-fonts
                webElements3 = Driver.get().findElements(By.cssSelector("[src*=gstatic]")).size();
                //to find javascript google-fonts
                webElements4 = Driver.get().findElements(By.cssSelector("[src*=googleapis]")).size();
            } catch (Exception exception) {
            }

            if (webElements > 0 || webElements2>0 || webElements3>0 || webElements4>0) {
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

    }


