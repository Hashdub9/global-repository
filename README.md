Global Portfolio (Multi-Currency) — Java

A Java backend application that processes global stock buy/sell transactions, normalizes all monetary values into USD using historical exchange rates at trade time, and generates a per-user portfolio summary.
The portfolio can be viewed in USD and the user’s home currency (converted at a user-selected report date).

What the system does

Supports multiple users via PortfolioSystem
Each user can:
buy(date, ticker, quantity)
sell(date, ticker, quantity)
viewPortfolio(reportDate)

Every transaction is normalized to USD at the time of trade

Portfolio aggregates data per stock (ticker):
total shares held
net USD invested

Portfolio view shows:
USD (home/normalized currency)
User’s home currency (derived dynamically for a chosen report date)

Currency Normalization (Core Requirement)
Every transaction is converted to USD at the time of the trade, 
using the historical exchange rate for that date.

Formula used:
USD Amount = (Quantity × Local Price per Share) × Exchange Rate (for that date)


This formula is applied:
for every BUY
for every SELL
before any portfolio aggregation
USD is the single source of truth for all portfolio calculations.

Portfolio Aggregation Rules
BUY
increases share count
increases net USD invested

SELL
decreases share count
decreases net USD invested

Aggregation is done per ticker, per user.

Validations & Business Rules
Unsupported trade date
If market data (price or FX) is missing for a date, the trade is rejected.

No backdated trades
A user cannot execute a trade on a date earlier than their last successful trade.

Short-sell guard
Selling more shares than currently owned is blocked.
The transaction is skipped and an error is logged.

Precision
All monetary values use BigDecimal
Rounded to 2 decimal places (HALF_UP)

Home Currency Reporting (Extension)

- USD is the normalized base currency and is always shown in portfolio output.
- Each user has a configurable home currency (e.g., EUR, JPY, USD).
- If the user’s home currency is not USD, the portfolio displays:
    - USD amount
    - Home currency amount (converted using FX rate on the report date)
- If the user’s home currency is USD, the USD amount is displayed only once
  to avoid duplicate columns.
 
Each user has a home currency (e.g., USD, EUR, JPY).
Portfolio values are always stored in USD.
When viewing the portfolio, the user provides a report date.
USD values are converted to the user’s home currency using:
FX rate on the report date
This allows the same portfolio to be viewed consistently across currencies.
Example:
AAPL | shares=1024 | USD=196608.00 | EUR=180374.31

Package Structure
com.harshita.globalportfolio
├─ app
│  └─ Main.java
├─ data
│  ├─ MarketData.java
│  ├─ InMemoryMarketData.java
│  └─ SampleData.java
├─ domain
│  ├─ model
│  │  ├─ Currency.java
│  │  ├─ Ticker.java
│  │  ├─ Money.java
│  │  ├─ StockQuote.java
│  │  ├─ Position.java
│  │  ├─ Portfolio.java
│  │  └─ User.java
│  └─ service
│     └─ TradeService.java
└─ system
└─ PortfolioSystem.java

How to Run
Run Main (IntelliJ IDEA)

Run:
com.harshita.globalportfolio.app.Main

Run Tests
IntelliJ: Run test classes directly

Maven:
mvn test

Sample Output
Harshita BUY 2000 SAP on 2026-01-10 for USD 297000.00
Harshita BUY 1024 AAPL on 2026-01-11 for USD 196608.00
Harshita SELL 108 SAP on 2026-01-12 for USD 15774.48

---- Portfolio for Harshita (as of 2026-01-12) ----
Home currency: EUR
AAPL | shares=1024 | USD=196608.00 | EUR=180374.31
SAP  | shares=1892 | USD=281225.52 | EUR=258005.06
------------------------------------------

Demo BUY 112 SONY on 2026-01-10 for USD 14778.40

---- Portfolio for Demo (as of 2026-01-10) ----
Home currency: JPY
SONY | shares=112 | USD=14778.40 | JPY=1624000.00
------------------------------------------

Assumptions & Out-of-Scope

Shares are whole numbers (no fractional shares).
The system tracks net USD invested, not profit/loss.
Users can buy any quantity of shares (no capital limits enforced).
Market prices and FX rates are provided by an in-memory data source:
Prices per share and exchange rates are treated as system-owned data
This avoids relying on user input for financial correctness.
Real-time pricing, fees, taxes, FIFO/LIFO accounting, and P&L are intentionally out of scope.