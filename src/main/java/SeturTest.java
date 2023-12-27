import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class SeturTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static String location = "";
    private static String date = "";
    private static String numberOfPeople = "";

    public SeturTest() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    public void CsvReader () {
        String csvFilePath = "src/main/csvFiles/testCSV.txt";

        try {
            Reader reader = new FileReader(csvFilePath);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            boolean firstRecord = true;

            for (CSVRecord csvRecord : csvParser) {
                if (firstRecord) {

                    firstRecord = false;
                    continue;
                }
                location = csvRecord.get(0);
                date = csvRecord.get(1);
                numberOfPeople = csvRecord.get(2);

            }

            csvParser.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void enterSite () {
        driver.get("https://www.setur.com.tr/");
        if (wait.until(ExpectedConditions.urlToBe("https://www.setur.com.tr/"))){
            System.out.println("Setur Url Geldi");
        }else{
            System.out.println("Hata , Setur url gelmedi !");
        }
    }
    public void controlOtelTab () {

        WebElement currentTab = driver.findElement(By.cssSelector(".sc-5391ca11-0.hxzxnh"));
        WebElement currentTabControl = currentTab.findElement((By.cssSelector(".sc-5391ca11-2.bpCtkF")));
        if(currentTabControl.getText().equals("Otel")){
            System.out.println("Otel Tab'ı default geldi");
        }else {
            System.out.println("Hata , Otel Tab'ı default gelmedi");
        }
    }
    public void cookieAndAdsClosed () {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement adsCloseButton =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[5]/div/div/div/div[2]/div/span")));
        WebElement cookieRejective =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[5]/div[2]/a[1]")));
        adsCloseButton.click();
        cookieRejective.click();


    }
    public void enterInformation () {

        try {

            Thread.sleep(300);

            // Gidilcek lokasyon seçme

            WebElement searchTextBox = driver.findElement(By.cssSelector(".sc-24cfb4e8-0.jpJa-DS"));
            searchTextBox.sendKeys(location );

            highlightBackground(searchTextBox);
            searchTextBox.click();

            WebElement searchTextBoxFirstElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sc-10cd16e6-0.ftkduW > div:nth-child(1)")));
            Thread.sleep(500);
            highlightBackground(searchTextBoxFirstElement);
            searchTextBoxFirstElement.click();

            // Tarih Seçimi

            Datepick();

            // Kişi sayısı
            WebElement numberOfPeopleButton = driver.findElement(By.cssSelector(".sc-b2c3f6ee-20.kJtpTG"));
            highlightBackground(numberOfPeopleButton);
            numberOfPeopleButton.click();
            WebElement incrementButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='increment-button']")));

            int intagerNumberOfPeople = Integer.parseInt(numberOfPeople);

            if (intagerNumberOfPeople > 0 ){
                highlightBackground(incrementButton);
                incrementButton.click();
                System.out.println("Kişi sayısı bir artırıldı  ");
            }else {
                System.out.println("Hata , Kişi sayısı artırılmadı ! ");
            }

            // Ara butonu

            WebElement testToVisibleSearchButton = driver.findElement(By.cssSelector(".sc-fc13bd1-0.vgwrl"));
            Thread.sleep(1000);
            if(testToVisibleSearchButton.isEnabled()){
                highlightBackground(testToVisibleSearchButton);
                testToVisibleSearchButton.click();
                System.out.println("Ara butonu görünür.");
            }

        } catch (Exception e) {
            //
        }
    }

    public void Datepick() {

        WebElement dateBox = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[3]/div[3]/div[1]/div[2]/div/div/div/div[2]/div[2]/div/div/div"));
        highlightBackground(dateBox);
        dateBox.click();
        WebElement nextMonthButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"__next\"]/div[3]/div[3]/div[1]/div[2]/div/div/div/div[2]/div[2]/div/div[2]/div/div/div/div/div/div[2]/div[1]/button[2]")));

        int maxTries = 3;
        for (int i = 0; i < maxTries; i++) {
            try {
                highlightBackground(nextMonthButton);
                nextMonthButton.click();
                Thread.sleep(1000);
                /*
                WebElement nisanInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//strong[contains(text(), 'Nisan')]")));
                String text = nisanInput.getText();
                if ( text != null) {
                    System.out.println("Nisan ayı bulundu!!!!");
                    break;
                }
                 */
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        WebElement firstDay = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[3]/div[3]/div[1]/div[2]/div/div/div/div[2]/div[2]/div/div[2]/div/div/div/div/div/div[2]/div[2]/div/div[3]/div/table/tbody/tr[1]/td[1]/span"));
        WebElement lastDay = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[3]/div[3]/div[1]/div[2]/div/div/div/div[2]/div[2]/div/div[2]/div/div/div/div/div/div[2]/div[2]/div/div[3]/div/table/tbody/tr[1]/td[7]/span"));

        try {
            highlightBackground(firstDay);
            firstDay.click();
            Thread.sleep(500);
            highlightBackground(lastDay);
            lastDay.click();
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void newCurrentUrl() {

        WebDriverWait waitForUrl = new WebDriverWait(driver, Duration.ofSeconds(30));
        String oldUrl = driver.getCurrentUrl();
        waitForUrl.until(ExpectedConditions.not(ExpectedConditions.urlToBe(oldUrl)));
        String newUrl = driver.getCurrentUrl();
        waitForUrl.until(ExpectedConditions.urlToBe(newUrl));

        isAntalyaAvailable();
        otherRegions();
    }

    public void isAntalyaAvailable() {
        String searchName = "Antalya";
        String currentUrl = driver.getCurrentUrl();

        if (currentUrl.contains(searchName.toLowerCase())) {
            System.out.println("Sayfada " + searchName + " bulunuyor.");
        } else {
            System.out.println("Hata, sayfada " + searchName + " bulunmuyor!");
        }
    }

    public void otherRegions () {

        Random randomMethod = new Random();
        WebElement otherRegionDiv = driver.findElement(By.cssSelector(".sc-2569635-2.PSzMH"));
        List<WebElement> checkboxes = otherRegionDiv.findElements(By.cssSelector(".sc-e4b3cd20-0[data-testid='checkbox']"));


        int randomIndex = randomMethod.nextInt(checkboxes.size());
        WebElement randomCheckbox = checkboxes.get(randomIndex);

        highlightBackground(randomCheckbox);
        randomCheckbox.click();


        String numberText = randomCheckbox.findElement(By.cssSelector(".sc-e4b3cd20-3")).getText();
        String selectedNumber = numberText.replaceAll("[^0-9]", "");
    }

    public void highlightBackground(WebElement element) {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.backgroundColor='yellow'", element);
    }


    public static void main(String[] args) {
        SeturTest seturTest = new SeturTest();

        seturTest.CsvReader();
        seturTest.enterSite();
        seturTest.controlOtelTab();
        seturTest.cookieAndAdsClosed();
        seturTest.enterInformation();
        seturTest.newCurrentUrl();


    }
}


