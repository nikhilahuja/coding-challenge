package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferRequest;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InsufficientFundsException;
import com.db.awmd.challenge.exception.InvalidAccountIdException;
import com.db.awmd.challenge.service.AccountsService;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Before
  public void prepare() {
    accountsService.getAccountsRepository().clearAccounts();
  }

  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }
  }

  @Test
  public void transferMoney() {
    String fromAccountId = "Id-123";
    Account fromAccount = new Account(fromAccountId, new BigDecimal("1000"));
    this.accountsService.createAccount(fromAccount);

    String toAccountId = "Id-456";
    Account toAccount = new Account(toAccountId, new BigDecimal("2000"));
    this.accountsService.createAccount(toAccount);

    this.accountsService.transferMoney(new TransferRequest(fromAccountId, toAccountId, new BigDecimal("500")));

    assertThat(this.accountsService.getAccount(fromAccountId).getBalance().equals(new BigDecimal("500")));
    assertThat(this.accountsService.getAccount(toAccountId).getBalance().equals(new BigDecimal("2500")));
  }

  @Test
  public void transferMoney_FromAccountHasInsufficientBalance() {
    String fromAccountId = "Id-123";
    Account fromAccount = new Account(fromAccountId, new BigDecimal("1000"));
    this.accountsService.createAccount(fromAccount);

    String toAccountId = "Id-456";
    Account toAccount = new Account(toAccountId, new BigDecimal("2000"));
    this.accountsService.createAccount(toAccount);

    try {
      this.accountsService.transferMoney(new TransferRequest(fromAccountId, toAccountId, new BigDecimal("2000")));
      fail("Should have failed when transferring funds more than the balance in from account.");
    } catch (InsufficientFundsException ex) {
      assertThat(ex.getMessage()).isEqualTo("Insufficient funds to initiate transfer.");
    }
  }

  @Test
  public void transferMoney_InvalidFromAccountId() {
    String toAccountId = "Id-456";
    Account toAccount = new Account(toAccountId, new BigDecimal("2000"));
    this.accountsService.createAccount(toAccount);

    try {
      this.accountsService.transferMoney(new TransferRequest("Id-123", toAccountId, new BigDecimal("2000")));
      fail("Should have failed when transferring funds from invalid from account id.");
    } catch (InvalidAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Invalid From account id provided.");
    }
  }

  @Test
  public void transferMoney_InvalidToAccountId() {
    String fromAccountId = "Id-123";
    Account fromAccount = new Account(fromAccountId, new BigDecimal("2000"));
    this.accountsService.createAccount(fromAccount);

    try {
      this.accountsService.transferMoney(new TransferRequest(fromAccountId, "Id-456", new BigDecimal("2000")));
      fail("Should have failed when transferring funds from invalid from account id.");
    } catch (InvalidAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Invalid To account id provided.");
    }
  }
}
