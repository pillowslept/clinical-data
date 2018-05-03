package co.edu.itm.clinicaldata.mapper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import co.edu.itm.clinicaldata.exception.ValidateException;

@ControllerAdvice
public class Mapper extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(Mapper.class
            .getName());

    @Autowired
    private Environment environment;

    @ExceptionHandler(ValidateException.class)
    protected ResponseEntity<Object> handleEntityNotFound(ValidateException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        LOGGER.error(ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Object> handleError1(MultipartException ex) {
        String maxFileSize = environment
                .getRequiredProperty("spring.http.multipart.max-file-size");
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                String.format(
                        "El tamaño del archivo no puede superar el límite establecido de <%s>",
                        maxFileSize));
        LOGGER.error(ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
