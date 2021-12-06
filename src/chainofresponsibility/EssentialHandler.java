package chainofresponsibility;
import database.Inventory;
import order.Order;
import order.ParticularOrder;

import output.OutputStrategy;
import output.InvalidOutput;

public class EssentialHandler implements OrderHandler {
    private OrderHandler successor = null;
    private OutputStrategy strategy;

    @Override
    public void handleOrder(Order order) throws Exception {
        Inventory inventoryObj = Inventory.getInstance();
        int essentialItemCount = 0;
        boolean areQuantitiesFeasible = true;

        // Check for invalid input
        for(ParticularOrder temp : order.getOrders()){
            if(inventoryObj.getCategoryMap().get(temp.item).equals("Essential")){
                essentialItemCount = essentialItemCount + temp.quantity;
                if(temp.quantity > inventoryObj.getIndividualItemMap().get(temp.getItem())){
                    areQuantitiesFeasible = false;
                    break;
                }
            }
        }
        // if the total essential qty greater than allowed qty
        if(!areQuantitiesFeasible){
            strategy = new InvalidOutput();
            strategy.printOutput(order);
        }
        // If quantity is greater than 3
        else if(essentialItemCount > 5 ){
            strategy = new InvalidOutput();
            strategy.printOutput(order,"Essential");
        }

        // Set the order to other
        else{
            if(successor != null){
                successor.handleOrder(order);
            }
        }
    }

    @Override
    public void setNext(OrderHandler next) {
        this.successor = next;
    }
}
