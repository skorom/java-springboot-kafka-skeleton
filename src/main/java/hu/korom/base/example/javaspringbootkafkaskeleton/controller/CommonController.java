package hu.korom.base.example.javaspringbootkafkaskeleton.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import hu.korom.base.example.javaspringbootkafkaskeleton.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * A global exception handling class on the top of the REST controllers.
 */
@ControllerAdvice
public class CommonController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    ResponseEntity<Object> handleException(HttpServletRequest request, Throwable ex) {
        log.error("Exception reaching REST endpoint: ", ex);
        final String causeMessage = Optional.ofNullable(ex.getCause())
                .map(e->String.format("%s: %s", e.getClass().getCanonicalName(),e.getMessage()))
                .orElse("No further cause");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(new ErrorResponse(ex.getMessage(),"900",causeMessage));
    }
}
