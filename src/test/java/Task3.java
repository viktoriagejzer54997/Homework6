import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import static com.codeborne.selenide.Selenide.$x;

//Задача 3: написать автотесты для следующих кейсов:
//
//3.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей).
// Проверить уведомление об обработке заказа.
//
//3.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно.
//
//3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.
//
//3.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.
//
//3.5. Хотя бы в одном кейсе должны быть обращение к API для генерации тестовых данных.
//
//3.6. Хотя бы в одном кейсе в блоке @AfterEach тестовые данные должны удаляться.
public class Task3 {
    private Integer productToDeleteId = null;

    @BeforeEach
    void setup(){
        Selenide.open("http://localhost:8080/");
    }

    @AfterEach
    void tearDown(){
        // Если тест сохранил ID товара — удаляем его напрямую через API эндпоинт DELETE /goods/{id}
        if (productToDeleteId != null) {
            try {
                RestAssured.given()
                        .auth().basic("admin", "secret123") // Авторизация для бэкенда
                        .when()
                        .delete("http://localhost:8080/goods/" + productToDeleteId) // Подставляем числовой ID в URL
                        .then()
                        .statusCode(200);
            } catch (Exception e) {
                System.out.println("Не удалось удалить товар через API после теста: " + e.getMessage());
            } finally {
                productToDeleteId = null; // Сбрасываем значение переменной
            }
        }
    }

    //3.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей).
// Проверить уведомление об обработке заказа.
    @Test
    void task3_1(){
        $x("//div/a[@href=\"/admin\"]").click();
        //Авторизация в админке
        $x("//p/input[@type=\"text\"]").sendKeys("admin");
        $x("//p/input[@type=\"password\"]").sendKeys("secret123");
        $x("//*[@type=\"submit\"]").click();

        //Ввод нового товара
        int randomNumber = (int) (Math.random() * 10000);
        String productName = "Товар_" + randomNumber;
        $x("//div/input[@type=\"text\"]").sendKeys(productName);
        $x("//div/input[@type=\"number\"]").sendKeys("50");
        $x("//div/button[@id=\"add-btn\"]").click();

        //Переход на витрину
        $x("//div/a[@href=\"/\"]").click();

        //Добавление нового товара в корзину
        $x("//div[@data-name='"+productName+"'][@data-price='50']//button[@data-action='add-to-cart']").click();
        $x("//div[@data-name='"+productName+"'][@data-price='50']//button[@data-action='add-to-cart']").click();
        $x("//div[@data-name='"+productName+"'][@data-price='50']//button[@data-action='add-to-cart']").click();

        //Проверка отображения тоста
        $x("//*[@id='open-cart-btn']").click();
        $x("//*[@id = 'makeOrder']").click();
        $x("//div[@class='toast' and text()='Заказ принят в обработку!']").shouldBe(Condition.visible);
    }

    //3.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно.
    @Test
    void task3_2(){

        //Добавляем в корзину
        $x("(//button[@data-action='add-to-cart'])[1]").click();
        $x("(//button[@data-action='add-to-cart'])[2]").click();
        $x("(//button[@data-action='add-to-cart'])[3]").click();

        //Подсчет общей стоимости
        String rawPrice1 = $x("(//button[@data-action='add-to-cart'])[1]").getAttribute("data-price");
        int productPrice1 = Integer.parseInt(rawPrice1);

        String rawPrice2 = $x("(//button[@data-action='add-to-cart'])[2]").getAttribute("data-price");
        int productPrice2 = Integer.parseInt(rawPrice2);

        String rawPrice3 = $x("(//button[@data-action='add-to-cart'])[3]").getAttribute("data-price");
        int productPrice3 = Integer.parseInt(rawPrice3);

        int totalProductPrice = productPrice1 + productPrice2 + productPrice3;

        //Переход в корзину и проверка общей стоимости
        $x("//*[@id='open-cart-btn']").click();
        int totalPrice = Integer.parseInt($x("//span[@id='total-price']").getText());
        Assertions.assertEquals(totalProductPrice, totalPrice, "Сумма в корзине не совпадает с ожидаемой!");
    }

    //3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.
    @Test
    void task3_3(){
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

    //Проверка уведомления
        $x("//div[@class='toast' and text()='Товар успешно добавлен!']").shouldBe(Condition.visible);
    }

    //3.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.
    @Test
    void task3_4(){
        int randomNumber = (int) (Math.random() * 10000);
        String initialName = "API_Товар_" + randomNumber;
        String jsonBody = "{\"name\":\"" + initialName + "\", \"price\":200}";

        //Отправляем POST-запрос с помощью RestAssured
        productToDeleteId = RestAssured.given()
                .auth().basic("admin", "secret123")
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("http://localhost:8080/goods/add")
                .path("data.id");

        //Редактирование созданного товара
        $x("//div/a[@href=\"/admin\"]").click();
        $x("//p/input[@type=\"text\"]").sendKeys("admin");
        $x("//p/input[@type=\"password\"]").sendKeys("secret123");
        $x("//*[@type=\"submit\"]").click();
        Selenide.refresh();

        //Находим в таблице админки товар
        String newName = "Картофель_" + randomNumber;
        $x("//tr[td/input[@value='" + initialName + "']]//input[@type='text']").clear();
        $x("//tr[td/input[@value='" + initialName + "']]//input[@type='text']").sendKeys(newName);
        $x("//tr[td/input[@value='" + initialName + "']]//button[contains(@class, 'btn-upd')]").click();

        //Провека на витрине
        $x("//div/a[@href=\"/\"]").click();
        $x("//div[@class='product-card' and @data-name='" + newName + "']").shouldBe(Condition.visible);

    }

}
