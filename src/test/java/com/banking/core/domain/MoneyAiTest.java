package com.banking.core.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void constructorWithValidInputs() {
        Money money = new Money(BigDecimal.TEN, "USD");
        assertEquals(BigDecimal.TEN, money.getAmount());
        assertEquals("USD", money.getCurrency());
    }

    @Test
    void constructorWithDoubleAmount() {
        Money money = new Money(10.50, "EUR");
        assertEquals(new BigDecimal("10.50"), money.getAmount());
        assertEquals("EUR", money.getCurrency());
    }

    @Test
    void constructorWithNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> new Money((BigDecimal) null, "USD"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    void constructorWithInvalidCurrency(String invalidCurrency) {
        assertThrows(IllegalArgumentException.class, () -> new Money(BigDecimal.ONE, invalidCurrency));
    }

    @Test
    void constructorWithNullCurrency() {
        assertThrows(IllegalArgumentException.class, () -> new Money(BigDecimal.ONE, null));
    }

    @Test
    void addWithSameCurrency() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "USD");
        Money result = money1.add(money2);
        assertEquals(new BigDecimal("11"), result.getAmount());
        assertEquals("USD", result.getCurrency());
    }

    @Test
    void addWithDifferentCurrencies() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "EUR");
        assertThrows(IllegalArgumentException.class, () -> money1.add(money2));
    }

    @Test
    void subtractWithSameCurrency() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "USD");
        Money result = money1.subtract(money2);
        assertEquals(new BigDecimal("9"), result.getAmount());
        assertEquals("USD", result.getCurrency());
    }

    @Test
    void subtractWithDifferentCurrencies() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "EUR");
        assertThrows(IllegalArgumentException.class, () -> money1.subtract(money2));
    }

    @ParameterizedTest
    @CsvSource({
        "10, 5, true",
        "5, 10, false",
        "10, 10, false"
    })
    void isGreaterThan(BigDecimal amount1, BigDecimal amount2, boolean expected) {
        Money money1 = new Money(amount1, "USD");
        Money money2 = new Money(amount2, "USD");
        assertEquals(expected, money1.isGreaterThan(money2));
    }

    @Test
    void isGreaterThanWithDifferentCurrencies() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "EUR");
        assertThrows(IllegalArgumentException.class, () -> money1.isGreaterThan(money2));
    }

    @ParameterizedTest
    @CsvSource({
        "5, 10, true",
        "10, 5, false",
        "10, 10, false"
    })
    void isLessThan(BigDecimal amount1, BigDecimal amount2, boolean expected) {
        Money money1 = new Money(amount1, "USD");
        Money money2 = new Money(amount2, "USD");
        assertEquals(expected, money1.isLessThan(money2));
    }

    @Test
    void isLessThanWithDifferentCurrencies() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.ONE, "EUR");
        assertThrows(IllegalArgumentException.class, () -> money1.isLessThan(money2));
    }

    @Test
    void testEquals() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.TEN, "USD");
        Money money3 = new Money(BigDecimal.ONE, "USD");
        Money money4 = new Money(BigDecimal.TEN, "EUR");

        assertEquals(money1, money2);
        assertNotEquals(money1, money3);
        assertNotEquals(money1, money4);
        assertNotEquals(money1, null);
        assertNotEquals(money1, new Object());
    }

    @Test
    void testHashCode() {
        Money money1 = new Money(BigDecimal.TEN, "USD");
        Money money2 = new Money(BigDecimal.TEN, "USD");
        assertEquals(money1.hashCode(), money2.hashCode());
    }

    @Test
    void testToString() {
        Money money = new Money(BigDecimal.TEN, "USD");
        assertEquals("10 USD", money.toString());
    }
}
