package com.example.WebUIAutomation;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCases {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Sesuaikan path ke chromedriver sesuai dengan lingkungan Anda
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSuccessfulLogin() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("inventory.html"));

        List<WebElement> items = driver.findElements(By.className("inventory_item"));
        assertFalse(items.isEmpty());
    }

    @Test
    public void testFailedLoginWithWrongPassword() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector("h3[data-test='error']"));
        assertEquals("Username and password do not match any user in this service", errorMessage.getText());
    }

    @Test
    public void testSortingItemsFromHighToLowPrice() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement sortDropdown = driver.findElement(By.className("product_sort_container"));
        sortDropdown.click();
        sortDropdown.findElement(By.xpath("//option[@value='hilo']")).click();

        List<WebElement> itemPrices = driver.findElements(By.className("inventory_item_price"));
        double firstItemPrice = Double.parseDouble(itemPrices.get(0).getText().replace("$", ""));
        double secondItemPrice = Double.parseDouble(itemPrices.get(1).getText().replace("$", ""));
        assertTrue(firstItemPrice > secondItemPrice);
    }

    @Test
    public void testCheckout() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();

        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();

        WebElement successMessage = driver.findElement(By.className("complete-header"));
        assertEquals("THANK YOU FOR YOUR ORDER", successMessage.getText());
    }
}
