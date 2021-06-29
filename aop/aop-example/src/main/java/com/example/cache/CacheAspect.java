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
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Order(6)
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {

    private static final int NON_INITIALIZED_INDEX = -1;

    @Value("${cache.max-size:2}")
    private final int maxCacheSize;
    @Value("${cache.expire-after-seconds:10}")
    private final long expireAfterSeconds;
    private final ConcurrentMap<String, Cache<Object, Object>> cache = new ConcurrentHashMap<>();

    @SneakyThrows
    @Around("@annotation(myCacheable)")
    public Object cacheableAdvice(final ProceedingJoinPoint joinPoint, final MyCacheable myCacheable) {
        log.debug("Before cacheable advice for method: {}", joinPoint.getSignature());

        final Object[] args = joinPoint.getArgs();
        final Cache<Object, Object> concreteCache = cache.computeIfAbsent(myCacheable.cacheName(), cacheName -> buildCache());
        final Object key = args[myCacheable.keyIndex()];
        Object cachedResult = concreteCache.getIfPresent(key);

        if (cachedResult == null) {
            log.debug("Cache {} is empty for key {}. Call method", myCacheable.cacheName(), key);
            cachedResult = joinPoint.proceed();
            concreteCache.put(key, cachedResult);
        }

        log.debug("After cacheable advice for method: {}", joinPoint.getSignature());

        return cachedResult;
    }

    @After("@annotation(myCacheEvict)")
    public void cacheEvictAdvice(final JoinPoint joinPoint, final MyCacheEvict myCacheEvict) {
        log.debug("Before cacheEvict advice for method: {}", joinPoint.getSignature());

        Assert.isTrue(myCacheEvict.fullClean() ^ (myCacheEvict.keyIndex() > NON_INITIALIZED_INDEX), "CacheEvict bad init");

        final String cacheName = myCacheEvict.cacheName();
        final Cache<Object, Object> concreteCache = cache.get(cacheName);
        if (concreteCache != null) {
            if (myCacheEvict.fullClean()) {
                log.debug("Full clean for cache {}", cacheName);
                concreteCache.cleanUp();
            } else {
                final Object[] args = joinPoint.getArgs();
                final Object key = args[myCacheEvict.keyIndex()];

                log.debug("Clean cache {} for key {}", cacheName, key);
                concreteCache.invalidate(key);
            }
        }

        log.debug("After cacheEvict advice for method: {}", joinPoint.getSignature());
    }

    private Cache<Object, Object> buildCache() {
        return CacheBuilder.newBuilder()
            .maximumSize(maxCacheSize)
            .expireAfterAccess(expireAfterSeconds, TimeUnit.SECONDS)
            .removalListener(
                notification -> log.debug("Remove item by key {} with value {}. reason {}", notification.getKey(), notification.getValue(), notification.getCause())
            )
            .build();
    }

}
