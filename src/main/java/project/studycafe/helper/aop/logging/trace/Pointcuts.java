package project.studycafe.helper.aop.logging.trace;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    //project.studycafe.app 패키지와 하위 패키지
    @Pointcut("execution(* project.studycafe.app..*(..))")
    public void allAppProject() {} //pointcut signature

    @Pointcut("execution(* project.studycafe.helper..*(..))")
    public void allHelperProject() {}

    @Pointcut("execution(* project.studycafe.helper.handler..*(..))")
    public void allHandlerProject() {}

    @Pointcut("execution(* project.studycafe.helper.interceptor..*(..))")
    public void allInterceptorProject() {}

    @Pointcut("execution(* project.studycafe.app.controller..*(..))")
    public void allController() {}

    @Pointcut("execution(* project.studycafe.app.service..*(..))")
    public void allService() {}

    @Pointcut("execution(* project.studycafe.app.repository..*(..))")
    public void allRepository() {}

    @Pointcut("execution(* *..*Repository.*(..))")
    public void nameRepository() {}

    @Pointcut("execution(* *..*Service.*(..))")
    public void nameService() {}

    //    클래스 이름 패턴이 *Controller
    @Pointcut("execution(* *..*Controller.*(..))")
    public void nameController() {}

    @Pointcut("allController() && allService() && allRepository()")
    public void ControllerAndServiceAndRepository() {}

    @Pointcut("allHelperProject() && allAppProject()")
    public void allAppAndHelperProject() {}

    @Pointcut("allHandlerProject() && allAppProject()")
    public void allAppAndHandlerProject() {}

    @Pointcut("allInterceptorProject()Project() && allAppProject()")
    public void allAppAndInterceptorProject() {}
}
