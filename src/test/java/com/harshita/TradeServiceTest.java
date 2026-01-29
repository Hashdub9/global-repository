package com.harshita;

import com.harshita.globalportfolio.data.MarketData;
import com.harshita.globalportfolio.data.SampleData;
import com.harshita.globalportfolio.domain.model.Portfolio;
import com.harshita.globalportfolio.domain.model.Ticker;
import com.harshita.globalportfolio.domain.service.TradeService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TradeServiceTest {

    @Test
    void unsupportedDate_shouldThrow() {
        MarketData md = SampleData.create();
        TradeService svc = new TradeService(md);
        Portfolio p = new Portfolio();

        assertThrows(IllegalArgumentException.class, () ->
                svc.doBuy(p, null, LocalDate.of(2026, 1, 9), Ticker.SAP, 1)
        );
    }

    @Test
    void backdatedTrade_shouldThrow() {
        MarketData md = SampleData.create();
        TradeService svc = new TradeService(md);
        Portfolio p = new Portfolio();

        LocalDate d1 = LocalDate.of(2026, 1, 10);
        LocalDate d0 = LocalDate.of(2026, 1, 9);

        // lastDate is d1, but trade date is d0 -> should throw
        assertThrows(IllegalArgumentException.class, () ->
                svc.doBuy(p, d1, d0, Ticker.SAP, 1)
        );
    }

    @Test
    void shortSell_shouldReturnNull_andNotChangeShares() {
        MarketData md = SampleData.create();
        TradeService svc = new TradeService(md);
        Portfolio p = new Portfolio();

        LocalDate d = LocalDate.of(2026, 1, 10);

        BigDecimal sellUsd = svc.doSell(p, null, d, Ticker.AAPL, 1);
        assertNull(sellUsd);
        assertEquals(0, p.sharesOf(Ticker.AAPL));
    }

    @Test
    void conversion_shouldMatchSampleData() {
        MarketData md = SampleData.create();
        TradeService svc = new TradeService(md);
        Portfolio p = new Portfolio();

        // SAP on 2026-01-10: 135 EUR, EUR->USD 1.10, qty 2 => 297.00
        BigDecimal usd = svc.doBuy(p, null, LocalDate.of(2026, 1, 10), Ticker.SAP, 2);

        assertEquals(new BigDecimal("297.00"), usd);
        assertEquals(2, p.sharesOf(Ticker.SAP));
    }
}