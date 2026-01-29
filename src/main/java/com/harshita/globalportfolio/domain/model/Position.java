package com.harshita.globalportfolio.domain.model;

import java.math.BigDecimal;

public class Position {

    private int shares;
    private BigDecimal usdInvested;

    public Position() {
        this.shares = 0;
        this.usdInvested = BigDecimal.ZERO;
    }

    public int getShares() {
        return shares;
    }

    public BigDecimal getUsdInvested() {
        return usdInvested;
    }

    public void addBuy(int qty, BigDecimal usdAmount) {
        shares += qty;
        usdInvested = usdInvested.add(usdAmount);
    }

    public void addSell(int qty, BigDecimal usdAmount) {
        shares -= qty;
        usdInvested = usdInvested.subtract(usdAmount);
    }
}
