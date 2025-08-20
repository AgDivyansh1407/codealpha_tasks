import java.util.*;

// ---------------------------------------
// Class representing a Stock
// ---------------------------------------
class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return symbol + " @ $" + price;
    }
}

// ---------------------------------------
// Class representing a Portfolio
// ---------------------------------------
class Portfolio {
    private Map<String, Integer> holdings;
    private double balance;

    public Portfolio(double balance) {
        this.balance = balance;
        holdings = new HashMap<>();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public boolean buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (cost <= balance) {
            balance -= cost;
            holdings.put(stock.getSymbol(), holdings.getOrDefault(stock.getSymbol(), 0) + quantity);
            return true;
        }
        return false;
    }

    public boolean sellStock(Stock stock, int quantity) {
        String symbol = stock.getSymbol();
        int held = holdings.getOrDefault(symbol, 0);
        if (held >= quantity) {
            holdings.put(symbol, held - quantity);
            balance += stock.getPrice() * quantity;
            return true;
        }
        return false;
    }

    public void printPortfolio() {
        System.out.println("========= PORTFOLIO =========");
        System.out.println("Cash Balance: $" + balance);
        System.out.println("Owned Stocks:");
        if (holdings.isEmpty()) {
            System.out.println("  (No stocks owned yet)");
        } else {
            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue() + " shares");
            }
        }
        System.out.println("=============================\n");
    }
}

// ---------------------------------------
// Main class with main() method
// ---------------------------------------
public class StockTradingApp {
    private static Map<String, Stock> market = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);

    public static void initializeMarket() {
        market.put("AAPL", new Stock("AAPL", 170.50));
        market.put("GOOG", new Stock("GOOG", 2850.10));
        market.put("TSLA", new Stock("TSLA", 125.75));
        market.put("AMZN", new Stock("AMZN", 3300.20));
    }

    public static void displayMarket() {
        System.out.println("------- STOCK MARKET -------");
        for (Stock stock : market.values()) {
            System.out.println(stock);
        }
        System.out.println("----------------------------\n");
    }

    public static void main(String[] args) {
        initializeMarket();
        Portfolio portfolio = new Portfolio(10000.0); // starting balance

        while (true) {
            System.out.println("========= STOCK TRADING MENU =========");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Deposit Cash");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    displayMarket();
                    break;
                case 2:
                    displayMarket();
                    System.out.print("Enter Stock Symbol to Buy: ");
                    String buySymbol = sc.next().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int buyQty = sc.nextInt();
                    Stock stockToBuy = market.get(buySymbol);
                    if (stockToBuy != null) {
                        if (portfolio.buyStock(stockToBuy, buyQty)) {
                            System.out.println("Purchase successful!");
                        } else {
                            System.out.println("Not enough balance.");
                        }
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Stock Symbol to Sell: ");
                    String sellSymbol = sc.next().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int sellQty = sc.nextInt();
                    Stock stockToSell = market.get(sellSymbol);
                    if (stockToSell != null) {
                        if (portfolio.sellStock(stockToSell, sellQty)) {
                            System.out.println("Sale completed!");
                        } else {
                            System.out.println("Not enough shares to sell.");
                        }
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;

                case 4:
                    portfolio.printPortfolio();
                    break;

                case 5:
                    System.out.print("Enter amount to deposit: ");
                    double amt = sc.nextDouble();
                    portfolio.deposit(amt);
                    System.out.println("Amount added to balance.");
                    break;

                case 6:
                    System.out.println("Thank you for using Stock Trading App!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid input. Try again.");
            }
        }
    }
}

