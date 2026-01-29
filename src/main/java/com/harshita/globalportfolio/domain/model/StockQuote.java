package com.harshita.globalportfolio.domain.model;

import java.util.Objects;

public final class StockQuote {

    private final Ticker ticker;
    private final Money pricePerShare;

    public StockQuote(Ticker ticker, Money pricePerShare) {
        this.ticker = Objects.requireNonNull(ticker, "ticker");
        this.pricePerShare = Objects.requireNonNull(pricePerShare, "pricePerShare");
    }

    public Ticker getTicker() {
        return ticker;
    }

    public Money getPricePerShare() {
        return pricePerShare;
    }
}
