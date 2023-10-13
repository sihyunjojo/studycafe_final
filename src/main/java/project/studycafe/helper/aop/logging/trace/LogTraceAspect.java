package project.studycafe.helper.aop.logging.trace;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import project.studycafe.helper.aop.logging.trace.object.TraceStatus;

// 빈으로 등록해줘야함.
@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace trace;

    public LogTraceAspect(LogTrace logTrace) {
        this.trace = logTrace;
    }

    @Around("project.studycafe.helper.aop.logging.trace.Pointcuts.allAppProject()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            status = trace.begin(message);

            //로직 호출
            Object result = joinPoint.proceed();

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

//    @Before("project.studycafe.helper.aop.logging.trace.Pointcuts.ControllerAndServiceAndRepository()")
//    public void doBefore(JoinPoint joinPoint) {
//        log.info("[before] {}", joinPoint.getSignature());
//    }
//
//    @AfterReturning(value = "project.studycafe.helper.aop.logging.trace.Pointcuts.ControllerAndServiceAndRepository()", returning = "result")
//    public void doReturn(JoinPoint joinPoint, Object result) {
//        log.info("[return] {} return={}", joinPoint.getSignature(), result);
//    }
//
//    @AfterThrowing(value = "project.studycafe.helper.aop.logging.trace.Pointcuts.ControllerAndServiceAndRepository()", throwing = "ex")
//    public void doThrowing(JoinPoint joinPoint, Exception ex) {
//        log.info("[ex] {} message={}", ex);
//    }
//
//    @After(value = "project.studycafe.helper.aop.logging.trace.Pointcuts.ControllerAndServiceAndRepository()")
//    public void doAfter(JoinPoint joinPoint) {
//        log.info("[after] {}", joinPoint.getSignature());
//    }
}
