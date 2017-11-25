package com.db.awmd.challenge.domain.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransferRequestDifferentAccountIdValidator.class)
public @interface TransferRequestDifferentAccountId {
	String message() default "From account id and To account id must be different";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
