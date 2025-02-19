package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider { // Lets us read the data
        ChromeDriver driver;
        Wrappers wrapper;

        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */

        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
                wrapper = new Wrappers(driver);
        }

        @Test
        public void testCase01() {
                driver.get("https://www.youtube.com/");
                Assert.assertTrue(driver.getCurrentUrl().contains("www.youtube.com"),
                                "URL does not match expected value");
                wrapper.clickElement(By.xpath("//*[@id='guide-icon']/parent::button[@aria-label='Guide']"));
                wrapper.clickElement(By.xpath("//a[text()='About']"));
                WebElement contactusMessage = wrapper.findElement(By.className("ytabout__content"));
                wrapper.scrollToElement(contactusMessage);
                WebElement headingMessage = contactusMessage
                                .findElement(By.xpath("./child::h1[contains(text(), 'About')]"));
                WebElement paragraphMessage = contactusMessage
                                .findElement(By.xpath("./child::p[contains(text(),'Our mission ')]"));
                Assert.assertEquals(headingMessage.getText(), "About YouTube",
                                " About us heading does not contain Expected Text");
                Assert.assertEquals(paragraphMessage.getText(),
                                "Our mission is to give everyone a voice and show them the world.",
                                " About us  does not contain Expected Text");
        }

        @Test
        public void testCase02() throws InterruptedException {
                driver.get("https://www.youtube.com/");
                wrapper.clickElement(By.xpath("//*[@id='guide-icon']/parent::button[@aria-label='Guide']"));
                wrapper.clickElement(By.xpath("//yt-formatted-string[text()='Movies']"));
                Thread.sleep(2000);
                By nextButtonLocator = By.xpath("//button[@aria-label='Next']");
                while (true) {
                        try {
                                WebElement nextButton = wrapper.findElement(nextButtonLocator);
                                if (nextButton.isDisplayed()) {
                                        wrapper.clickElement(nextButtonLocator);
                                        Thread.sleep(1000);
                                } else {
                                        break;
                                }
                        } catch (Exception e) {
                                System.out.println("Next button not found or disappeared.");
                                break;
                        }
                }
                WebElement movieRatingElement = wrapper.findElement(By.xpath(
                                "//*[@id='items']/ytd-grid-movie-renderer[16]/ytd-badge-supported-renderer/div[2]/p"));
                WebElement movieGenre = wrapper
                                .findElement(By.xpath("//*[@id='items']/ytd-grid-movie-renderer[16]/a/span"));
                String[] genreText = movieGenre.getText().split(" • ");
                SoftAssert softAssert = new SoftAssert();
                softAssert.assertEquals(movieRatingElement.getText(), "U/A", "Movie rating mismatch!");
                softAssert.assertEquals(genreText[0], "Indian cinema", "Genre mismatch!");
                softAssert.assertAll();
        }

        @Test
        public void testCase03() throws InterruptedException {
                driver.get("https://www.youtube.com/");
                wrapper.clickElement(By.xpath("//*[@id='guide-icon']/parent::button[@aria-label='Guide']"));
                wrapper.clickElement(By.xpath("//yt-formatted-string[text()='Music']"));
                Thread.sleep(2000);
                WebElement showMoreButton = wrapper.findElement(By.xpath("//button[@aria-label='Show more']"));
                wrapper.scrollToElement(showMoreButton);
                wrapper.jsClickElement(showMoreButton);
                WebElement playlist = wrapper
                                .findElement(By.xpath("//span[normalize-space()='Bollywood Dance Hitlist']"));
                wrapper.scrollToElement(playlist);
                System.out.println("Playlist Name: " + playlist.getText());
                WebElement songsCount = playlist.findElement(By.xpath(
                                "./ancestor::div[@class='yt-lockup-view-model-wiz__metadata']/parent::div/descendant::div[@class='badge-shape-wiz__text']"));
                int numberOfSongs = Integer.parseInt(songsCount.getText().replaceAll("\\D+", ""));
                Assert.assertTrue(numberOfSongs <= 50, "Songs count exceeds 50! Actual count: " + numberOfSongs);
        }

        @Test
        public void testCase04() throws InterruptedException {
                driver.get("https://www.youtube.com/");
                wrapper.clickElement(By.xpath("//*[@id='guide-icon']/parent::button[@aria-label='Guide']"));
                wrapper.clickElement(By.xpath("//yt-formatted-string[text()='News']"));
                Thread.sleep(5000);
                WebElement latestNewsPost = wrapper.findElement(By.xpath("//span[text()='Latest news posts']"));
                wrapper.scrollToElement(latestNewsPost);
                List<WebElement> titles = wrapper.findElements(By.xpath("//*[@id='author-text']"));
                List<WebElement> body = wrapper.findElements(By.xpath("//*[@id='body']"));
                int sum = 0;
                for (int i = 0; i < 3; i++) {
                        try {
                                WebElement likes = body.get(i).findElement(By.xpath(
                                                "./following-sibling::div/descendant::span[@id='vote-count-middle']"));
                                String likesCount = likes.getText().replaceAll("[^0-9]", "");
                                int likescount = likesCount.isEmpty() ? 0 : Integer.parseInt(likesCount);
                                System.out.println("Title : " + titles.get(i).getText());
                                System.out.println("Body  : " + body.get(i).getText());
                                if (likescount > 0) {
                                        sum += likescount;
                                }
                        } catch (Exception e) {
                                System.out.println("Error retrieving likes count: " + e.getMessage());
                        }
                }
                System.out.println("Total likes for first three latest news posts: " + sum);
        }

        @Test(dataProvider = "excelData", dataProviderClass = demo.utils.ExcelDataProvider.class)
        public void testCase05(String searchString) throws InterruptedException {
                long sum = 0;
                driver.get("https://www.youtube.com/");
                wrapper.search("//form[@action='/results']/input[@name='search_query']", searchString);
                List<WebElement> views = wrapper
                                .findElements(By.xpath("//*[@id='metadata-line']/span[contains(text(), 'views')]"));
                for (WebElement view : views) {
                        String viewString = view.getText();
                        long numberOfViews = wrapper.convertViewsToNumber(viewString);
                        if (sum + numberOfViews <= 100000000) {
                                sum = sum + numberOfViews;
                        } else {
                                break;
                        }

                }

                System.out.println(searchString + "  : Views sum is  :" + sum);
        }

        @AfterTest
        public void endTest() {
                driver.close();
                driver.quit();

        }
}