package com.harshita;

import com.harshita.globalportfolio.data.MarketData;
import com.harshita.globalportfolio.data.SampleData;
import com.harshita.globalportfolio.domain.model.Ticker;
import com.harshita.globalportfolio.domain.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import com.harshita.globalportfolio.domain.model.Currency;

class UserTest {

    @Test
    void userCanBuySellAndViewPortfolio() {

        // Arrange
        MarketData md = SampleData.create();
        User user = new User("Harshita", Currency.USD, md);

        // Act
        user.buy(LocalDate.of(2026, 1, 10), Ticker.SAP, 100);
        user.sell(LocalDate.of(2026, 1, 10), Ticker.SAP, 20);

        // Assert (no exception = pass)
        user.viewPortfolio(LocalDate.of(2026, 1, 10));
    }
}

