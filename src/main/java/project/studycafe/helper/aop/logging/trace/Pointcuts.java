package project.studycafe.helper.aop.logging.trace;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    //project.studycafe.app 패키지와 하위 패키지
    @Pointcut("execution(* project.studycafe.app..*(..))")
    public void allAppProject() {
    } //pointcut signature

    @Pointcut("execution(* project.studycafe.helper..*(..))")
    public void allHelperProject() {
    }

    // project.studycafe.controller 패키지와 하위 패키지
    @Pointcut("execution(* project.studycafe.app.controller..*(..))")
    public void allController() {

    } //pointcut signature

    // project.studycafe.service 패키지와 하위 패키지
    @Pointcut("execution(* project.studycafe.app.service..*(..))")
    public void allService() {
    } //pointcut signature

    // project.studycafe.repository 패키지와 하위 패키지
    @Pointcut("execution(* project.studycafe.app.repository..*(..))")
    public void allRepository() {
    } //pointcut signature

    //클래스 이름 패턴이 *Repository
    @Pointcut("execution(* *..*Repository.*(..))")
    public void nameRepository() {
    }

    //클래스 이름 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    public void nameService() {
    }

    //    클래스 이름 패턴이 *Controller
    @Pointcut("execution(* *..*Controller.*(..))")
    public void nameController() {
    }


    //allOrder && allService
    @Pointcut("allController() && allService() && allRepository()")
    public void ControllerAndServiceAndRepository() {
    }

    @Pointcut("allHelperProject() && allAppProject()")
    public void allAppAndHelperProject() {}
}
