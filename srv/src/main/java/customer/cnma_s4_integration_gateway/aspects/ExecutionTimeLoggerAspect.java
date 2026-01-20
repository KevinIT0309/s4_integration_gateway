package customer.cnma_s4_integration_gateway.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import customer.cnma_s4_integration_gateway.annotations.ProconarumLogExecutionTime;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ExecutionTimeLoggerAspect {
    
    @PostConstruct
    public void init() {
        System.out.println("✅ ExecutionTimeLoggerAspect initialized.");
    }
    
    @Around("@annotation(logExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, ProconarumLogExecutionTime logExecutionTime) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();  // proceed to method call

        long duration = System.currentTimeMillis() - start;
        log.info("⏱️ [{}] executed in {} ms", joinPoint.getSignature().getName(), duration);

        return result;
    }
}
