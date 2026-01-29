package com.harshita.globalportfolio.domain.service;

import com.harshita.globalportfolio.data.MarketData;
import com.harshita.globalportfolio.domain.model.Money;
import com.harshita.globalportfolio.domain.model.Portfolio;
import com.harshita.globalportfolio.domain.model.StockQuote;
import com.harshita.globalportfolio.domain.model.Ticker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class TradeService {

    private final MarketData md;

    public TradeService(MarketData md) {
        this.md = md;
    }

    // returns USD amount (rounded). Throws if invalid date or backdated.
    public BigDecimal doBuy(Portfolio portfolio, LocalDate lastDate, LocalDate date, Ticker ticker, int qty) {
        validate(date, lastDate);
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");

        StockQuote q = md.quote(date, ticker);
        Money p = q.getPricePerShare();

        BigDecimal fx = md.fxToUsd(date, p.getCurrency());
        BigDecimal usdAmount = p.getAmount()
                .multiply(new BigDecimal(qty))
                .multiply(fx)
                .setScale(2, RoundingMode.HALF_UP);

        portfolio.buy(ticker, qty, usdAmount);
        return usdAmount;
    }

    // returns USD amount (rounded), or null if short-sell blocked.
    public BigDecimal doSell(Portfolio portfolio, LocalDate lastDate, LocalDate date, Ticker ticker, int qty) {
        validate(date, lastDate);
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");

        int owned = portfolio.sharesOf(ticker);
        if (owned < qty) {
            return null; // short-sell guard
        }

        StockQuote q = md.quote(date, ticker);
        Money p = q.getPricePerShare();

        BigDecimal fx = md.fxToUsd(date, p.getCurrency());
        BigDecimal usdAmount = p.getAmount()
                .multiply(new BigDecimal(qty))
                .multiply(fx)
                .setScale(2, RoundingMode.HALF_UP);

        portfolio.sell(ticker, qty, usdAmount);
        return usdAmount;
    }

    private void validate(LocalDate date, LocalDate lastDate) {
        if (date == null) throw new IllegalArgumentException("date cannot be null");

        if (!md.isSupportedDate(date)) {
            throw new IllegalArgumentException("Trade date not supported: " + date);
        }

        if (lastDate != null && date.isBefore(lastDate)) {
            throw new IllegalArgumentException(
                    "Cannot trade in the past. Last trade date=" + lastDate + " new date=" + date
            );
        }
    }
}
