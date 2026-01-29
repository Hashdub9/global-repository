package com.harshita.globalportfolio.app;

import com.harshita.globalportfolio.data.MarketData;
import com.harshita.globalportfolio.data.SampleData;
import com.harshita.globalportfolio.domain.model.Currency;
import com.harshita.globalportfolio.domain.model.Ticker;
import com.harshita.globalportfolio.system.PortfolioSystem;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        MarketData md = SampleData.create();
        PortfolioSystem sys = new PortfolioSystem(md);

        var h = sys.addUser("Harshita", Currency.EUR);

        h.buy(LocalDate.of(2026, 1, 10), Ticker.SAP, 2000);
        h.buy(LocalDate.of(2026, 1, 11), Ticker.AAPL, 1024);
        h.sell(LocalDate.of(2026, 1, 12), Ticker.SAP, 108);
        h.viewPortfolio(LocalDate.of(2026, 1, 12));

        var d = sys.addUser("Demo", Currency.GBP);

        d.buy(LocalDate.of(2026, 1, 10), Ticker.SONY, 112);
        d.viewPortfolio(LocalDate.of(2026, 1, 10));

    }
}