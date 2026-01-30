Here's the JUnit 5 test class for the Transaction class:

```java
package com.banking.transaction.domain;

import com.banking.core.domain.Money;
import com.banking.core.domain.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private static final String ACCOUNT_ID = "123456";
    private static final TransactionType TYPE = TransactionType.DEPOSIT;
    private static final Money AMOUNT = new Money(new BigDecimal("100.00"));
    private static final String DESCRIPTION = "Test transaction";

    @Test
    void testCreateTransaction() {
        Transaction transaction = new Transaction(ACCOUNT_ID, TYPE, AMOUNT, DESCRIPTION);

        assertNotNull(transaction.getTransactionId());
        assertEquals(ACCOUNT_ID, transaction.getAccountId());
        assertEquals(TYPE, transaction.getType());
        assertEquals(AMOUNT, transaction.getAmount());
        assertNotNull(transaction.getTimestamp());
        assertEquals(DESCRIPTION, transaction.getDescription());
        assertNull(transaction.getRelatedAccountId());
    }

    @Test
    void testCreateTransactionWithRelatedAccountId() {
        String relatedAccountId = "789012";
        Transaction transaction = new Transaction(ACCOUNT_ID, TYPE, AMOUNT, DESCRIPTION, relatedAccountId);

        assertEquals(relatedAccountId, transaction.getRelatedAccountId());
    }

    @Test
    void testCreateTransactionWithNullDescription() {
        Transaction transaction = new Transaction(ACCOUNT_ID, TYPE, AMOUNT, null);

        assertEquals("", transaction.getDescription());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testCreateTransactionWithInvalidAccountId(String invalidAccountId) {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(invalidAccountId, TYPE, AMOUNT, DESCRIPTION));
    }

    @Test
    void testCreateTransactionWithNullType() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(ACCOUNT_ID, null, AMOUNT, DESCRIPTION));
    }

    @Test
    void testCreateTransactionWithNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(ACCOUNT_ID, TYPE, null, DESCRIPTION));
    }

    @Test
    void testEquals() {
        Transaction transaction1 = new Transaction(ACCOUNT_ID, TYPE, AMOUNT, DESCRIPTION);
        Transaction transaction2 = new Transaction(ACCOUNT_ID, TYPE, AMOUNT, DESCRIPTION);
        Transaction transaction3 = new Transaction("987654", TransactionType.WITHDRAWAL, new Money(new BigDecimal("50.00")), "Another transaction");

        assertEquals(transaction1, transaction1);
        assertNotEquals(transaction1, transaction2);
        assertNotEquals(transaction1, transaction3);
        assertNotEquals(transaction1, null);
        assertNotEquals(transaction1, new Object());
    }

    @Test
    void testHashCode() {
        Transaction transaction1 = new Transaction(ACCOUNT_ID, TYPE, AMOUNT, DESCRIPTION);
        Transaction transaction2 = new Transaction(ACCOUNT_ID, TYPE, AMOUNT, DESCRIPTION);

        assertNotEquals(transaction1.hashCode(), transaction2.hashCode());
    }

    @Test
    void testToString() {
        Transaction transaction = new Transaction(ACCOUNT_ID, TYPE, AMOUNT, DESCRIPTION);
        String expectedString = String.format("Transaction{id='%s', accountId='%s', type=%s, amount=%s, timestamp=%s, description='%s'}",
                transaction.getTransactionId(), ACCOUNT_ID, TYPE, AMOUNT, transaction.getTimestamp(), DESCRIPTION);

        assertEquals(expectedString, transaction.toString());
    }
}
```
