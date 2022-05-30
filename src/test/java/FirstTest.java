import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Set;


public class FirstTest {

    private Logger logger = LogManager.getLogger(FirstTest.class);
    private WebDriver driver;
    private ConfigServer cfg = ConfigFactory.create(ConfigServer.class);

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
    }

    @After
    public void close() {
        if (driver != null)
            driver.close();
        logger.info("драйвер закрыт");
    }

    @Test
    public void openDuckHeadless(){
        String otus = "ОТУС";
        String locatorLine = "//input[@id ='search_form_input_homepage']";
        String locatorButton = "//input[@id = 'search_button_homepage']";
        String locatorFirstResult = "(//a[@data-testid='result-title-a'])[1]//span[contains(text(),'Онлайн‑курсы для профессионалов, дистанционное обучение')]";

        ChromeOptions option = new ChromeOptions();
        option.addArguments("headless");
        driver = new ChromeDriver(option);

        driver.get(cfg.duckUrl());
        logger.info("Открыли https://duckduckgo.com/ в headless режиме");
        driver.findElement(By.xpath(locatorLine)).sendKeys(otus);
        logger.info("В поисковую строку ввели ОТУС");
        driver.findElement(By.xpath(locatorButton)).click();
        Assert.assertTrue(driver.findElement(By.xpath(locatorFirstResult)).isDisplayed());
        logger.info("Первый результат поисковой выдачи содержит \"Онлайн‑курсы для профессионалов, дистанционное обучение...\"");
    }

    @Test
    public void openDemo() throws InterruptedException {

        String locatorModalWindow = "//a[@class='pp_expand']";
        String locatorPictures = "//span[@class='image-block']";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--kiosk");
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        logger.info("Запустили хром в киоске");

        driver.get(cfg.demoUrl());
        logger.info("Открыли demo.w3layouts.com/..");
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(30));

        List<WebElement> elements = driver.findElements(By.xpath(locatorPictures));
        int index = (int) ((Math.random() * elements.size()) - 1); //получаем индекс рандомной картинки

        logger.info("индекс равен "+index);
        System.out.println("Number of elements:" +elements.size());

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", wait.until(ExpectedConditions.elementToBeClickable(elements.get(index))));
        Thread.sleep(1000); //если убрать, то тест периодически ломается
        wait.until(ExpectedConditions.elementToBeClickable(elements.get(index))).click();
        Assert.assertTrue(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatorModalWindow))).isEnabled());
        logger.info("Получили элемент, содержащий признак модального окна");

/*      wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locatorCloseButton))).click();
        logger.info("Закрыли модальное окно");
        Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.xpath(locatorModalWindow)))));
        logger.info("Проверили, что модальное окно пропало из дома. Прошла проверка на открытие картинки в модальном окне");*/
    }

    @Test
    public void otusAvtoriz(){
        String buttonEntrance = "//button[@data-modal-id='new-log-reg']";
        String fieldEmail = "//input[@type='text'][@name = 'email'][@placeholder='Электронная почта']";
        String fieldPassword = "//input[@type='password'][@placeholder='Введите пароль']";
        String buttonToComeIn = "//div[contains(@class,\"new-input-line_last\")]/button[contains(text(), \"Войти\")]";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);
        driver.get(cfg.otusUrl());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //нажать на кнопку Вход
        driver.findElement(By.xpath(buttonEntrance)).click();
        // wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonEntrance))).click();
        //заполнить поле Электронная почта
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(fieldEmail))).sendKeys(cfg.email());
        //заполнить поле Введите пароль
        driver.findElement(By.xpath(fieldPassword)).sendKeys(cfg.password());
        //нажать на кнопку Войти
        driver.findElement(By.xpath(buttonToComeIn)).click();
        logger.info("Успешная авторизация");
        cookie();
        }

    public void cookie(){
        Set<Cookie> cookies = driver.manage().getCookies();
        logger.info("Вывод кук:");
        for(Cookie cookie:cookies){
        logger.info(cookie);
        }
    }
}

































