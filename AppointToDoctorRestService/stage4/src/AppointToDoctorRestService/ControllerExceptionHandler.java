package AppointToDoctorRestService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//import javax.servlet.http.HttpServletResponse;
//import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

//@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

        protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        // Just like a POJO, a Map is also converted to a JSON key-value structure
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", "Bad Request");
        body.put("message", "Validation failed");
        body.put("path", "/newDoctor");
        return new ResponseEntity<>(body, headers, status);
    }

        @ExceptionHandler(ConstraintViolationException.class)
        public void constraintViolationException(HttpServletResponse response) throws IOException {
                response.sendError(HttpStatus.BAD_REQUEST.value(),"One of the fields is empty");
        }

}
