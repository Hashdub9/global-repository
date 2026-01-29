package com.harshita.globalportfolio.data;

import com.harshita.globalportfolio.domain.model.Currency;
import com.harshita.globalportfolio.domain.model.StockQuote;
import com.harshita.globalportfolio.domain.model.Ticker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class InMemoryMarketData implements MarketData {

    private final Map<LocalDate, Map<Currency, BigDecimal>> fx;
    private final Map<LocalDate, Map<Ticker, StockQuote>> prices;

    public InMemoryMarketData(Map<LocalDate, Map<Currency, BigDecimal>> fx,
                              Map<LocalDate, Map<Ticker, StockQuote>> prices) {
        if (fx == null || prices == null) {
            throw new IllegalArgumentException("market data maps cannot be null");
        }
        this.fx = fx;
        this.prices = prices;
    }

    @Override
    public boolean isSupportedDate(LocalDate date) {
        return date != null && fx.containsKey(date) && prices.containsKey(date);
    }

    @Override
    public BigDecimal fxToUsd(LocalDate date, Currency currency) {
        if (!isSupportedDate(date)) {
            throw new IllegalArgumentException("No market data for date: " + date);
        }
        BigDecimal r = fx.get(date).get(currency);
        if (r == null) {
            throw new IllegalArgumentException("No FX for " + currency + " on " + date);
        }
        return r;
    }

    @Override
    public StockQuote quote(LocalDate date, Ticker ticker) {
        if (!isSupportedDate(date)) {
            throw new IllegalArgumentException("No market data for date: " + date);
        }
        StockQuote q = prices.get(date).get(ticker);
        if (q == null) {
            throw new IllegalArgumentException("No quote for " + ticker + " on " + date);
        }
        return q;
    }
}
