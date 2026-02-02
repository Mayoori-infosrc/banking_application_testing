package com.banking.account.domain;

import com.banking.core.domain.AccountType;
import com.banking.core.domain.Money;
import com.banking.core.exception.InsufficientFundsException;
import com.banking.core.exception.InvalidAccountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private static final String VALID_CUSTOMER_ID = "customer123";
    private static final AccountType VALID_ACCOUNT_TYPE = AccountType.SAVINGS;
    private static final Money VALID_INITIAL_BALANCE = new Money(BigDecimal.valueOf(100), "USD");

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(VALID_CUSTOMER_ID, VALID_ACCOUNT_TYPE, VALID_INITIAL_BALANCE);
    }

    @Test
    void testConstructorWithValidInputs() {
        assertNotNull(account.getAccountId());
        assertEquals(VALID_CUSTOMER_ID, account.getCustomerId());
        assertEquals(VALID_ACCOUNT_TYPE, account.getAccountType());
        assertEquals(VALID_INITIAL_BALANCE, account.getBalance());
        assertTrue(account.isActive());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void testConstructorWithInvalidCustomerId(String invalidCustomerId) {
        assertThrows(IllegalArgumentException.class, () -> 
            new Account(invalidCustomerId, VALID_ACCOUNT_TYPE, VALID_INITIAL_BALANCE));
    }

    @Test
    void testConstructorWithNullAccountType() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Account(VALID_CUSTOMER_ID, null, VALID_INITIAL_BALANCE));
    }

    @Test
    void testConstructorWithNullInitialBalance() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Account(VALID_CUSTOMER_ID, VALID_ACCOUNT_TYPE, null));
    }

    @Test
    void testDepositWithValidAmount() {
        Money depositAmount = new Money(BigDecimal.valueOf(50), "USD");
        account.deposit(depositAmount);
        assertEquals(new Money(BigDecimal.valueOf(150), "USD"), account.getBalance());
    }

    @Test
    void testDepositWithNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(null));
    }

    @Test
    void testDepositWithZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> 
            account.deposit(new Money(BigDecimal.ZERO, "USD")));
    }

    @Test
    void testDepositWithNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> 
            account.deposit(new Money(BigDecimal.valueOf(-50), "USD")));
    }

    @Test
    void testWithdrawWithValidAmount() {
        Money withdrawAmount = new Money(BigDecimal.valueOf(50), "USD");
        account.withdraw(withdrawAmount);
        assertEquals(new Money(BigDecimal.valueOf(50), "USD"), account.getBalance());
    }

    @Test
    void testWithdrawWithNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(null));
    }

    @Test
    void testWithdrawWithZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> 
            account.withdraw(new Money(BigDecimal.ZERO, "USD")));
    }

    @Test
    void testWithdrawWithNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> 
            account.withdraw(new Money(BigDecimal.valueOf(-50), "USD")));
    }

    @Test
    void testWithdrawWithInsufficientFunds() {
        assertThrows(InsufficientFundsException.class, () -> 
            account.withdraw(new Money(BigDecimal.valueOf(150), "USD")));
    }

    @Test
    void testDeactivateAndActivate() {
        account.deactivate();
        assertFalse(account.isActive());

        account.activate();
        assertTrue(account.isActive());
    }

    @Test
    void testDepositOnInactiveAccount() {
        account.deactivate();
        assertThrows(InvalidAccountException.class, () -> 
            account.deposit(new Money(BigDecimal.valueOf(50), "USD")));
    }

    @Test
    void testWithdrawOnInactiveAccount() {
        account.deactivate();
        assertThrows(InvalidAccountException.class, () -> 
            account.withdraw(new Money(BigDecimal.valueOf(50), "USD")));
    }

    @Test
    void testEqualsAndHashCode() {
        Account sameAccount = new Account(account.getAccountId(), VALID_CUSTOMER_ID, VALID_ACCOUNT_TYPE, VALID_INITIAL_BALANCE, true);
        Account differentAccount = new Account(VALID_CUSTOMER_ID, VALID_ACCOUNT_TYPE, VALID_INITIAL_BALANCE);

        assertEquals(account, sameAccount);
        assertNotEquals(account, differentAccount);
        assertEquals(account.hashCode(), sameAccount.hashCode());
        assertNotEquals(account.hashCode(), differentAccount.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = String.format("Account{id='%s', customerId='%s', type=%s, balance=%s, active=%s}",
                account.getAccountId(), VALID_CUSTOMER_ID, VALID_ACCOUNT_TYPE, VALID_INITIAL_BALANCE, true);
        assertEquals(expectedString, account.toString());
    }
}
