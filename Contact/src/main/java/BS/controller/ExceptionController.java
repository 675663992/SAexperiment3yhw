package BS.controller;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = NotLoginException.class)
    public SaResult catchNotLoginException(NotLoginException e) {
        return SaResult.get(11011, e.getMessage(), null);
    }
}
