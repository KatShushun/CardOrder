package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardApplicationNegativeTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
//        System.setProperty("webdriver.chrome.driver", "./driver/win/chromedriver.exe"); // запуск драйвера, сейчас
//        это делает драйверменеджер, см ниже
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
//отключаем графический интерфейс:
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

//        Для selenide headless-режим активируется в .appveyor.yml (см туда)
//        driver = new ChromeDriver(); // вариант если не отключаем графический интерфейс

    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldTestEmptyName() {
        driver.get("http://localhost:9999");

        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79111111111");
//        Thread.sleep(5000); замедление процесса Чтобы что-то успеть увидеть
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();

        assertEquals("Поле обязательно для заполнения", driver.findElement(By.cssSelector(".input_invalid[data-test" +
                "-id=name] .input__sub")).getText().trim());
//        другой вариант того же самого, но не так оптимально:
//        WebElement form = driver.findElement(By.cssSelector(".input_invalid[data-test-id=phone]"));
//        String text = form.findElement(By.className("input__sub")).getText();
//        assertEquals("Поле обязательно для заполнения", text.trim());

    }

    @Test
    void shouldTestEmptyPhone() {
        driver.get("http://localhost:9999");

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();

        assertEquals("Поле обязательно для заполнения", driver.findElement(By.cssSelector(".input_invalid[data-test" +
                "-id=phone] .input__sub")).getText().trim());
//        WebElement form = driver.findElement(By.cssSelector(".input_invalid[data-test-id=phone]"));
//        String text = form.findElement(By.className("input__sub")).getText();
//        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldTestLatinName() {
        driver.get("http://localhost:9999");

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("yyyyyyuuuuiii");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79111111111");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();

        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", driver.findElement(By.cssSelector(".input_invalid[data-test" +
                "-id=name] .input__sub")).getText().trim());
    }
}
