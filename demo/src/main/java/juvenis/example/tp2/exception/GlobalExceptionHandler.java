package juvenis.example.tp2.exception; 

import java.time.LocalDateTime; 

import java.util.LinkedHashMap; 

import java.util.Map; 

import org.springframework.http.HttpStatus; 

import org.springframework.http.ResponseEntity; 

import org.springframework.web.bind.MethodArgumentNotValidException; 

import org.springframework.web.bind.annotation.ExceptionHandler; 

import org.springframework.web.bind.annotation.RestControllerAdvice; 

import jakarta.servlet.http.HttpServletRequest; 

@RestControllerAdvice 

public class GlobalExceptionHandler { 

    @ExceptionHandler(ZeroDivisionException.class) 

    public ResponseEntity<ApiError> handleDivisionByZero( 

    		ZeroDivisionException exception, 

            HttpServletRequest request) { 

        ApiError error = new ApiError( 

                LocalDateTime.now(), 

                HttpStatus.BAD_REQUEST.value(), 

                HttpStatus.BAD_REQUEST.getReasonPhrase(), 

                exception.getMessage(), 

                request.getRequestURI() 

        ); 

        return ResponseEntity 

                .status(HttpStatus.BAD_REQUEST) 

                .body(error); 

    } 

    @ExceptionHandler(ManualConversionException.class) 

    public ResponseEntity<ApiError> handleIllegalArgument( 

    		ManualConversionException exception, 

            HttpServletRequest request) { 

        ApiError error = new ApiError( 

                LocalDateTime.now(), 

                HttpStatus.BAD_REQUEST.value(), 

                HttpStatus.BAD_REQUEST.getReasonPhrase(), 

                exception.getMessage(), 

                request.getRequestURI() 

        ); 

        return ResponseEntity 

                .status(HttpStatus.BAD_REQUEST) 

                .body(error); 

    } 

    @ExceptionHandler(ResourceNotFoundException.class) 

    public ResponseEntity<ApiError> handleResourceNotFound( 

            ResourceNotFoundException exception, 

            HttpServletRequest request) { 

        ApiError error = new ApiError( 

                LocalDateTime.now(), 

                HttpStatus.NOT_FOUND.value(), 

                HttpStatus.NOT_FOUND.getReasonPhrase(), 

                exception.getMessage(), 

                request.getRequestURI() 

        ); 

        return ResponseEntity 

                .status(HttpStatus.NOT_FOUND) 

                .body(error); 

    } 

    @ExceptionHandler(ExternalServiceException.class) 

    public ResponseEntity<ApiError> handleExternalService( 

            ExternalServiceException exception, 

            HttpServletRequest request) { 

        ApiError error = new ApiError( 

                LocalDateTime.now(), 

                HttpStatus.SERVICE_UNAVAILABLE.value(), 

                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), 

                exception.getMessage(), 

                request.getRequestURI() 

        ); 

        return ResponseEntity 

                .status(HttpStatus.SERVICE_UNAVAILABLE) 

                .body(error); 

    } 

    @ExceptionHandler(MethodArgumentNotValidException.class) 

    public ResponseEntity<Map<String, Object>> handleValidationErrors( 

            MethodArgumentNotValidException exception, 

            HttpServletRequest request) { 

        Map<String, String> validationErrors = 

                new LinkedHashMap<>(); 

        exception.getBindingResult() 

                .getFieldErrors() 

                .forEach(fieldError -> 

                        validationErrors.put( 

                                fieldError.getField(), 

                                fieldError.getDefaultMessage() 

                        ) 

                ); 

        Map<String, Object> response = new LinkedHashMap<>(); 

        response.put("timestamp", LocalDateTime.now()); 

        response.put("status", HttpStatus.BAD_REQUEST.value()); 

        response.put( 

                "error", 

                HttpStatus.BAD_REQUEST.getReasonPhrase() 

        ); 

        response.put( 

                "message", 

                "Les données envoyées sont invalides" 

        ); 

        response.put("validationErrors", validationErrors); 

        response.put("path", request.getRequestURI()); 

        return ResponseEntity 

                .status(HttpStatus.BAD_REQUEST) 

                .body(response); 

    } 

} 

 