package id.ac.ui.cs.advprog.eshop.functional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public class CreateProductFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;
    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    /**
     * Test the creation of a product
     */
    @Test
    public void testCreateProduct(ChromeDriver driver) {
        driver.get(baseUrl + "/product/list");

        // Click on the create button
        driver.findElement(By.linkText("Create Product")).click();

        // Fill in the product form
        driver.findElement(By.id("nameInput")).sendKeys("New Product");
        driver.findElement(By.id("quantityInput")).sendKeys("100");

        // Submit the product form
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify that the product appears
        WebElement productRow = driver.findElement(By.xpath("//tr[td[contains(text(),'New Product')]]"));
        assertNotNull(productRow, "Product row should exist");

        // Verify product name that was created
        WebElement productName = productRow.findElement(By.xpath("./td[1]"));
        assertEquals("New Product", productName.getText(), "Product name should match");

        // Verify product quantity that was created
        WebElement productQuantity = productRow.findElement(By.xpath("./td[2]"));
        assertEquals("100", productQuantity.getText(), "Product quantity should match");
    }

    /**
     * Test the editing of a product
     */
    @Test
    public void testEditProduct(ChromeDriver driver) {
        driver.get(baseUrl + "/product/list");

        // Note: Create the product first
        driver.findElement(By.linkText("Create Product")).click();
        driver.findElement(By.id("nameInput")).sendKeys("Existing Product");
        driver.findElement(By.id("quantityInput")).sendKeys("100");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Note: Edit the product
        WebElement productRow = driver.findElement(By.xpath("//tr[td[contains(text(),'Existing Product')]]"));
        productRow.findElement(By.linkText("Edit")).click();

        // Edit product details: clear and update the name field
        WebElement nameField = driver.findElement(By.id("nameInput"));
        nameField.clear();
        nameField.sendKeys("Existing Product Edited");

        // Submit the changes
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify that the updated product really updated!
        WebElement updatedProduct = driver.findElement(By.xpath("//*[contains(text(), 'Existing Product Edited')]"));
        assertNotNull(updatedProduct, "Edited product should be visible in the list");
    }

    /**
     * Test the deletion of a product
     */
    @Test
    public void testDeleteProduct(ChromeDriver driver) {
        driver.get(baseUrl + "/product/list");

        // Note: Create the product first
        driver.findElement(By.linkText("Create Product")).click();
        driver.findElement(By.id("nameInput")).sendKeys("Product To Delete");
        driver.findElement(By.id("quantityInput")).sendKeys("100");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Note: Delete the product
        WebElement productRow = driver.findElement(By.xpath("//tr[td[contains(text(),'Product To Delete')]]"));
        productRow.findElement(By.xpath(".//button[text()='Delete']")).click();
        driver.switchTo().alert().accept();

        // Wait for the deletion to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that the product is really deleted
        boolean isPresent = driver.findElements(By.xpath("//*[contains(text(), 'Product To Delete')]")).size() > 0;
        assertFalse(isPresent, "Deleted product should not be visible");
    }
}
