package com.banking.core.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void testConstructorWithValidInputs() {
        Money money = new Money(BigDecimal.TEN, "USD");
        assertEquals(BigDecimal.TEN, money.getAmount());
        assertEquals("USD", money.getCurrency());
    }

    @Test
    void testConstructorWithDoubleAmount() {
        Money money = new Money(10.50, "EUR");
        assertEquals(new BigDecimal("10.50"), money.getAmount());
        assertEquals("EUR", money.getCurrency());
    }

    @Test
    void testConstructorWithNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> new Money((BigDecimal) null, "USD"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    void testConstructorWithInvalidCurrency(String invalidCurrency) {
        assertThrows(IllegalArgumentException.class, () -> new Money(BigDecimal.ONE, invalidCurrency));
    }

    @Test
    void testConstructorWithNullCurrency() {
        assertThrows(IllegalArgumentException.class, () -> new Money(BigDecimal.ONE, null));
    }

    @Test
    void testAddWithSameCurrency() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "USD");
        Money result = money1.add(money2);
        assertEquals(new BigDecimal("11"), result.getAmount());
        assertEquals("USD", result.getCurrency());
    }

    @Test
    void testAddWithDifferentCurrency() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "EUR");
        assertThrows(IllegalArgumentException.class, () -> money1.add(money2));
    }

    @Test
    void testSubtractWithSameCurrency() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "USD");
        Money result = money1.subtract(money2);
        assertEquals(new BigDecimal("9"), result.getAmount());
        assertEquals("USD", result.getCurrency());
    }

    @Test
    void testSubtractWithDifferentCurrency() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "EUR");
        assertThrows(IllegalArgumentException.class, () -> money1.subtract(money2));
    }

    @Test
    void testIsGreaterThanWithSameCurrency() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "USD");
        assertTrue(money1.isGreaterThan(money2));
        assertFalse(money2.isGreaterThan(money1));
    }

    @Test
    void testIsGreaterThanWithDifferentCurrency() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "EUR");
        assertThrows(IllegalArgumentException.class, () -> money1.isGreaterThan(money2));
    }

    @Test
    void testIsLessThanWithSameCurrency() {
        Money money1 = new Money(BigDecimal.ONE, "USD");
        Money money2 = new Money(BigDecimal.TEN, "USD");
        assertTrue(money1.isLessThan(money2));
        assertFalse(money2.isLessThan(money1));
    }

    @Test
    void testIsLessThanWithDifferentCurrency() {
        Money money1 = new Money(BigDecimal.ONE, "USD");
        Money money2 = new Money(BigDecimal.TEN, "EUR");
        assertThrows(IllegalArgumentException.class, () -> money1.isLessThan(money2));
    }

    @Test
    void testEqualsAndHashCode() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.TEN, "USD");
        Money money3 = new Money(BigDecimal.ONE, "USD");
        Money money4 = new Money(BigDecimal.TEN, "EUR");

        assertEquals(money1, money2);
        assertEquals(money1.hashCode(), money2.hashCode());
        assertNotEquals(money1, money3);
        assertNotEquals(money1, money4);
    }

    @Test
    void testToString() {
        Money money = new Money(BigDecimal.TEN, "USD");
        assertEquals("10 USD", money.toString());
    }
}
