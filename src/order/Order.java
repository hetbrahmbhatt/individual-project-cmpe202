package order;

import java.util.List;

public class Order {

    List<ParticularOrder> order;

    public Order(){};

    public Order(List<ParticularOrder> order) {
        this.order = order;
    }

    public List<ParticularOrder> getOrders() {
        return order;
    }

    public void setOrders(List<ParticularOrder> order) {
        this.order = order;
    }
}
