package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferRequest;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.db.awmd.challenge.exception.InsufficientFundsException;
import com.db.awmd.challenge.exception.InvalidAccountIdException;
import com.db.awmd.challenge.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts;
  private final NotificationService notificationService;

  @Autowired
  public AccountsRepositoryInMemory(NotificationService notificationService) {
    this.notificationService = notificationService;
    accounts = new ConcurrentHashMap<>();
  }

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }

  /*
  * Performs the transfer of money from one account to another. The complete transaction needs to be in
  * synchronized block to make it Thread safe.
  *
  * @param transferRequest containing information of From account id, To account id and the amount to be transferred.
  *
  * @throws InsufficientFundsException if insufficient funds are available in From Account Id.
  * @throws InvalidAccountIdException if invalid from account id or to account id is provided.
  * */
  @Override
  public synchronized void transferMoney(TransferRequest transferRequest) throws InsufficientFundsException, InvalidAccountIdException {
    verifyAccountIds(transferRequest);

    Account fromAccount = getAccount(transferRequest.getFromAccountId());
    Account toAccount = getAccount(transferRequest.getToAccountId());

    // Initiate transfer
    fromAccount.debit(transferRequest.getAmount());
    toAccount.credit(transferRequest.getAmount());

    // Notify account holders about the transfer
    notificationService.notifyAboutTransfer(fromAccount, "Transfer of amount " + transferRequest.getAmount() + " " +
	          "to account " + toAccount.getAccountId() + " is completed.");
    notificationService.notifyAboutTransfer(toAccount, transferRequest.getAmount() + " is credited to your a/c " +
	          "by transfer from account " + fromAccount.getAccountId());
	return;
  }

	/* Check if FromAccountID & ToAccountID is valid.
	*
	* @param transferRequest containing FromAccountID and ToAccountID
	*
	* @throws InvalidAccountIdException if FromAccountID or ToAccountID is invalid.
	* */
  private void verifyAccountIds(TransferRequest transferRequest) throws InvalidAccountIdException {
    if(!isValidAccountId(transferRequest.getFromAccountId())) {
      throw new InvalidAccountIdException("Invalid From account id provided.");
    }
    if(!isValidAccountId(transferRequest.getToAccountId())) {
      throw new InvalidAccountIdException("Invalid To account id provided.");
    }
  }

  private boolean isValidAccountId(String accountId) {
    return getAccount(accountId) != null;
  }

}
