package id.ac.ui.cs.advprog.eshop.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler globalExceptionHandler;
    private Model model;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        model = mock(Model.class);
    }

    @Test
    void testHandleResourceNotFoundException() {
        // Setup
        ResourceNotFoundException ex = new ResourceNotFoundException("Test resource not found");

        // Execute
        String viewName = globalExceptionHandler.handleNotFound(ex, model);

        // Verify
        assertEquals("error/404", viewName);
        verifyModelAttributes(HttpStatus.NOT_FOUND, "Test resource not found");
    }

    @Test
    void testHandleHttpRequestMethodNotSupportedException() {
        // Setup
        HttpRequestMethodNotSupportedException ex =
                new HttpRequestMethodNotSupportedException("POST", List.of("GET", "PUT"));

        // Execute
        String viewName = globalExceptionHandler.handleWrongMethod(ex, model);

        // Verify
        assertEquals("error/405", viewName);
        String expectedMessage = "The POST method is not supported for this request. Supported methods are: GET, PUT";
        verifyModelAttributes(HttpStatus.METHOD_NOT_ALLOWED, expectedMessage);
    }

    @Test
    void testHandleGeneralException() {
        // Setup
        Exception ex = new Exception("Unexpected error");

        // Execute
        String viewName = globalExceptionHandler.handleGeneralError(ex, model);

        // Verify
        assertEquals("error/500", viewName);
        verifyModelAttributes(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private void verifyModelAttributes(HttpStatus expectedStatus, String expectedMessage) {
        verify(model).addAttribute(eq("timestamp"), any(Date.class));
        verify(model).addAttribute("status", expectedStatus.value());
        verify(model).addAttribute("error", expectedStatus.getReasonPhrase());
        verify(model).addAttribute("message", expectedMessage);
    }
}