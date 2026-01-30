Here's the JUnit 5 test class for the AccountService:

```java
package com.banking.account.service;

import com.banking.account.domain.Account;
import com.banking.core.domain.AccountType;
import com.banking.core.domain.Money;
import com.banking.core.exception.InvalidAccountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private AccountService accountService;
    private static final String CUSTOMER_ID = "12345";
    private static final AccountType ACCOUNT_TYPE = AccountType.SAVINGS;
    private static final Money INITIAL_BALANCE = new Money(new BigDecimal("100.00"));

    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }

    @Test
    void createAccount_ShouldReturnNewAccount() {
        Account account = accountService.createAccount(CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE);

        assertNotNull(account);
        assertEquals(CUSTOMER_ID, account.getCustomerId());
        assertEquals(ACCOUNT_TYPE, account.getAccountType());
        assertEquals(INITIAL_BALANCE, account.getBalance());
    }

    @Test
    void getAccount_WithValidId_ShouldReturnAccount() {
        Account createdAccount = accountService.createAccount(CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE);
        Account retrievedAccount = accountService.getAccount(createdAccount.getAccountId());

        assertEquals(createdAccount, retrievedAccount);
    }

    @Test
    void getAccount_WithInvalidId_ShouldThrowInvalidAccountException() {
        assertThrows(InvalidAccountException.class, () -> accountService.getAccount("invalid_id"));
    }

    @Test
    void getAccountsByCustomer_ShouldReturnListOfCustomerAccounts() {
        Account account1 = accountService.createAccount(CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE);
        Account account2 = accountService.createAccount(CUSTOMER_ID, AccountType.CHECKING, INITIAL_BALANCE);
        accountService.createAccount("other_customer", ACCOUNT_TYPE, INITIAL_BALANCE);

        List<Account> customerAccounts = accountService.getAccountsByCustomer(CUSTOMER_ID);

        assertEquals(2, customerAccounts.size());
        assertTrue(customerAccounts.contains(account1));
        assertTrue(customerAccounts.contains(account2));
    }

    @Test
    void getAllAccounts_ShouldReturnAllAccounts() {
        accountService.createAccount(CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE);
        accountService.createAccount("other_customer", AccountType.CHECKING, INITIAL_BALANCE);

        List<Account> allAccounts = accountService.getAllAccounts();

        assertEquals(2, allAccounts.size());
    }

    @Test
    void deactivateAccount_ShouldDeactivateAccount() {
        Account account = accountService.createAccount(CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE);
        accountService.deactivateAccount(account.getAccountId());

        Account deactivatedAccount = accountService.getAccount(account.getAccountId());
        assertFalse(deactivatedAccount.isActive());
    }

    @Test
    void activateAccount_ShouldActivateAccount() {
        Account account = accountService.createAccount(CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE);
        accountService.deactivateAccount(account.getAccountId());
        accountService.activateAccount(account.getAccountId());

        Account activatedAccount = accountService.getAccount(account.getAccountId());
        assertTrue(activatedAccount.isActive());
    }

    @Test
    void getBalance_ShouldReturnAccountBalance() {
        Account account = accountService.createAccount(CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE);
        Money balance = accountService.getBalance(account.getAccountId());

        assertEquals(INITIAL_BALANCE, balance);
    }
}
```
