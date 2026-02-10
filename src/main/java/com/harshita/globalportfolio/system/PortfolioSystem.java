package com.harshita.globalportfolio.system;

import com.harshita.globalportfolio.data.MarketData;
import com.harshita.globalportfolio.domain.model.Currency;
import com.harshita.globalportfolio.domain.model.User;

import java.util.HashMap;
import java.util.Map;

public class PortfolioSystem {

    private final MarketData md;
    private final Map<String, User> users = new HashMap<>();

    public PortfolioSystem(MarketData md) {
        this.md = md;
    }

    // UPDATED: now takes homeCurrency
    public User addUser(String name, Currency homeCurrency) {
        if (users.containsKey(name)) {
            throw new IllegalArgumentException("User already exists: " + name);
        }
        User u = new User(name, homeCurrency, md);
        users.put(name, u);
        return u;
    }

    public User getUser(String name) {
        User u = users.get(name);
        if (u == null) {
            throw new IllegalArgumentException("No such user: " + name);
        }
        return u;
    }
}
