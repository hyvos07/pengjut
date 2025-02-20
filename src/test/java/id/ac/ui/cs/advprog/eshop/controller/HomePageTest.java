package id.ac.ui.cs.advprog.eshop.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomePageTest {
    @Test
    public void testHomePage() {
        HomePageController homeController = new HomePageController();
        String viewName = homeController.homePage();
        assertEquals("HomePage", viewName);
    }
}
