package com.db.awmd.challenge.domain.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransferRequestAmountScaleValidator.class)
public @interface TransferRequestAmountScale {
	String message() default "Amount cannot have more than 2 decimal digits.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
