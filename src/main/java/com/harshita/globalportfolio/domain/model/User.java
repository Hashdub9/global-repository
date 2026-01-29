package com.harshita.globalportfolio.domain.model;

import com.harshita.globalportfolio.data.MarketData;
import com.harshita.globalportfolio.domain.service.TradeService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

public class User {

    private final String name;
    private final Currency homeCurrency;     // User’s home currency
    private final Portfolio portfolio;
    private final TradeService tradeService;
    private final MarketData md;             // Needed for FX during reporting

    private LocalDate lastDate; // last successful trade date (null initially)

    // constructor — matches PortfolioSystem
    public User(String name, Currency homeCurrency, MarketData md) {
        this.name = name;
        this.homeCurrency = homeCurrency;
        this.md = md;
        this.portfolio = new Portfolio();
        this.tradeService = new TradeService(md);
    }

    public void buy(LocalDate date, Ticker ticker, int qty) {
        BigDecimal usd = tradeService.doBuy(portfolio, lastDate, date, ticker, qty);
        lastDate = date;

        System.out.println(
                name + " BUY " + qty + " " + ticker +
                        " on " + date + " for USD " + usd
        );
    }

    public void sell(LocalDate date, Ticker ticker, int qty) {
        BigDecimal usd = tradeService.doSell(portfolio, lastDate, date, ticker, qty);

        if (usd == null) {
            int owned = portfolio.sharesOf(ticker);
            System.out.println(
                    "ERROR: cannot sell " + qty + " shares of " + ticker +
                            ". Owned=" + owned
            );
            return;
        }

        lastDate = date;

        System.out.println(
                name + " SELL " + qty + " " + ticker +
                        " on " + date + " for USD " + usd
        );
    }

    // USD + user home currency (date entered by user)
    public void viewPortfolio(LocalDate reportDate) {
        System.out.println("---- Portfolio for " + name + " (as of " + reportDate + ") ----");
        System.out.println("Home currency: " + homeCurrency);

        BigDecimal usdToHome = md.fx(reportDate, Currency.USD, homeCurrency);

        portfolio.allPositions().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {
                    Ticker ticker = e.getKey();
                    Position pos = e.getValue();

                    if (pos.getShares() > 0) {
                        BigDecimal usd = pos.getUsdInvested();

                        if (homeCurrency == Currency.USD) {
                            System.out.println(
                                    ticker +
                                            " | shares=" + pos.getShares() +
                                            " | USD=" + usd
                            );
                        } else {
                            BigDecimal home = usd
                                    .multiply(usdToHome)
                                    .setScale(2, RoundingMode.HALF_UP);

                            System.out.println(
                                    ticker +
                                            " | shares=" + pos.getShares() +
                                            " | USD=" + usd +
                                            " | " + homeCurrency + "=" + home
                            );
                        }
                    }
                });

        System.out.println("------------------------------------------");
    }
}

