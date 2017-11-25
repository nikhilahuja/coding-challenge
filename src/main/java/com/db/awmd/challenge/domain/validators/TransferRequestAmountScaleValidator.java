package com.db.awmd.challenge.domain.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class TransferRequestAmountScaleValidator implements ConstraintValidator<TransferRequestAmountScale, BigDecimal> {
	@Override
	public void initialize(TransferRequestAmountScale constraintAnnotation) {
		// No initialization needed
	}

	@Override
	public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
		/*
		* If Amount scale is more than 2, return FALSE. Otherwise return TRUE.
		* */
		if (value.scale() > 2) {
			return false;
		}
		return true;
	}
}
