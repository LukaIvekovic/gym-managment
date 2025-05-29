package hr.fer.gymmanagment.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiErrorResponse> handleAnyException(Exception e) {
        log.error("An error occurred: ", e);
        var applicationError = new ApiErrorResponse(e.getMessage(), e);

        return ResponseEntity.internalServerError().body(applicationError);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException e) {
        log.warn("Bad request: ", e);
        var applicationError = new ApiErrorResponse(e.getMessage(), e);

        return ResponseEntity.badRequest().body(applicationError);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException e) {
        log.warn("Resource not found: ", e);
        var applicationError = new ApiErrorResponse(e.getMessage(), e);

        return ResponseEntity.status(404).body(applicationError);
    }
}
