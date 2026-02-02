package com.banking.account.service;

import com.banking.account.domain.Account;
import com.banking.core.domain.AccountType;
import com.banking.core.domain.Money;
import com.banking.core.exception.InvalidAccountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private AccountService accountService;
    private static final String CUSTOMER_ID = "customer123";
    private static final Money INITIAL_BALANCE = new Money(BigDecimal.valueOf(100), "USD");

    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void testCreateAccount(AccountType accountType) {
        Account account = accountService.createAccount(CUSTOMER_ID, accountType, INITIAL_BALANCE);

        assertNotNull(account);
        assertEquals(CUSTOMER_ID, account.getCustomerId());
        assertEquals(accountType, account.getAccountType());
        assertEquals(INITIAL_BALANCE, account.getBalance());
        assertTrue(account.isActive());
    }

    @Test
    void testGetAccount() {
        Account createdAccount = accountService.createAccount(CUSTOMER_ID, AccountType.CHECKING, INITIAL_BALANCE);
        Account retrievedAccount = accountService.getAccount(createdAccount.getAccountId());

        assertEquals(createdAccount, retrievedAccount);
    }

    @Test
    void testGetAccount_InvalidAccountId() {
        assertThrows(InvalidAccountException.class, () -> accountService.getAccount("nonexistent"));
    }

    @Test
    void testGetAccountsByCustomer() {
        accountService.createAccount(CUSTOMER_ID, AccountType.CHECKING, INITIAL_BALANCE);
        accountService.createAccount(CUSTOMER_ID, AccountType.SAVINGS, INITIAL_BALANCE);
        accountService.createAccount("otherCustomer", AccountType.CHECKING, INITIAL_BALANCE);

        List<Account> customerAccounts = accountService.getAccountsByCustomer(CUSTOMER_ID);

        assertEquals(2, customerAccounts.size());
        assertTrue(customerAccounts.stream().allMatch(account -> account.getCustomerId().equals(CUSTOMER_ID)));
    }

    @Test
    void testGetAllAccounts() {
        accountService.createAccount(CUSTOMER_ID, AccountType.CHECKING, INITIAL_BALANCE);
        accountService.createAccount("otherCustomer", AccountType.SAVINGS, INITIAL_BALANCE);

        List<Account> allAccounts = accountService.getAllAccounts();

        assertEquals(2, allAccounts.size());
    }

    @Test
    void testDeactivateAccount() {
        Account account = accountService.createAccount(CUSTOMER_ID, AccountType.CHECKING, INITIAL_BALANCE);
        accountService.deactivateAccount(account.getAccountId());

        assertFalse(accountService.getAccount(account.getAccountId()).isActive());
    }

    @Test
    void testActivateAccount() {
        Account account = accountService.createAccount(CUSTOMER_ID, AccountType.CHECKING, INITIAL_BALANCE);
        accountService.deactivateAccount(account.getAccountId());
        accountService.activateAccount(account.getAccountId());

        assertTrue(accountService.getAccount(account.getAccountId()).isActive());
    }

    @Test
    void testGetBalance() {
        Account account = accountService.createAccount(CUSTOMER_ID, AccountType.CHECKING, INITIAL_BALANCE);
        Money balance = accountService.getBalance(account.getAccountId());

        assertEquals(INITIAL_BALANCE, balance);
    }

    @Test
    void testGetBalance_InvalidAccountId() {
        assertThrows(InvalidAccountException.class, () -> accountService.getBalance("nonexistent"));
    }

    @Test
    void testDeactivateAccount_InvalidAccountId() {
        assertThrows(InvalidAccountException.class, () -> accountService.deactivateAccount("nonexistent"));
    }

    @Test
    void testActivateAccount_InvalidAccountId() {
        assertThrows(InvalidAccountException.class, () -> accountService.activateAccount("nonexistent"));
    }

    @Test
    void testGetAccountsByCustomer_NoAccounts() {
        List<Account> customerAccounts = accountService.getAccountsByCustomer("nonexistentCustomer");

        assertTrue(customerAccounts.isEmpty());
    }

    @Test
    void testGetAllAccounts_NoAccounts() {
        List<Account> allAccounts = accountService.getAllAccounts();

        assertTrue(allAccounts.isEmpty());
    }
}
