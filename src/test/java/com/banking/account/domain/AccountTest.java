Here's a JUnit 5 test class for the Account class:

```java
package com.banking.account.domain;

import com.banking.core.domain.AccountType;
import com.banking.core.domain.Money;
import com.banking.core.exception.InsufficientFundsException;
import com.banking.core.exception.InvalidAccountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;
    private static final String CUSTOMER_ID = "12345";
    private static final AccountType ACCOUNT_TYPE = AccountType.SAVINGS;
    private static final Money INITIAL_BALANCE = new Money(new BigDecimal("100.00"));

    @BeforeEach
    void setUp() {
        account = new Account(CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE);
    }

    @Test
    void testAccountCreation() {
        assertNotNull(account.getAccountId());
        assertEquals(CUSTOMER_ID, account.getCustomerId());
        assertEquals(ACCOUNT_TYPE, account.getAccountType());
        assertEquals(INITIAL_BALANCE, account.getBalance());
        assertTrue(account.isActive());
    }

    @Test
    void testInvalidCustomerId() {
        assertThrows(IllegalArgumentException.class, () -> new Account("", ACCOUNT_TYPE, INITIAL_BALANCE));
        assertThrows(IllegalArgumentException.class, () -> new Account(null, ACCOUNT_TYPE, INITIAL_BALANCE));
    }

    @Test
    void testNullAccountType() {
        assertThrows(IllegalArgumentException.class, () -> new Account(CUSTOMER_ID, null, INITIAL_BALANCE));
    }

    @Test
    void testNullInitialBalance() {
        assertThrows(IllegalArgumentException.class, () -> new Account(CUSTOMER_ID, ACCOUNT_TYPE, null));
    }

    @Test
    void testDeposit() {
        Money depositAmount = new Money(new BigDecimal("50.00"));
        account.deposit(depositAmount);
        assertEquals(new Money(new BigDecimal("150.00")), account.getBalance());
    }

    @Test
    void testDepositNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(new Money(new BigDecimal("-50.00"))));
    }

    @Test
    void testDepositZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(new Money(BigDecimal.ZERO)));
    }

    @Test
    void testWithdraw() {
        Money withdrawalAmount = new Money(new BigDecimal("50.00"));
        account.withdraw(withdrawalAmount);
        assertEquals(new Money(new BigDecimal("50.00")), account.getBalance());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        Money withdrawalAmount = new Money(new BigDecimal("150.00"));
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(withdrawalAmount));
    }

    @Test
    void testWithdrawNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new Money(new BigDecimal("-50.00"))));
    }

    @Test
    void testWithdrawZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new Money(BigDecimal.ZERO)));
    }

    @Test
    void testDeactivateAccount() {
        account.deactivate();
        assertFalse(account.isActive());
    }

    @Test
    void testActivateAccount() {
        account.deactivate();
        account.activate();
        assertTrue(account.isActive());
    }

    @Test
    void testOperationsOnInactiveAccount() {
        account.deactivate();
        assertThrows(InvalidAccountException.class, () -> account.deposit(new Money(new BigDecimal("50.00"))));
        assertThrows(InvalidAccountException.class, () -> account.withdraw(new Money(new BigDecimal("50.00"))));
    }

    @Test
    void testEquality() {
        Account sameAccount = new Account(account.getAccountId(), CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE, true);
        Account differentAccount = new Account(CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE);

        assertEquals(account, sameAccount);
        assertNotEquals(account, differentAccount);
    }

    @Test
    void testHashCode() {
        Account sameAccount = new Account(account.getAccountId(), CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE, true);
        assertEquals(account.hashCode(), sameAccount.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = String.format("Account{id='%s', customerId='%s', type=%s, balance=%s, active=%s}",
                account.getAccountId(), CUSTOMER_ID, ACCOUNT_TYPE, INITIAL_BALANCE, true);
        assertEquals(expectedString, account.toString());
    }
}
```
