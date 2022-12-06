package security.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD})
public @interface RequiresProtection {

  String[] roles() default "";

  String userIdentifierFrom() default "path";

  String userIdentifierParamName() default "users";

}
