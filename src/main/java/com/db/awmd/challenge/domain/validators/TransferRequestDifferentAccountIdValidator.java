package com.db.awmd.challenge.domain.validators;

import com.db.awmd.challenge.domain.TransferRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TransferRequestDifferentAccountIdValidator implements
		ConstraintValidator<TransferRequestDifferentAccountId, TransferRequest> {
	@Override
	public void initialize(TransferRequestDifferentAccountId constraintAnnotation) {
		// No initialization needed
	}

	@Override
	public boolean isValid(TransferRequest value, ConstraintValidatorContext context) {
		/*
		* If From Account Id is same as To Account Id, return FALSE. Otherwise return TRUE.
		* */
		if(value.getFromAccountId().equals(value.getToAccountId())) {
			return false;
		}
		return true;
	}
}
