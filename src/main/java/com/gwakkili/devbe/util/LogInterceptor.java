package com.gwakkili.devbe.util;

import com.gwakkili.devbe.security.dto.MemberDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        Map<String, String[]> paramMap = request.getParameterMap();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String member = (principal instanceof MemberDetails) ? ((MemberDetails) principal).getUsername() : (String) principal;
        String uuid = UUID.randomUUID().toString();

        MDC.put("uuid", uuid);
        MDC.put("member", member);

        String params = "";
        if (!paramMap.isEmpty()) {
            params = paramMapToString(paramMap);
        }

        log.info("handler[{}], request[method:{}, uri:{}, parameters:({})]", handler, method, requestURI, params);

        return true;
    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s=%s",
                        entry.getKey(), String.join(",", entry.getValue())))
                .collect(Collectors.joining(", "));
    }
}
