package com.example.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Order(10)
@Slf4j
@Aspect
@Component
public class CacheAspectFirst {

    @SneakyThrows
    @Around("@annotation(myCacheable)")
    public Object cacheableAdvice(final ProceedingJoinPoint joinPoint, final MyCacheable myCacheable) {
        log.debug("FIRST Before cacheable advice for method: {}", joinPoint.getSignature());
        Object result = joinPoint.proceed();
        log.debug("FIRST After cacheable advice for method: {}", joinPoint.getSignature());

        return result;
    }
}
