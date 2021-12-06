package order;

public class ParticularOrder {


    public String item;
    public int quantity;
    public String cardNumber;

    public ParticularOrder(String item, int quantity, String cardNumber) {
        this.item = item;
        this.quantity = quantity;
        this.cardNumber = cardNumber;
    }


    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
