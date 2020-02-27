package com.kakaopaysec.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionMessage {

    public Map<String, String> errMsg(String msg, String errCode){
        Map<String, String> result = new HashMap<>();
        result.put("code", errCode);
        result.put("메세지", msg);
        return result;

    }

}
