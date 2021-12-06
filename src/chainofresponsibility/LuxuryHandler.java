package chainofresponsibility;
import order.Order;
import database.Inventory;
import order.ParticularOrder;

import output.OutputStrategy;
import output.InvalidOutput;

public class LuxuryHandler implements OrderHandler {
    private OrderHandler successor = null;
    private OutputStrategy strategy;

    @Override
    public void handleOrder(Order order) throws Exception {
        Inventory inventoryObj = Inventory.getInstance();
        int luxuryItemCount = 0;
        boolean areQuantitiesFeasible = true;
        for(ParticularOrder temp : order.getOrders()){
            if(inventoryObj.getCategoryMap().get(temp.item).equals("Luxury")){
                luxuryItemCount = luxuryItemCount + temp.quantity;
                if(temp.quantity > inventoryObj.getIndividualItemMap().get(temp.getItem())){
                    areQuantitiesFeasible = false;
                    break;
                }
            }
        }

        if(!areQuantitiesFeasible){
            strategy = new InvalidOutput();
            strategy.printOutput(order);
        }
        else if(luxuryItemCount > 3 ){
            strategy = new InvalidOutput();
            strategy.printOutput(order,"Luxury");
        }
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
