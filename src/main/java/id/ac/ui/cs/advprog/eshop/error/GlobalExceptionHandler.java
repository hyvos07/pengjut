package id.ac.ui.cs.advprog.eshop.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Date;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException e, Model model) {
        logger.error("Resource not found: ", e);
        populateError(model, HttpStatus.NOT_FOUND, e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleWrongMethod(HttpRequestMethodNotSupportedException e, Model model) {
        logger.error("Method not allowed: ", e);
        String message = "The " + e.getMethod() + " method is not supported for this request. " +
                "Supported methods are: " + String.join(", ", Objects.requireNonNull(e.getSupportedMethods()));
        populateError(model, HttpStatus.METHOD_NOT_ALLOWED, message);
        return "error/405";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception e, Model model) {
        logger.error("Unexpected error: ", e);
        populateError(model, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        return "error/500";
    }

    private void populateError(Model model, HttpStatus status, String message) {
        model.addAttribute("timestamp", new Date());
        model.addAttribute("status", status.value());
        model.addAttribute("error", status.getReasonPhrase());
        model.addAttribute("message", message);
    }
}