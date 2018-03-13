package hello;

import java.math.BigDecimal;

public class Order {
    String isin;
    String quantity;
    String price;
    String customer;

    public Order(String isin, String quantity, String price, String customer) {
        this.isin = isin;
        this.quantity = quantity;
        this.price = price;
        this.customer = customer;
    }

    public String getIsin() {
        return isin;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getCustomer() {
        return customer;
    }
}
