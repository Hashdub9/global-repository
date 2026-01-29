package com.harshita.globalportfolio.data;

import com.harshita.globalportfolio.domain.model.Currency;
import com.harshita.globalportfolio.domain.model.StockQuote;
import com.harshita.globalportfolio.domain.model.Ticker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public interface MarketData {
    boolean isSupportedDate(LocalDate date);

    /**
     * Conversion factor from given currency to USD on this date.
     * Example: EUR->USD might be 1.10
     */
    BigDecimal fxToUsd(LocalDate date, Currency currency);

    /**
     * Quote (price + currency) for a ticker on this date.
     */
    StockQuote quote(LocalDate date, Ticker ticker);

    //generic conversion between any 2 currencies using USD as bridge

    default BigDecimal fx(LocalDate date, Currency from, Currency to) {
        if (from == to) return BigDecimal.ONE;

        if (to == Currency.USD) {
            return fxToUsd(date, from);
        }

        if (from == Currency.USD) {
            // USD -> X = 1 / (X -> USD)
            BigDecimal toUsd = fxToUsd(date, to);
            return BigDecimal.ONE.divide(toUsd, 10, RoundingMode.HALF_UP);
        }

        // from -> USD -> to
        BigDecimal fromToUsd = fxToUsd(date, from);
        BigDecimal usdToTo = fx(date, Currency.USD, to);
        return fromToUsd.multiply(usdToTo);
    }
}
