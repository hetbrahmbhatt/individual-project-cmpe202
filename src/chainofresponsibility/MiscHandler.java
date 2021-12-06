package chainofresponsibility;
import java.util.HashMap;
import java.util.HashSet;
import database.Inventory;
import order.Order;
import order.ParticularOrder;
import output.OutputStrategy;
import output.InvalidOutput;
import output.ValidOutput;
import order.Card;

public class MiscHandler implements OrderHandler {
    private OrderHandler successor = null;
    private OutputStrategy strategy;
    @Override
    public void handleOrder(Order order) throws Exception {
        Inventory inventoryObj = Inventory.getInstance();
        int miscItemCount = 0;
        boolean areQuantitiesFeasible = true;
        for(ParticularOrder temp : order.getOrders()){
            if(inventoryObj.getCategoryMap().get(temp.item).equals("Misc")){
                miscItemCount = temp.quantity + miscItemCount;
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
        else if(miscItemCount > 6 ){
            strategy = new InvalidOutput();
            strategy.printOutput(order,"Misc");
        }

        else{
            strategy = new ValidOutput();
            HashMap<String,Integer>  categoryCountMap = inventoryObj.getCategorCountMap();
            HashMap<String,Integer>  individualCategoryMap = inventoryObj.getIndividualItemMap();
            HashSet<Card> presentCards = inventoryObj.getCards();

            // updating the inventory
            for(ParticularOrder temp : order.getOrders()){
                String itemCategory = inventoryObj.categoryMap.get(temp.item);
                String item = temp.item;
                categoryCountMap.put(itemCategory, categoryCountMap.get(itemCategory) - temp.quantity);
                individualCategoryMap.put(item,individualCategoryMap.get(item) - temp.quantity);
            }
            // If card does not exist, add it to the database
            for(ParticularOrder temp : order.getOrders()){
                if(!presentCards.contains(new Card(temp.cardNumber))){
                    presentCards.add(new Card(temp.cardNumber));
                }
            }
            inventoryObj.setIndividualCategoryCountMap(individualCategoryMap);
            inventoryObj.setCategoryCountMap(categoryCountMap);
            inventoryObj.setCards(presentCards);

            // print the output
            strategy.printOutput(order);

        }
    }

    @Override
    public void setNext(OrderHandler next) {
        this.successor = next;
    }
}
