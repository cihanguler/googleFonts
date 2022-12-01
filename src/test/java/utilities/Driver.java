package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.util.HashMap;
import java.util.Map;

public class Driver {
    private Driver() {
    }

    private static InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    public static WebDriver get() {
        if (driverPool.get() == null) {
            String browserParamFromEnv = System.getProperty("browser");
            String browser = browserParamFromEnv == null ? ConfigurationReader.get("browser") : browserParamFromEnv;
            DesiredCapabilities caps = new DesiredCapabilities();
            ChromeOptions chromeOptions = new ChromeOptions();

            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    caps = new DesiredCapabilities();
                    /**
                     *I have disabled the cookies in below ChromeOptions
                     * You need to add this feature to your configuration.properties
                     * Add cookiesEnableDisable=2  (disable the cookies)
                     * Add cookiesEnableDisable =0  (enable the cookies)
                     */
                    chromeOptions = new ChromeOptions();
                    Map<String, Object> prefs = new HashMap<String, Object>();
                    Map<String, Object> profile = new HashMap<String, Object>();
                    Map<String, Object> contentSettings = new HashMap<String, Object>();

                    contentSettings.put("cookies", ConfigurationReader.get("cookiesEnableDisable"));
                    profile.put("managed_default_content_settings", contentSettings);
                    prefs.put("profile", profile);
                    chromeOptions.setExperimentalOption("prefs", prefs);
                    caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                    driverPool.set(new ChromeDriver(chromeOptions));
                    break;
                case "chrome-headless":
                    WebDriverManager.chromedriver().setup();
                    driverPool.set(new ChromeDriver(new ChromeOptions().setHeadless(true)));
                    break;

            }
        }
        return driverPool.get();
    }
    public static void closeDriver() {
        driverPool.get().quit();
        driverPool.remove();
    }
}