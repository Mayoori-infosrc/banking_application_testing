package com.banking.transaction.domain;

import com.banking.core.domain.Money;
import com.banking.core.domain.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private static final String VALID_ACCOUNT_ID = "ACC123";
    private static final TransactionType VALID_TYPE = TransactionType.DEPOSIT;
    private static final Money VALID_AMOUNT = new Money(BigDecimal.TEN, "USD");
    private static final String VALID_DESCRIPTION = "Test transaction";

    @Test
    void testConstructorWithValidInputs() {
        Transaction transaction = new Transaction(VALID_ACCOUNT_ID, VALID_TYPE, VALID_AMOUNT, VALID_DESCRIPTION);

        assertNotNull(transaction.getTransactionId());
        assertEquals(VALID_ACCOUNT_ID, transaction.getAccountId());
        assertEquals(VALID_TYPE, transaction.getType());
        assertEquals(VALID_AMOUNT, transaction.getAmount());
        assertNotNull(transaction.getTimestamp());
        assertTrue(transaction.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(transaction.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(1)));
        assertEquals(VALID_DESCRIPTION, transaction.getDescription());
        assertNull(transaction.getRelatedAccountId());
    }

    @Test
    void testConstructorWithNullDescription() {
        Transaction transaction = new Transaction(VALID_ACCOUNT_ID, VALID_TYPE, VALID_AMOUNT, null);
        assertEquals("", transaction.getDescription());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void testConstructorWithInvalidAccountId(String invalidAccountId) {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(invalidAccountId, VALID_TYPE, VALID_AMOUNT, VALID_DESCRIPTION));
    }

    @Test
    void testConstructorWithNullType() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(VALID_ACCOUNT_ID, null, VALID_AMOUNT, VALID_DESCRIPTION));
    }

    @Test
    void testConstructorWithNullAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(VALID_ACCOUNT_ID, VALID_TYPE, null, VALID_DESCRIPTION));
    }

    @Test
    void testConstructorWithRelatedAccountId() {
        String relatedAccountId = "REL456";
        Transaction transaction = new Transaction(VALID_ACCOUNT_ID, VALID_TYPE, VALID_AMOUNT, VALID_DESCRIPTION, relatedAccountId);
        assertEquals(relatedAccountId, transaction.getRelatedAccountId());
    }

    @Test
    void testEqualsAndHashCode() {
        Transaction transaction1 = new Transaction(VALID_ACCOUNT_ID, VALID_TYPE, VALID_AMOUNT, VALID_DESCRIPTION);
        Transaction transaction2 = new Transaction(VALID_ACCOUNT_ID, VALID_TYPE, VALID_AMOUNT, VALID_DESCRIPTION);
        Transaction transaction3 = new Transaction("DIFF123", TransactionType.WITHDRAWAL, new Money(BigDecimal.ONE, "EUR"), "Different");

        assertNotEquals(transaction1, transaction2);
        assertNotEquals(transaction1.hashCode(), transaction2.hashCode());
        assertNotEquals(transaction1, transaction3);
        assertNotEquals(transaction1.hashCode(), transaction3.hashCode());
        assertEquals(transaction1, transaction1);
        assertNotEquals(transaction1, null);
        assertNotEquals(transaction1, new Object());
    }

    @Test
    void testToString() {
        Transaction transaction = new Transaction(VALID_ACCOUNT_ID, VALID_TYPE, VALID_AMOUNT, VALID_DESCRIPTION);
        String toString = transaction.toString();
        assertTrue(toString.contains(VALID_ACCOUNT_ID));
        assertTrue(toString.contains(VALID_TYPE.toString()));
        assertTrue(toString.contains(VALID_AMOUNT.toString()));
        assertTrue(toString.contains(VALID_DESCRIPTION));
    }
}
