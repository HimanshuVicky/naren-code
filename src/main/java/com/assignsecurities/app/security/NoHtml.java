package com.assignsecurities.app.security;



import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoHtmlValidator.class)
@Documented
public @interface NoHtml {
	// TODO use a better message, look up ValidationMEssages.properties
    String message() default "{com.assignsecurities.constraints.nohtml}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
