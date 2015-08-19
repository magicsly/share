package com.share.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import com.share.service.share_userService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ElNino on 15/6/24.
 */
@Component
@Aspect
public class userLogin {

    @Autowired
    share_userService share_userService;

    @Pointcut("execution(* com.share.service.share_userService.userinfo(..)) ||" +
            "execution(* com.share.service.share_userService.editUser(..))")
    public void isLogin() {}

    @Before("isLogin()")
    public void before(JoinPoint joinPoint) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if(!share_userService.userisLogin(request)) {
            response.sendRedirect("/share/errorlogin");
            return;
        }
    }
}
