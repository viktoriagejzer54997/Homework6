//Задача 2: переписать тесты из задачи 1 на Selenide и добавить ещё один автотест. В коде, связанном с этим заданием, не должно быть методов из Selenium — только Selenide. Также все проверки должны быть выполнены методами Selenide (shouldBe).
//
//Список кейсов:
//
//2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.
//
//2.2. Добавить товар в корзину и проверить, что он отображается.
//
//2.3. Попытаться войти в админку с неверным логином и паролем.
//
//2.4. Проверить сохранение товаров в корзине после обновления страницы.
//
//2.5. Добавить в корзину товаров более чем на 300 рублей и нажать на кнопку «Оформить заказ». Проверить, что отображается JS Alert.

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Task2 {

    @BeforeEach
    void setup(){
        Selenide.open("http://localhost:8080/");
    }

    //2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.
    @Test
    void task2_1(){
        $x("//div/a[@href=\"/admin\"]").click();
//Авторизация в админке
        $x("//p/input[@type=\"text\"]").sendKeys("admin");
        $x("//p/input[@type=\"password\"]").sendKeys("secret123");
        $x("//*[@type=\"submit\"]").click();

        //Ввод нового товара
        int randomNumber = (int) (Math.random() * 10000);
        String productName = "Товар_" + randomNumber;
        $x("//div/input[@type=\"text\"]").sendKeys(productName);
        $x("//div/input[@type=\"number\"]").sendKeys("350");
        $x("//div/button[@id=\"add-btn\"]").click();

        //Переход на витрину и проверка
        $x("//div/a[@href=\"/\"]").click();
        $x("//div[@class='product-card' and @data-name='" + productName + "']").shouldBe(Condition.visible);
    }

    //2.2. Добавить товар в корзину и проверить, что он отображается.
    @Test
    void task2_2(){
        $x("//div/a[@href=\"/admin\"]").click();
        //Авторизация в админке
        $x("//p/input[@type=\"text\"]").sendKeys("admin");
        $x("//p/input[@type=\"password\"]").sendKeys("secret123");
        $x("//*[@type=\"submit\"]").click();

        //Ввод нового товара
        int randomNumber = (int) (Math.random() * 10000);
        String productName = "Товар_" + randomNumber;
        $x("//div/input[@type=\"text\"]").sendKeys(productName);
        $x("//div/input[@type=\"number\"]").sendKeys("350");
        $x("//div/button[@id=\"add-btn\"]").click();

        //Переход на витрину
        $x("//div/a[@href=\"/\"]").click();

        //Добавление нового товара в корзину
        $x("//div[@data-name='"+productName+"'][@data-price='350']//button[@data-action='add-to-cart']").click();

        //Проверка товара
        $x("//*[@id='open-cart-btn']").click();
        $x("//div[contains(@class, 'cart-item') and .//b[text()='"+productName+"'] and .//div[contains(text(), '350')]]")
                .shouldBe(Condition.visible);
    }

    //2.3. Попытаться войти в админку с неверным логином и паролем.
    @Test
    void task2_3(){
        $x("//div/a[@href=\"/admin\"]").click();
        $x("//p/input[@type=\"text\"]").sendKeys("admin");;
        $x("//p/input[@type=\"password\"]").sendKeys("secret1");
        $x("//*[@type=\"submit\"]").click();
        $x("//*[@class='alert alert-danger']")
                .shouldBe(Condition.visible)
                .shouldHave(text("Неверные учетные данные пользователя"));
    }

    //2.4. Проверить сохранение товаров в корзине после обновления страницы.
    @Test
    void task2_4(){
        $x("//div/a[@href=\"/admin\"]").click();
        //Авторизация в админке
        $x("//p/input[@type=\"text\"]").sendKeys("admin");
        $x("//p/input[@type=\"password\"]").sendKeys("secret123");
        $x("//*[@type=\"submit\"]").click();

        //Ввод нового товара
        int randomNumber = (int) (Math.random() * 10000);
        String productName = "Товар_" + randomNumber;
        $x("//div/input[@type=\"text\"]").sendKeys(productName);
        $x("//div/input[@type=\"number\"]").sendKeys("300");
        $x("//div/button[@id=\"add-btn\"]").click();

        //Переход на витрину
        $x("//div/a[@href=\"/\"]").click();

        //Добавление нового товара в корзину
        $x("//div[@data-name='"+productName+"'][@data-price='300']//button[@data-action='add-to-cart']").click();

        //Перезагрузка страницы
        Selenide.refresh();

        //Проверка товара
        $x("//*[@id='open-cart-btn']").click();
        $x("//div[contains(@class, 'cart-item') and .//b[text()='"+productName+"'] and .//div[contains(text(), '300')]]")
                .shouldBe(Condition.visible);
    }

    //2.5. Добавить в корзину товаров более чем на 300 рублей и нажать на кнопку «Оформить заказ». Проверить, что отображается JS Alert.
    @Test
    void task2_5(){
        $x("//div/a[@href=\"/admin\"]").click();
        //Авторизация в админке
        $x("//p/input[@type=\"text\"]").sendKeys("admin");
        $x("//p/input[@type=\"password\"]").sendKeys("secret123");
        $x("//*[@type=\"submit\"]").click();

        //Ввод нового товара
        int randomNumber = (int) (Math.random() * 10000);
        String productName = "Товар_" + randomNumber;
        $x("//div/input[@type=\"text\"]").sendKeys(productName);
        $x("//div/input[@type=\"number\"]").sendKeys("500");
        $x("//div/button[@id=\"add-btn\"]").click();

        //Переход на витрину
        $x("//div/a[@href=\"/\"]").click();

        //Добавление нового товара в корзину
        $x("//div[@data-name='"+productName+"'][@data-price='500']//button[@data-action='add-to-cart']").click();

        //Проверка товара
        $x("//*[@id='open-cart-btn']").click();
        $x("//*[@id = 'makeOrder']").click();
        confirm("[SmartShop]: Денег не хватает! Сумма 500 ₽ превышает лимит 300 ₽.");

    }

}
