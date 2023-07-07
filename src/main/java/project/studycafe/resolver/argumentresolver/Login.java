package project.studycafe.resolver.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)// 파라미터에 붙이는 애너테이션이다.
@Retention(RetentionPolicy.RUNTIME) //실제 런타임 실행할때까지 런타임 살아 있으라는 어노테이션(그랴여 동적으로 읽을 수 있다)
public @interface Login {
}
