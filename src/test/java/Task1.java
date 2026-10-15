import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.lang.model.element.Element;

import static org.assertj.core.api.Assertions.assertThat;

//1.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.
//
//1.2. Добавить товар в корзину и проверить, что он отображается.
//
//1.3. Попытаться войти в админку с неверным логином и паролем.
//
//1.4. Проверить сохранение товаров в корзине после обновления страницы.
public class Task1 {
    WebDriver driver;

    @BeforeEach
    void setup(){
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
    }

    //1.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.
    @Test
    void task1_1(){
        driver.findElement(By.xpath("//div/a[@href=\"/admin\"]")).click();
        //Авторизация в админке
        driver.findElement(By.xpath("//p/input[@type=\"text\"]")).sendKeys("admin");;
        driver.findElement(By.xpath("//p/input[@type=\"password\"]")).sendKeys("secret123");
        driver.findElement(By.xpath("//*[@type=\"submit\"]")).click();

        //Ввод нового товара
        int randomNumber = (int) (Math.random() * 10000);
        String productName = "Товар_" + randomNumber;
        driver.findElement(By.xpath("//div/input[@type=\"text\"]")).sendKeys(productName);
        driver.findElement(By.xpath("//div/input[@type=\"number\"]")).sendKeys("300");
        driver.findElement(By.xpath("//div/button[@id=\"add-btn\"]")).click();

        //Переход на витрину и проверка
        driver.findElement(By.xpath("//div/a[@href=\"/\"]")).click();
        WebElement addedProduct = driver.findElement(By.xpath("//div[@class='product-card' and @data-name='" + productName + "']"));
        assertThat(addedProduct)
                .as("Проверка отображения товара после добавления")
                .isNotNull();
    }


    //1.2. Добавить товар в корзину и проверить, что он отображается.
    @Test
    void task1_2(){
        driver.findElement(By.xpath("//div/a[@href=\"/admin\"]")).click();
        //Авторизация в админке
        driver.findElement(By.xpath("//p/input[@type=\"text\"]")).sendKeys("admin");;
        driver.findElement(By.xpath("//p/input[@type=\"password\"]")).sendKeys("secret123");
        driver.findElement(By.xpath("//*[@type=\"submit\"]")).click();

        //Ввод нового товара
        int randomNumber = (int) (Math.random() * 10000);
        String productName = "Товар_" + randomNumber;
        driver.findElement(By.xpath("//div/input[@type=\"text\"]")).sendKeys(productName);
        driver.findElement(By.xpath("//div/input[@type=\"number\"]")).sendKeys("300");
        driver.findElement(By.xpath("//div/button[@id=\"add-btn\"]")).click();

        //Переход на витрину
        driver.findElement(By.xpath("//div/a[@href=\"/\"]")).click();

        //Добавление нового товара в корзину
        driver.findElement(By.xpath("//div[@data-name='"+productName+"'][@data-price='300']//button[@data-action='add-to-cart']")).click();

        //Проверка товара
        driver.findElement(By.xpath("//*[@id='open-cart-btn']")).click();
        WebElement addedProduct = driver.findElement(By.xpath("//div[contains(@class, 'cart-item') and .//b[text()='"+productName+"'] and .//div[contains(text(), '300')]]"));
        assertThat(addedProduct)
                .as("Проверка отображения товара в корзине")
                .isNotNull();
    }


    //1.3. Попытаться войти в админку с неверным логином и паролем.
    @Test
    void negativeAuthorizationTest(){
        driver.findElement(By.xpath("//div/a[@href=\"/admin\"]")).click();
        driver.findElement(By.xpath("//p/input[@type=\"text\"]")).sendKeys("admin");;
        driver.findElement(By.xpath("//p/input[@type=\"password\"]")).sendKeys("secret1");
        driver.findElement(By.xpath("//*[@type=\"submit\"]")).click();
        String alert = driver.findElement(By.xpath("//*[@class='alert alert-danger']")).getText();
        assertThat(alert)
                .as("Проверка отображения сообщения о неверном логине и пароле")
                .isEqualTo("Неверные учетные данные пользователя");
    }

    //1.4. Проверить сохранение товаров в корзине после обновления страницы.
    @Test
    void pageUpdate(){
        driver.findElement(By.xpath("//div/a[@href=\"/admin\"]")).click();
        //Авторизация в админке
        driver.findElement(By.xpath("//p/input[@type=\"text\"]")).sendKeys("admin");;
        driver.findElement(By.xpath("//p/input[@type=\"password\"]")).sendKeys("secret123");
        driver.findElement(By.xpath("//*[@type=\"submit\"]")).click();

        //Ввод нового товара
        int randomNumber = (int) (Math.random() * 10000);
        String productName = "Товар_" + randomNumber;
        driver.findElement(By.xpath("//div/input[@type=\"text\"]")).sendKeys(productName);
        driver.findElement(By.xpath("//div/input[@type=\"number\"]")).sendKeys("300");
        driver.findElement(By.xpath("//div/button[@id=\"add-btn\"]")).click();

        //Переход на витрину
        driver.findElement(By.xpath("//div/a[@href=\"/\"]")).click();

        //Добавление нового товара в корзину
        driver.findElement(By.xpath("//div[@data-name='"+productName+"'][@data-price='300']//button[@data-action='add-to-cart']")).click();

        //Перезагрузка страницы
        driver.navigate().refresh();

        //Проверка товара
        driver.findElement(By.xpath("//*[@id='open-cart-btn']")).click();
        WebElement addedProduct = driver.findElement(By.xpath("//div[contains(@class, 'cart-item') and .//b[text()='"+productName+"'] and .//div[contains(text(), '300')]]"));
        assertThat(addedProduct)
                .as("Проверка отображения товара в корзине")
                .isNotNull();
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }
}
