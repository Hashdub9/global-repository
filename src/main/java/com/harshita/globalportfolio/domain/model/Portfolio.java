package com.harshita.globalportfolio.domain.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Portfolio {

    private final Map<Ticker, Position> positions = new HashMap<>();

    public Position getPosition(Ticker t) {
        return positions.computeIfAbsent(t, k -> new Position());
    }

    public int sharesOf(Ticker t) {
        return getPosition(t).getShares();
    }

    public void buy(Ticker t, int qty, BigDecimal usdAmount) {
        getPosition(t).addBuy(qty, usdAmount);
    }

    public void sell(Ticker t, int qty, BigDecimal usdAmount) {
        getPosition(t).addSell(qty, usdAmount);
    }

    public Map<Ticker, Position> allPositions() {
        return positions;
    }
}
