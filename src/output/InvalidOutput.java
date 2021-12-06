package output;

import database.Inventory;
import order.Card;
import order.Order;
import order.ParticularOrder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class InvalidOutput implements OutputStrategy {
    @Override
    // print if individual qty's are greater
    public void printOutput(Order order) throws Exception {
        Inventory inventoryObj = Inventory.getInstance();
        StringBuilder sb = new StringBuilder();
        for(ParticularOrder temp : order.getOrders()){
            if(temp.quantity > inventoryObj.getIndividualItemMap().get(temp.getItem())){
                sb.append(temp.item);
                sb.append(",");
            }
        }

        // Create and print the o/p
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String fileOutputName = "error_" + timestamp.getTime();
        String fileName = "data" + "/" + fileOutputName + ".txt";
        BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
        br.write("Please correct quantities");
        br.newLine();
        br.write(sb.toString());
        br.close();

    }

    // Print if overall category has more items
    @Override
    public void printOutput(Order order, String category) throws Exception {
        Inventory inventoryObj = Inventory.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(category + " category has more items");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String fileOutputName = "output_" + timestamp.getTime();
        String fileName = "data" + "/" + fileOutputName + ".txt";
        BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
        br.write(sb.toString());
        br.newLine();
        br.write("Please see quantities for below listed items of " + category + " category and correct them");
        br.newLine();
        StringBuilder outputItems = new StringBuilder();
        if(category.equals("Essential")){
            for(ParticularOrder temp : order.getOrders()){
                if(inventoryObj.getCategoryMap().get(temp.item).equals("Essential")){
                    outputItems.append(temp.item + "," + "");

                }
            }
        }
        else if(category.equals("Misc")){
            for(ParticularOrder temp : order.getOrders()){
                if(inventoryObj.getCategoryMap().get(temp.item).equals("Misc")){
                    outputItems.append(temp.item + "," + "");
                }
            }
        }
        else {
            for (ParticularOrder temp : order.getOrders()) {
                if (inventoryObj.getCategoryMap().get(temp.item).equals("Luxury")) {
                    outputItems.append(temp.item + "," + "");
                }
            }
        }
        br.write(outputItems.toString());
        br.close();

    }


}
