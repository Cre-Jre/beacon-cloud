package com.luoyurui.api.advice;

import com.luoyurui.api.util.R;
import com.luoyurui.api.vo.ResultVO;
import com.luoyurui.common.exception.ApiException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResultVO apiException(ApiException ex){
        return R.error(ex);
    }
}
