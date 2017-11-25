package com.db.awmd.challenge.domain;

import com.db.awmd.challenge.domain.validators.TransferRequestAmountScale;
import com.db.awmd.challenge.domain.validators.TransferRequestDifferentAccountId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@TransferRequestDifferentAccountId
public class TransferRequest {
	@NotNull
	@NotEmpty
	@Getter
	private final String fromAccountId;

	@NotNull
	@NotEmpty
	@Getter
	private final String toAccountId;

	@NotNull
	@DecimalMin(value = "0.01", message = "Transfer amount must be greater than 0.")
	@TransferRequestAmountScale
	private final BigDecimal amount;

	@JsonCreator
	public TransferRequest(@JsonProperty("fromAccountId") String fromAccountId,
	    @JsonProperty("toAccountId") String toAccountId, @JsonProperty("amount") BigDecimal amount) {
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amount = amount;
	}
}
