package com.harshita.globalportfolio.data;

import com.harshita.globalportfolio.domain.model.Currency;
import com.harshita.globalportfolio.domain.model.Money;
import com.harshita.globalportfolio.domain.model.StockQuote;
import com.harshita.globalportfolio.domain.model.Ticker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class SampleData {

    public static MarketData create() {

        LocalDate d1 = LocalDate.of(2026, 1, 10);
        LocalDate d2 = LocalDate.of(2026, 1, 11);
        LocalDate d3 = LocalDate.of(2026, 1, 12);

        Map<LocalDate, Map<Currency, BigDecimal>> fx = Map.of(
                d1, Map.of(
                        Currency.USD, bd("1.00"),
                        Currency.EUR, bd("1.10"),
                        Currency.GBP, bd("1.26"),
                        Currency.JPY, bd("0.0091")
                ),
                d2, Map.of(
                        Currency.USD, bd("1.00"),
                        Currency.EUR, bd("1.11"),
                        Currency.GBP, bd("1.25"),
                        Currency.JPY, bd("0.0090")
                ),
                d3, Map.of(
                        Currency.USD, bd("1.00"),
                        Currency.EUR, bd("1.09"),
                        Currency.GBP, bd("1.27"),
                        Currency.JPY, bd("0.0092")
                )
        );

        Map<LocalDate, Map<Ticker, StockQuote>> prices = Map.of(
                d1, Map.of(
                        Ticker.AAPL, q(Ticker.AAPL, "190", Currency.USD),
                        Ticker.SAP,  q(Ticker.SAP,  "135", Currency.EUR),
                        Ticker.SONY, q(Ticker.SONY, "14500", Currency.JPY)
                ),
                d2, Map.of(
                        Ticker.AAPL, q(Ticker.AAPL, "192", Currency.USD),
                        Ticker.SAP,  q(Ticker.SAP,  "136", Currency.EUR),
                        Ticker.SONY, q(Ticker.SONY, "14620", Currency.JPY)
                ),
                d3, Map.of(
                        Ticker.AAPL, q(Ticker.AAPL, "191", Currency.USD),
                        Ticker.SAP,  q(Ticker.SAP,  "134", Currency.EUR),
                        Ticker.SONY, q(Ticker.SONY, "14480", Currency.JPY)
                )
        );

        return new InMemoryMarketData(fx, prices);
    }

    private static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }

    private static StockQuote q(Ticker t, String price, Currency c) {
        return new StockQuote(t, new Money(bd(price), c));
    }
}
