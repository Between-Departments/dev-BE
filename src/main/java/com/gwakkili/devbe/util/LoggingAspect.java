package com.gwakkili.devbe.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.security.dto.MemberDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("execution(public * com.gwakkili.devbe.*.controller.*.*(..))")
    private void cut() {
    }

    @Before("cut()")
    public void beforeRequestLog(JoinPoint joinPoint) {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        Map<String, String[]> paramMap = request.getParameterMap();

        String params = "";
        if (!paramMap.isEmpty()) {
            params = paramMapToString(paramMap);
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String member = (principal instanceof MemberDetails) ? ((MemberDetails) principal).getUsername() : (String) principal;
        String uuid = UUID.randomUUID().toString();
        MDC.put("uuid", uuid);
        MDC.put("member", member);
        String handler = joinPoint.getSignature().toShortString();
        log.info("handler[{}], request[method:{}, uri:{}, parameters:({})]", handler, method, requestURI, params);
    }

    @AfterThrowing(pointcut = "cut()", throwing = "exception")
    public void afterExceptionLog(JoinPoint joinPoint, Exception exception) {
        String handler = joinPoint.getSignature().toShortString();
        log.error("handler[{}], exception[{}]", handler, exception.toString());
    }

    @AfterReturning(pointcut = "cut()", returning = "returnValue")
    public void afterReturnLog(JoinPoint joinPoint, Object returnValue) throws IOException {
        String handler = joinPoint.getSignature().toShortString();
        String body = objectMapper.writeValueAsString(returnValue);
        log.info("handler[{}] returnValue[{}]", handler, body);

    }


    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s=%s",
                        entry.getKey(), String.join(",", entry.getValue())))
                .collect(Collectors.joining(", "));
    }
}
