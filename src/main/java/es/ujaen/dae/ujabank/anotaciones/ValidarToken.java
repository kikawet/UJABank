/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.anotaciones;

import antlr.Token;
import es.ujaen.dae.ujabank.validators.TokenManager;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author flo00008
 *
 * https://www.baeldung.com/spring-mvc-custom-validator
 * https://www.baeldung.com/spring-validate-requestparam-pathvariable
 */
@Documented
@Constraint(validatedBy = TokenManager.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidarToken {

    String message() default "Token no valido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
