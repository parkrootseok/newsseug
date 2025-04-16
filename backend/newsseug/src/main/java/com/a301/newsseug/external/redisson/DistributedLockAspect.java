package com.a301.newsseug.external.redisson;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private static final String REDISSON_LOCK_PREFIX = "SCHEDULER";
    private final RedissonClient redissonClient;

    @Around("@annotation(com.a301.newsseug.external.redisson.DistributedLock)")
    public Object distributedLock(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        // 키 생성 -> PREFIX:METHOD_NAME:DYNAMIC_VALUE
        String key = String.join(
                ":",
                REDISSON_LOCK_PREFIX,
                method.getName(),
                CustomSpringELParser.getDynamicValue(
                        signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key()
                )
        );

        RLock rLock = redissonClient.getLock(key);

        boolean lockable = false;
        try {

            try {
                // 락 획득 시도
                lockable = rLock.tryLock(
                        distributedLock.waitTime(),
                        distributedLock.leaseTime(),
                        TimeUnit.SECONDS
                );
            } catch (Exception e) {
                // 락 획득 실패 시 Optional 리턴
                log.warn("Fail to occupy lock={}", key);
                return Optional.empty();
            }

            log.info("Acquired lock key={}", key);
            return joinPoint.proceed();

        } finally {
            // 락 잡은 경우만 UnLock
            if (lockable && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }

    }

}
